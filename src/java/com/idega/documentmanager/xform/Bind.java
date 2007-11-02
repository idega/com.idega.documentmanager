package com.idega.documentmanager.xform;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.idega.documentmanager.util.FormManagerUtil;
import com.idega.util.xml.XPathUtil;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.2 $
 *
 * Last modified: $Date: 2007/11/02 15:04:56 $ by $Author: civilis $
 */
public class Bind {

	private static final Logger logger = Logger.getLogger(Bind.class.getName());
	
	protected Bind() { }
	
	private String id;
	private Element bindElement;
	private Nodeset nodeset;
	private String type;
	
	public Element getBindElement() {
		return bindElement;
		
	}
	protected void setBindElement(Element bindElement) {
		this.bindElement = bindElement;
	}
	public String getId() {
		return id;
	}
	protected void setId(String id) {
		this.id = id;
	}
	public Nodeset getNodeset() {
		
		if(nodeset == null) {
			
			String nodesetPath = getBindElement().getAttribute(FormManagerUtil.nodeset_att);
			if(nodesetPath == null || "".equals(nodesetPath)) {
				
				logger.log(Level.WARNING, "No nodeset attribute for bind element");
				return null;
			}

			Element model = FormManagerUtil.getFormInstanceModelElement(getBindElement().getOwnerDocument());
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
	
	/**
	 * locates bind element in the xforms document using xpath //xf:bind[@id=$bindId]
	 * creates new Bind object, which contains bind relevant data
	 * 
	 * @param xform - xforms document
	 * @param bindId - bind id
	 * @return - Bind object, which contains bind relevant data
	 */
	public static Bind locate(Document xform, String bindId) {
		
		Element model = FormManagerUtil.getFormInstanceModelElement(xform);
		XPathUtil bindElementXPath = getBindElementXPath();
		
		Element bindElement;
		
		synchronized (bindElementXPath) {
			
			bindElementXPath.clearVariables();
			bindElementXPath.setVariable(bindIdVariable, bindId);
			bindElement = (Element)bindElementXPath.getNode(model);
		}
		
		if(bindElement == null)
			return null;
		
		Bind bind = new Bind();
		bind.setId(bindId);
		bind.setBindElement(bindElement);
		
		return bind;
	}
	
	public static Bind create(Document xform, String bindId, Nodeset nodeset) {
		
		Bind bind = locate(xform, bindId);
		
		if(bind == null) {
			
			//create
			Element bindElement = xform.createElementNS(FormManagerUtil.xforms_namespace_uri, FormManagerUtil.bind_tag);;
			bindElement.setAttribute(FormManagerUtil.id_att, bindId);
			
			Element model = FormManagerUtil.getFormInstanceModelElement(xform);
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
		getBindElement().getParentNode().removeChild(getBindElement());
		id = null;
	}
}