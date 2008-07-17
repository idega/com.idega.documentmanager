package com.idega.documentmanager.manager.impl;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.idega.documentmanager.business.component.properties.PropertiesMultiUploadDescription;
import com.idega.documentmanager.component.FormComponent;
import com.idega.documentmanager.component.beans.ComponentDataBean;
import com.idega.documentmanager.component.beans.ComponentMultiUploadBean;
import com.idega.documentmanager.component.beans.LocalizedStringBean;
import com.idega.documentmanager.component.properties.impl.ConstUpdateType;
import com.idega.documentmanager.manager.XFormsManagerMultiUploadDescription;
import com.idega.documentmanager.util.FormManagerUtil;
import com.idega.documentmanager.xform.Bind;
import com.idega.util.xml.XPathUtil;

/**
 * @author <a href="mailto:arunas@idega.com">Arūnas Vasmanas</a>
 * @version $Revision: 1.4 $
 * 
 * Last modified: $Date: 2008/07/17 11:54:43 $ by $Author: arunas $
 */
public class XFormsManagerMultiUploadDescriptionImpl extends XFormsManagerImpl implements
	XFormsManagerMultiUploadDescription {

    private static final String INSTANCE = "instance('data-instance')/Multi_file_upload_with_description_";
    private static final String REPEAT = "upload_entries_";
    private static final String ENTRY = "/entry";
    private static final String AT_START = "index('";
    private static final String AT_END = "')";


    private static final String APPEND_TITLE = ".title";
    private static final String APPEND_REMOVE = ".remove";
    private static final String APPEND_LABEL = ".label";
    private static final String APPEND_DESCRIPTION = ".text";
    
    private static final int TITLE_LABEL = 0;
    private static final int ADD_BUTTON_LABEL = 1;
    private static final int DESCRIPTION_BUTTON_LABEL = 2;
    private static final int REMOVE_BUTTON_LABEL = 3;
    
    
    final private XPathUtil labelsXPathUT = new XPathUtil(".//xf:label[@model]");
    final private XPathUtil insertXPUT = new XPathUtil(".//xf:insert[@at='last()']");
    final private XPathUtil repeatXPUT = new XPathUtil(".//xf:repeat");
    final private XPathUtil deleteXPUT = new XPathUtil(".//xf:delete");
    final private XPathUtil bindXPUT = new XPathUtil(".//xf:bind[@id='multiupload']");
    final private XPathUtil outputXPUT = new XPathUtil(".//xf:output[@value]");

    @Override
    protected ComponentDataBean newXFormsComponentDataBeanInstance() {
	return new ComponentMultiUploadBean();
    }

    @Override
    protected void loadXFormsComponentDataBean(FormComponent component,
	    Document xform, Element componentElement) {
	super.loadXFormsComponentDataBean(component, xform, componentElement);
	    	XPathUtil util = new XPathUtil(".//xf:bind[@id='bind."+componentElement.getAttribute(FormManagerUtil.id_att)+"']");
	    
		Element bindElement = (Element) util.getNode(xform.getFirstChild());
		
		Bind bind = Bind.load(bindElement);
		if (bind != null){
		    ComponentMultiUploadBean xforms_component = (ComponentMultiUploadBean) component.getXformsComponentDataBean();
		    xforms_component.setBind(bind);
		}
    }

    @Override
    public void removeComponentFromXFormsDocument(FormComponent component) {

	ComponentMultiUploadBean xforms_component = (ComponentMultiUploadBean) component
		.getXformsComponentDataBean();
	Element data_src_element = xforms_component.getMultiUploadInstance();

	if (data_src_element != null)
	    data_src_element.getParentNode().removeChild(data_src_element);

	super.removeComponentFromXFormsDocument(component);
    }

    // component attach to document

    @Override
    public void addComponentToDocument(FormComponent component) {

	super.addComponentToDocument(component);

	ComponentMultiUploadBean xfMultiUploadComponentBean = (ComponentMultiUploadBean) component.getXformsComponentDataBean();

	
	Element nodeElement = (Element) insertXPUT.getNode(xfMultiUploadComponentBean.getElement());
	nodeElement.setAttribute(FormManagerUtil.nodeset_att,constructInsertNodeset(component));
	
	nodeElement = (Element) repeatXPUT.getNode(xfMultiUploadComponentBean.getElement());
	nodeElement.setAttribute(FormManagerUtil.bind_att, constructBindValue(component.getId()));
	nodeElement.setAttribute(FormManagerUtil.id_att, constructRepeatId(component.getId()));

	nodeElement = bindXPUT.getNode(xfMultiUploadComponentBean.getBind().getBindElement());
	nodeElement.setAttribute(FormManagerUtil.id_att, constructBindValue(component.getId()));
	
	nodeElement = (Element) deleteXPUT.getNode(xfMultiUploadComponentBean.getElement());
	nodeElement.setAttribute(FormManagerUtil.nodeset_att,
		constructInsertNodeset(component));
	nodeElement.setAttribute(FormManagerUtil.at_att,
		constructDeleteAt(component.getId()));


	nodeElement = (Element)  outputXPUT.getNode(xfMultiUploadComponentBean.getElement());
	nodeElement.setAttribute(FormManagerUtil.value_att, constructOutputValueAt(component.getId()));
	nodeElement.removeAttribute(FormManagerUtil.ref_s_att);
	
    }

    private String constructInsertNodeset(FormComponent component) {

	StringBuffer buf = new StringBuffer();
	
	buf.append(INSTANCE).append(component.getId()).append(ENTRY);
	return buf.toString();

    }

    private String constructBindValue(String component_id) {

	StringBuffer buf = new StringBuffer();
	buf.append("upload_").append(component_id);
	return buf.toString();

    }

    private String constructRepeatId(String component_id) {
	StringBuffer buf = new StringBuffer();
	buf.append(REPEAT).append(component_id);
	return buf.toString();
    }

    private String constructDeleteAt(String component_id) {
	StringBuffer buf = new StringBuffer();
	buf.append(AT_START).append(REPEAT).append(component_id).append(AT_END);
	return buf.toString();
    }
    
    private String constructOutputValueAt(String component_id) {

	StringBuffer buf = new StringBuffer();
	buf.append("if(. !='' and ./description ='', instance('localized_strings')/").append(component_id).append(".info[@lang=instance('localized_strings')/current_language], '')");
	return buf.toString();

    }
    
    @Override
    public void update(FormComponent component, ConstUpdateType what) {
		
		switch (what) {
			
		case ADD_BUTTON_LABEL:
			updateAddButtonLabel(component);
			break;
			
		case REMOVE_BUTTON_LABEL:
			updateRemoveButtonLabel(component);
			break;
		case DESCRIPTION_BUTTON_LABEL:
			updateDescriptionButtonLabel(component);
			break;
		case LABEL:
		    	updateLabel(component);
		    	break;
		case ERROR_MSG:
			updateErrorMsg(component);
			break;
		default:
			break;
		}
	}
// 	getting all labels nodes from component   
    protected NodeList getLabelNodeList (FormComponent component){
	NodeList labels;
	ComponentMultiUploadBean xfMultiUploadComponent = (ComponentMultiUploadBean) component.getXformsComponentDataBean();
	synchronized (labelsXPathUT) {
	    labels = labelsXPathUT.getNodeset(xfMultiUploadComponent.getElement());
	}
	return labels;
    }

   protected void updateAddButtonLabel(FormComponent component) {
	
	PropertiesMultiUploadDescription properties = (PropertiesMultiUploadDescription)component.getProperties();
	LocalizedStringBean localizedText = properties.getAddButtonLabel();
	NodeList labels = getLabelNodeList(component);
		
	if(labels == null || labels.getLength() == 0)
		return;
	
	Element addButtonlabel = (Element)labels.item(ADD_BUTTON_LABEL);
	
	String ref = addButtonlabel.getAttribute(FormManagerUtil.ref_s_att);
	
	FormManagerUtil.putLocalizedText(!FormManagerUtil.isEmpty(ref) ? null : new StringBuilder(component.getId()).append(APPEND_LABEL).toString(), null, 
		addButtonlabel,
			component.getContext().getXformsXmlDoc(),
			localizedText
	);

   } 	
   
   protected void updateRemoveButtonLabel(FormComponent component) {
	
	PropertiesMultiUploadDescription properties = (PropertiesMultiUploadDescription)component.getProperties();
	LocalizedStringBean localizedText = properties.getRemoveButtonLabel();
	NodeList labels = getLabelNodeList(component);
	
	if(labels == null || labels.getLength() == 0)
		return;
	
	Element removeButtonlabel = (Element)labels.item(REMOVE_BUTTON_LABEL);
	String ref = removeButtonlabel.getAttribute(FormManagerUtil.ref_s_att);
	
	FormManagerUtil.putLocalizedText(!FormManagerUtil.isEmpty(ref) ? null : new StringBuilder(component.getId()).append(APPEND_REMOVE).toString(), null, 
		removeButtonlabel,
		component.getContext().getXformsXmlDoc(),
		localizedText
	);
   } 
   
   protected void updateDescriptionButtonLabel(FormComponent component) {
	
	PropertiesMultiUploadDescription properties = (PropertiesMultiUploadDescription)component.getProperties();
	LocalizedStringBean localizedText = properties.getDescriptionLabel();
	NodeList labels = getLabelNodeList(component);
	
	if(labels == null || labels.getLength() == 0)
		return;
	
	Element descriptionButtonlabel = (Element)labels.item(DESCRIPTION_BUTTON_LABEL);
	String ref = descriptionButtonlabel.getAttribute(FormManagerUtil.ref_s_att);
	
	FormManagerUtil.putLocalizedText(!FormManagerUtil.isEmpty(ref) ? null : new StringBuilder(component.getId()).append(APPEND_DESCRIPTION).toString(), null, 
		descriptionButtonlabel,
		component.getContext().getXformsXmlDoc(),
		localizedText
	);
  } 
   
   @Override
   protected void updateLabel(FormComponent component) {
       	PropertiesMultiUploadDescription properties = (PropertiesMultiUploadDescription)component.getProperties();
	LocalizedStringBean localizedText = properties.getLabel();
	NodeList labels = getLabelNodeList(component);
	
	if(labels == null || labels.getLength() == 0)
		return;
	
	Element title = (Element)labels.item(TITLE_LABEL);
	String ref = title.getAttribute(FormManagerUtil.ref_s_att);

	FormManagerUtil.putLocalizedText(!FormManagerUtil.isEmpty(ref) ? null : new StringBuilder(component.getId()).append(APPEND_TITLE).toString(), null, 
		title,
		component.getContext().getXformsXmlDoc(),
		localizedText
	);
       
   }
     
   @Override
   protected void updateErrorMsg(FormComponent component){
       
       ComponentDataBean xformsComponentDataBean = component.getXformsComponentDataBean();
       
       PropertiesMultiUploadDescription properties = (PropertiesMultiUploadDescription)component.getProperties();
       
       Element output = (Element) outputXPUT.getNode(xformsComponentDataBean.getElement());
            
       FormManagerUtil.putLocalizedText( new StringBuilder(component.getId()).append(".info").toString(), FormManagerUtil.localized_entries, output, component.getContext().getXformsXmlDoc(), properties.getErrorMsg());
       output.removeAttribute(FormManagerUtil.ref_s_att);
          
       
   }
    
    public LocalizedStringBean getAddButtonLabel(FormComponent component) {
	NodeList labels = getLabelNodeList(component);
	if(labels == null || labels.getLength() == 0)
		return null;
	return FormManagerUtil.getElementLocalizedStrings((Element)labels.item(ADD_BUTTON_LABEL), component.getContext().getXformsXmlDoc());
    }

    public LocalizedStringBean getRemoveButtonLabel(FormComponent component) {
	
	NodeList labels = getLabelNodeList(component);
	if(labels == null || labels.getLength() == 0)
		return null;
	return FormManagerUtil.getElementLocalizedStrings((Element)labels.item(REMOVE_BUTTON_LABEL), component.getContext().getXformsXmlDoc());

    }
    
    public LocalizedStringBean getDescriptionButtonLabel(FormComponent component) {
	
	NodeList labels = getLabelNodeList(component);
	if(labels == null || labels.getLength() == 0)
		return null;
	return FormManagerUtil.getElementLocalizedStrings((Element)labels.item(DESCRIPTION_BUTTON_LABEL), component.getContext().getXformsXmlDoc());

    }
    

}
