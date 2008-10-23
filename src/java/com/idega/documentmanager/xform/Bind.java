package com.idega.documentmanager.xform;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.idega.documentmanager.util.FormManagerUtil;
import com.idega.util.xml.XPathUtil;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.14 $
 *
 * Last modified: $Date: 2008/10/23 13:27:22 $ by $Author: civilis $
 */
public class Bind implements Cloneable {

	private static final Logger logger = Logger.getLogger(Bind.class.getName());
	
	protected Bind() { }
	
	private String id;
	private Element bindElement;
	private Nodeset nodeset;
	private String type;
	private String constraint;
	private String p3pType;
	private Boolean isRequired;
	private Boolean readonly;
	private String relevant;
	private Boolean isRelevant;
	
	public Element getBindElement() {
		return bindElement;
	}
	
	protected void setBindElement(Element bindElement) {
		this.bindElement = bindElement;
	}
	public String getId() {
		
		if(id == null)
			id = getBindElement().getAttribute(FormManagerUtil.id_att);
		
		return id;
	}
	protected void setId(String id) {
		this.id = id;
	}
	public Nodeset getNodeset() {
		
		if(nodeset == null) {
			
			String nodesetPath = getBindElement().getAttribute(FormManagerUtil.nodeset_att);
			
			if(FormManagerUtil.isEmpty(nodesetPath)) {
				
				logger.log(Level.WARNING, "No nodeset attribute for bind element");
				return null;
			}

			Element model = (Element)getBindElement().getParentNode();
			nodeset = Nodeset.locate(model, nodesetPath);
			
			if(nodeset == null)
				throw new NullPointerException("Nodeset path ("+nodesetPath+") specified in the nodeset attribute, but no nodeset found");
		}
			
		return nodeset;
	}
	public void setNodeset(Nodeset nodeset) {
		this.nodeset = nodeset;
		
		if(nodeset == null)
			getBindElement().removeAttribute(FormManagerUtil.nodeset_att);
		else
			getBindElement().setAttribute(FormManagerUtil.nodeset_att, nodeset.getPath());
	}
	public String getType() {
		
		if(type == null)
			type = getBindElement().getAttribute(FormManagerUtil.type_att);
		
		return type;
	}
	public void setType(String type) {
		
		this.type = type;
		
		if(type == null)
			getBindElement().removeAttribute(FormManagerUtil.type_att);
		else
			getBindElement().setAttribute(FormManagerUtil.type_att, type);
	}
	
	public String getConstraint() {
		
		if(constraint == null)
			constraint = getBindElement().getAttribute(FormManagerUtil.constraint_att);
		
		return constraint;
	}
	public void setConstraint(String constraint) {
		
		this.constraint = constraint;
		
		if(constraint == null)
			getBindElement().removeAttribute(FormManagerUtil.constraint_att);
		else
			getBindElement().setAttribute(FormManagerUtil.constraint_att, constraint);
	}
	
	/**
	 * locates bind element in the xforms document using xpath //xf:bind[@id=$bindId]
	 * creates new Bind object, which contains bind relevant data
	 * 
	 * @param modelId - model to locate in
	 * @param bindId - bind id
	 * @return - Bind object, which contains bind relevant data
	 */
	public static Bind locate(Document xform, String bindId, String modelId) {
//		TODO: remove modelId param
		
		XPathUtil bindElementXPath = getBindElementXPath();
		Element bindElement;
		
		synchronized (bindElementXPath) {
			
			bindElementXPath.clearVariables();
			bindElementXPath.setVariable(bindIdVariable, bindId);
			bindElement = (Element)bindElementXPath.getNode(xform);
		}
		
		if(bindElement == null)
			return null;
		
		Bind bind = new Bind();
		bind.setId(bindId);
		bind.setBindElement(bindElement);
		
		return bind;
	}
	
	private static final String modelIdVariable = "modelId";
	
	private static Element getModel(Document xform, String modelId) {
		
		Element model;
		
		if(FormManagerUtil.isEmpty(modelId))
			model = FormManagerUtil.getDefaultFormModelElement(xform);
			
		else {
			
			XPathUtil xpath = FormManagerUtil.getFormModelElementByIdXPath();
			
			synchronized (xpath) {
				
				xpath.clearVariables();
				xpath.setVariable(modelIdVariable, modelId);
				model = (Element)xpath.getNode(xform);
			}
		}
		
		return model;
	}
	
	public static Bind load(Element bindElement) {
		
		if(bindElement == null)
			return null;
		
		Bind bind = new Bind();
		bind.setId(bindElement.getAttribute(FormManagerUtil.id_att));
		bind.setBindElement(bindElement);
		
		return bind;
	}
	
	public static Bind create(Document xform, String bindId, String modelId, Nodeset nodeset) {
		
		Bind bind = locate(xform, bindId, modelId);
		
		if(bind == null) {
			
			Element model = getModel(xform, modelId);
			
			//create
			Element bindElement = xform.createElementNS(FormManagerUtil.xforms_namespace_uri, FormManagerUtil.bind_tag);
			bindElement.setAttribute(FormManagerUtil.id_att, bindId);
			
			model.appendChild(bindElement);
			
			bind = new Bind();
			bind.setId(bindId);
			bind.setBindElement(bindElement);
		}
		
		bind.setNodeset(nodeset);
			
		return bind;
	}
	
	private static XPathUtil bindElementXPath;
	private static final String bindIdVariable = "bindId";
	
	private static synchronized XPathUtil getBindElementXPath() {
		
		if(bindElementXPath == null)
			bindElementXPath = new XPathUtil(".//xf:bind[@id=$bindId]");
		
		return bindElementXPath;
	}
	
	public void remove() {
		
		Element bindElement = getBindElement();
		
//		String schemaType = bindElement.getAttribute(FormManagerUtil.type_att);
		
//		FIXME: perhaps remove schema type here if that's the last used or smth
//		if(schemaType != null && schemaType.startsWith(component.getId())) {
//			
//			Element schema_element = (Element)xforms_doc.getElementsByTagName(FormManagerUtil.schema_tag).item(0);
//			
//			Element type_element_to_remove = DOMUtil.getElementByAttributeValue(schema_element, "*", FormManagerUtil.name_att, schemaType);
//			
//			if(type_element_to_remove != null)
//				schema_element.removeChild(type_element_to_remove);
//		}
		
		bindElement.getParentNode().removeChild(bindElement);
		setBindElement(null);
		id = null;
	}
	
	@Override
	public Bind clone() {
		
		Bind bind = new Bind();
		bind.setBindElement((Element)(getBindElement() == null ? null : getBindElement().cloneNode(true)));
		bind.setId(getId());
		bind.setNodeset(getNodeset() == null ? null : getNodeset().clone());
		
		return bind;
	}
	
	public void setIsRequired(boolean isRequired) {
		
		this.isRequired = isRequired;
		
		if(isRequired)
			getBindElement().setAttribute(FormManagerUtil.required_att, "instance('control-instance')/required = 'true'");
		else
			getBindElement().removeAttribute(FormManagerUtil.required_att);
	}
	
	public boolean isRequired() {
		
		if(isRequired == null) {
			Element bind = getBindElement();
			isRequired = bind.hasAttribute(FormManagerUtil.required_att) && bind.getAttribute(FormManagerUtil.required_att).length() != 0;
		}
		
		return isRequired;
	}
	
	public boolean isReadonly() {
		
		if(readonly == null) {
			Element bind = getBindElement();
			readonly = bind.hasAttribute(FormManagerUtil.readonly_att) && bind.getAttribute(FormManagerUtil.readonly_att).length() != 0;
		}
		
		return readonly;
	}
	public void setReadonly(boolean readonly) {
		this.readonly = readonly;
		
		if(readonly)
			getBindElement().setAttribute(FormManagerUtil.readonly_att, "instance('control-instance')/readonly = 'true'");
		else
			getBindElement().removeAttribute(FormManagerUtil.readonly_att);
	}
	
	public void rename(String newName) {
		
		Nodeset nodeset = getNodeset();
		
		if(nodeset != null) {	
			nodeset.rename(newName);
		}
		
		setNodeset(nodeset);
	}
	
	public String getP3pType() {
		
		if(p3pType == null)
			p3pType = getBindElement().getAttribute(FormManagerUtil.p3ptype_att);
		
		return p3pType;
	}
	
	public void setP3pType(String p3pType) {
		
		if(p3pType == null)
			getBindElement().removeAttribute(FormManagerUtil.p3ptype_att);
		else
			getBindElement().setAttribute(FormManagerUtil.p3ptype_att, p3pType);
	}

	public String getRelevant() {
		
		if(relevant == null) {
			Element bind = getBindElement();
			relevant = bind.hasAttribute(FormManagerUtil.relevant_att) ? bind.getAttribute(FormManagerUtil.relevant_att) : null;
		}
		
		return relevant;
	}

	public void setRelevant(String relevant) {
		this.relevant = relevant;
		
		if(relevant != null)
			getBindElement().setAttribute(FormManagerUtil.relevant_att, relevant);
		else
			getBindElement().removeAttribute(FormManagerUtil.relevant_att);
	}

	/**
	 * 
	 * @return if the attribute contains xpath expression true() or false(), null, if attribute contains anything else 
	 */
	public Boolean getIsRelevant() {
		
		if(isRelevant == null) {

			String relVal = getRelevant();
			
			if(FormManagerUtil.xpath_true.equals(relVal))
				isRelevant = true;
			else if(FormManagerUtil.xpath_false.equals(relVal))
				isRelevant = false;
		}
		
		return isRelevant;
	}

	public void setIsRelevant(Boolean isRelevant) {
		
		if(isRelevant == null)
			throw new IllegalArgumentException("No value provided");
		
		if(isRelevant)
			setRelevant(FormManagerUtil.xpath_true);
		else
			setRelevant(FormManagerUtil.xpath_false);
		
		this.isRelevant = isRelevant;
	}
}