package com.idega.documentmanager.manager.impl;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.idega.documentmanager.business.component.properties.PropertiesComponent;
import com.idega.documentmanager.business.component.properties.PropertiesPlain;
import com.idega.documentmanager.component.FormComponent;
import com.idega.documentmanager.component.beans.ComponentDataBean;
import com.idega.documentmanager.component.beans.LocalizedStringBean;
import com.idega.documentmanager.component.properties.impl.ConstUpdateType;
import com.idega.documentmanager.manager.XFormsManagerPlain;
import com.idega.documentmanager.util.FormManagerUtil;
import com.idega.documentmanager.xform.Bind;
import com.idega.util.xml.XPathUtil;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.6 $
 *
 * Last modified: $Date: 2008/08/12 06:07:41 $ by $Author: arunas $
 */
public class XFormsManagerPlainImpl extends XFormsManagerImpl implements XFormsManagerPlain {

	@Override
	public void update(FormComponent component, ConstUpdateType what) {
		
		switch (what) {
			
		case LABEL:
			updateLabel(component);
			break;
			
		case VARIABLE_NAME:
			updateVariableName(component);
			break;
		case TEXT:
			updateText(component);
			break;

		default:
			break;
		}
	}
	
	protected void updateText(FormComponent component) {
		
		ComponentDataBean xformsComponentDataBean = component.getXformsComponentDataBean();
		
		PropertiesPlain properties = (PropertiesPlain)component.getProperties();
		LocalizedStringBean localizedText = properties.getText();
		
		NodeList outputs = FormManagerUtil.getElementsContainingAttribute(xformsComponentDataBean.getElement(), FormManagerUtil.output_tag, FormManagerUtil.ref_s_att);
		Element output;
		String localizationKey = null;
		
		if(outputs == null || outputs.getLength() == 0) {
			
			if(localizedText == null)
				return;
				
			output = xformsComponentDataBean.getElement().getOwnerDocument().createElementNS(component.getFormDocument().getFormDataModelElement().getNamespaceURI(), FormManagerUtil.output_tag);
			output = (Element)xformsComponentDataBean.getElement().appendChild(output);
			localizationKey = component.getId()+".text";
			
		} else
			output = (Element)outputs.item(0);
		
		FormManagerUtil.putLocalizedText(localizationKey, null, 
				output,
				component.getContext().getXformsXmlDoc(),
				localizedText
		);
	}
	
	public LocalizedStringBean getText(FormComponent component) {
				
		Element output = component.getXformsComponentDataBean().getElement();
		
		if (!output.hasAttribute(FormManagerUtil.ref_s_att)) {
		    
		    XPathUtil outputXPUT = new XPathUtil(".//xf:output");
		    output = (Element) outputXPUT.getNode(output);
			  
		    if (output == null)
			    return null;
		}
		
		return FormManagerUtil.getElementLocalizedStrings((Element)output, component.getContext().getXformsXmlDoc());
	}
	
	@Override
	protected boolean removeTextNodes() {
		return true;
	}
	
	@Override
	protected void getBindingsAndNodesets(FormComponent component, Document componentsXForm) {

		ComponentDataBean xformsComponentDataBean = component.getXformsComponentDataBean();
		
		NodeList outputsWithBind = FormManagerUtil.getElementsContainingAttribute(xformsComponentDataBean.getElement(), FormManagerUtil.output_tag, FormManagerUtil.bind_att);
		
		if(outputsWithBind == null || outputsWithBind.getLength() == 0)
			return;
		
		Element outputWithBind = (Element)outputsWithBind.item(0);
 		
		String bindId = outputWithBind.getAttribute(FormManagerUtil.bind_att);
		String modelId = outputWithBind.getAttribute(FormManagerUtil.model_att);
		
		if(!FormManagerUtil.isEmpty(bindId)) {
			
			Bind bind = Bind.locate(componentsXForm, bindId, modelId);
			
			if(bind == null)
				throw new NullPointerException("Binding not found by bind id: "+bindId+(FormManagerUtil.isEmpty(modelId) ? "" : " and modelId: "+modelId));
			
			xformsComponentDataBean.setBind(bind);
		}
	}
	
	@Override
	protected void updateVariableName(FormComponent component) {
		
		ComponentDataBean xformsComponentDataBean = component.getXformsComponentDataBean();
		PropertiesComponent properties = component.getProperties();
		
		Bind bind = xformsComponentDataBean.getBind();
		
		if(bind == null && properties.getVariable() != null) {

			Element output = xformsComponentDataBean.getElement().getOwnerDocument().createElementNS(component.getFormDocument().getFormDataModelElement().getNamespaceURI(), FormManagerUtil.output_tag);
			output = (Element)xformsComponentDataBean.getElement().appendChild(output);
			
			Element label = xformsComponentDataBean.getElement().getOwnerDocument().createElementNS(component.getFormDocument().getFormDataModelElement().getNamespaceURI(), FormManagerUtil.label_tag);
			output.appendChild(label);
			
			bind = Bind.create(component.getContext().getXformsXmlDoc(), "bind."+component.getId(), null, null);
			output.setAttribute(FormManagerUtil.bind_att, bind.getId());
			xformsComponentDataBean.setBind(bind);
		}
		
		super.updateVariableName(component);
	}
	
	@Override
	protected void updateLabel(FormComponent component) {
		
		ComponentDataBean xformsComponentDataBean = component.getXformsComponentDataBean();
		
		PropertiesComponent props = component.getProperties();
		LocalizedStringBean locStr = props.getLabel();
		
		NodeList labels = xformsComponentDataBean.getElement().getElementsByTagName(FormManagerUtil.label_tag);
		
		if(labels == null || labels.getLength() == 0)
			return;
		
		Element label = (Element)labels.item(0);
		
		String ref = label.getAttribute(FormManagerUtil.ref_s_att);
		
		FormManagerUtil.putLocalizedText(!FormManagerUtil.isEmpty(ref) ? null : new StringBuilder(component.getId()).append(".label").toString(), null, 
				label,
				component.getContext().getXformsXmlDoc(),
				locStr
		);
	}
}