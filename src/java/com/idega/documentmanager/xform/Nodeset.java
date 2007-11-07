package com.idega.documentmanager.xform;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.idega.documentmanager.util.FormManagerUtil;
import com.idega.util.CoreConstants;
import com.idega.util.xml.XPathUtil;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.3 $
 *
 * Last modified: $Date: 2007/11/07 15:02:29 $ by $Author: civilis $
 */
public class Nodeset implements Cloneable {

	private String path;
	private Element nodesetElement;
	
	protected Nodeset() { }

	public String getMapping() {
		
		return getNodesetElement().getAttribute(FormManagerUtil.mapping_att);
	}

	public void setMapping(String mapping) {

		if(mapping == null)
			getNodesetElement().removeAttribute(FormManagerUtil.mapping_att);
		else
			getNodesetElement().setAttribute(FormManagerUtil.mapping_att, mapping);
	}

	public String getPath() {
		return path;
	}

	protected void setPath(String path) {
		this.path = path;
	}
	
	private static final String instanceIdVariable = "instanceId";
	
	/**
	 * locates nodeset element in xf:instance/data.
	 * only simple path (i.e. the element name that's the child of data element) is being supported
	 * 
	 * @param model - current
	 * @param nodesetPath - only simple path (i.e. the element name that's the child of data element) is being supported
	 * @return Nodeset object with nodeset relevant data
	 */
	public static Nodeset locate(Element model, String nodesetPath) {

		Element instance = findInstance(model, nodesetPath);
		XPathUtil nodesetElementXPath = getNodesetElementXPath();
		
		Element nodesetElement;
		
		synchronized (nodesetElementXPath) {
			
			nodesetElementXPath.clearVariables();
			nodesetElementXPath.setVariable(nodesetPathVariable, 
					nodesetPath.contains(CoreConstants.SLASH) ? nodesetPath.substring(nodesetPath.lastIndexOf(CoreConstants.SLASH)+1) : nodesetPath);
			nodesetElement = (Element)nodesetElementXPath.getNode(instance);
		}
		
		if(nodesetElement == null)
			return null;

		Nodeset nodeset = new Nodeset();
		nodeset.setPath(nodesetPath);
		nodeset.setNodesetElement(nodesetElement);
		
		return nodeset;
	}
	
	private static Element findInstance(Element model, String nodesetPath) {
		
		String instanceId = nodesetPath.contains(FormManagerUtil.inst_start) ?
				
				nodesetPath.substring(
					nodesetPath.indexOf(FormManagerUtil.inst_start)
					+FormManagerUtil.inst_start.length(),
					nodesetPath.indexOf(FormManagerUtil.inst_end)
				)
		: null;
		
		
		if(nodesetPath.contains(FormManagerUtil.slash))
			nodesetPath = nodesetPath.substring(0, nodesetPath.indexOf(FormManagerUtil.slash));
		
		Element instance;
		
		if(FormManagerUtil.isEmpty(instanceId)) {
			instance = FormManagerUtil.getInstanceElement(model);
			
		} else {
			
			XPathUtil xpath = FormManagerUtil.getInstanceElementByIdXPath();
			
			synchronized (xpath) {
				
				xpath.clearVariables();
				xpath.setVariable(instanceIdVariable, instanceId);
				instance = (Element)xpath.getNode(model);
			}
		}
		
		return instance;
	}
	
	/**
	 * Creates nodeset element and places it under xf:instance/data element under model provided 
	 * 
	 * 
	 * @param model - instance to place nodeset in
	 * @param nodesetPath - path of nodeset. currently supported simple nodeset element name only
	 * @return created nodeset object
	 */
	public static Nodeset create(Element model, String nodesetPath) {
		
		Nodeset nodeset = locate(model, nodesetPath);
		
		if(nodeset == null) {
			
			Element instance = findInstance(model, nodesetPath);
			Document xform = model.getOwnerDocument();
			//create
			Element nodesetElement = xform.createElement(nodesetPath);
			Element parent = (Element)getNodesetElementParentXPath().getNode(instance);
			parent.appendChild(nodesetElement);
			
			nodeset = new Nodeset();
			nodeset.setNodesetElement(nodesetElement);
			nodeset.setPath(nodesetPath);
		}
			
		return nodeset;
	}
	
	public static Nodeset append(Element model, Element nodesetElement) {
		
		Element instance = FormManagerUtil.getInstanceElement(model);
		Element parent = (Element)getNodesetElementParentXPath().getNode(instance);
		parent.appendChild(nodesetElement);
		
		Nodeset nodeset = new Nodeset();
		nodeset.setNodesetElement(nodesetElement);
		nodeset.setPath(nodesetElement.getNodeName());
		return nodeset;
	}
	
	private static XPathUtil nodesetElementXPath;
	private static XPathUtil nodesetElementParentXPath;
	private static final String nodesetPathVariable = "nodesetPath";
	
	private static synchronized XPathUtil getNodesetElementXPath() {
		
		if(nodesetElementXPath == null)
			nodesetElementXPath = new XPathUtil(".//node()[name(.) = $nodesetPath]");
		
		return nodesetElementXPath;
	}
	
	private static synchronized XPathUtil getNodesetElementParentXPath() {
		
		if(nodesetElementParentXPath == null)
			nodesetElementParentXPath = new XPathUtil("./data");
		
		return nodesetElementParentXPath;
	}

	public Element getNodesetElement() {
		return nodesetElement;
	}

	protected void setNodesetElement(Element nodesetElement) {
		this.nodesetElement = nodesetElement;
	}
	
	public void remove() {
		
		getNodesetElement().getParentNode().removeChild(getNodesetElement());
		path = null;
	}
	
	@Override
	public Nodeset clone() {
		
		Nodeset nodeset = new Nodeset();
		nodeset.setNodesetElement((Element)(getNodesetElement() == null ? null : getNodesetElement().cloneNode(true)));
		nodeset.setPath(getPath());
		
		return nodeset;
	}
}