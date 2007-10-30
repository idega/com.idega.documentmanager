package com.idega.documentmanager.xform;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.idega.documentmanager.util.FormManagerUtil;
import com.idega.util.xml.XPathUtil;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.1 $
 *
 * Last modified: $Date: 2007/10/30 21:57:44 $ by $Author: civilis $
 */
public class Bind {

	
	protected Bind() { }
	
	private String id;
	private Element bindElement;
	private Nodeset nodeset;
	private String type;
	
	public Element getBindElement() {
		return bindElement;
	}
	public void setBindElement(Element bindElement) {
		this.bindElement = bindElement;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Nodeset getNodeset() {
		return nodeset;
	}
	public void setNodeset(Nodeset nodeset) {
		this.nodeset = nodeset;
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
	
	public static Bind locate(Document xform, String bindId) {
		
		Element model = FormManagerUtil.getFormInstanceModelElement(xform);
		XPathUtil bindElementXPath = getBindElementXPath();
		
		Element bindElement;
		
		synchronized (bindElementXPath) {
			
			bindElementXPath.clearVariables();
			bindElementXPath.setVariable("bindId", bindId);
			bindElement = (Element)bindElementXPath.getNode(model);
		}
		
		if(bindElement == null)
			return null;
		
		Bind bind = new Bind();
		bind.setId(bindId);
		bind.setBindElement(bindElement);
		
		return bind;
	}
	
	private static XPathUtil bindElementXPath;
	
	private static synchronized XPathUtil getBindElementXPath() {
		
		if(bindElementXPath == null)
			bindElementXPath = new XPathUtil("//xf:bind[@id=$bindId]");
		
		return bindElementXPath;
	}
}