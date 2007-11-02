package com.idega.documentmanager.xform;

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
public class Nodeset {

	private String path;
	private String mapping;
	private Element nodeset;
	
	protected Nodeset() { }

	public String getMapping() {
		
		if(mapping == null || "".equals(mapping))
			mapping = getNodeset().getAttribute(FormManagerUtil.mapping_att);
		
		return mapping;
	}

	public void setMapping(String mapping) {

		if(mapping == null)
			getNodeset().removeAttribute(FormManagerUtil.mapping_att);
		else
			getNodeset().setAttribute(FormManagerUtil.mapping_att, mapping);
		
		this.mapping = mapping;
	}

	public String getPath() {
		return path;
	}

	protected void setPath(String path) {
		this.path = path;
	}
	
	/**
	 * locates nodeset element in xf:instance/data.
	 * only simple path (i.e. the element name that's the child of data element) is being supported
	 * 
	 * @param model - parent of xf:instance
	 * @param nodesetPath - only simple path (i.e. the element name that's the child of data element) is being supported
	 * @return Nodeset object with nodeset relevant data
	 */
	public static Nodeset locate(Element model, String nodesetPath) {

		XPathUtil nodesetElementXPath = getNodesetElementXPath();
		
		Element nodesetElement;
		
		synchronized (nodesetElementXPath) {
			
			nodesetElementXPath.clearVariables();
			nodesetElementXPath.setVariable(nodesetPathVariable, nodesetPath);
			nodesetElement = (Element)nodesetElementXPath.getNode(model);
		}
		
		if(nodesetElement == null)
			return null;

		Nodeset nodeset = new Nodeset();
		nodeset.setPath(nodesetPath);
		nodeset.setNodeset(nodesetElement);
		
		return nodeset;
	}
	
	/**
	 * Creates nodeset element and places it under xf:instance/data element under model provided 
	 * 
	 * 
	 * @param model - model to place nodeset in
	 * @param nodesetPath - path of nodeset. currently supported simple nodeset element name only
	 * @return created nodeset object
	 */
	public static Nodeset create(Element model, String nodesetPath) {
		
		Nodeset nodeset = locate(model, nodesetPath);
		
		if(nodeset == null) {
			
			Document xform = model.getOwnerDocument();
			//create
			Element nodesetElement = xform.createElement(nodesetPath);
			Element parent = (Element)getNodesetElementParentXPath().getNode(model);
			parent.appendChild(nodesetElement);
			
			nodeset = new Nodeset();
			nodeset.setNodeset(nodesetElement);
			nodeset.setPath(nodesetPath);
		}
			
		return nodeset;
	}
	
	private static XPathUtil nodesetElementXPath;
	private static XPathUtil nodesetElementParentXPath;
	private static final String nodesetPathVariable = "nodesetPath";
	
	private static synchronized XPathUtil getNodesetElementXPath() {
		nodesetElementXPath = null;
		if(nodesetElementXPath == null)
			nodesetElementXPath = new XPathUtil(".//xf:instance[@id='data-instance']/data/child::node()[name(.) = $nodesetPath]");
		
		return nodesetElementXPath;
	}
	
	private static synchronized XPathUtil getNodesetElementParentXPath() {
		
		if(nodesetElementParentXPath == null)
			nodesetElementParentXPath = new XPathUtil(".//xf:instance[@id='data-instance']/data");
		
		return nodesetElementParentXPath;
	}

	public Element getNodeset() {
		return nodeset;
	}

	protected void setNodeset(Element nodeset) {
		this.nodeset = nodeset;
	}
	
	public void remove() {
		
		getNodeset().getParentNode().removeChild(getNodeset());
		path = null;
	}
}