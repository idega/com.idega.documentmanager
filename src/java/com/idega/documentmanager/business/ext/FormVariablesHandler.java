package com.idega.documentmanager.business.ext;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.idega.block.process.variables.Variable;
import com.idega.documentmanager.util.FormManagerUtil;
import com.idega.util.CoreConstants;
import com.idega.util.xml.XPathUtil;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.5 $
 *
 * Last modified: $Date: 2008/09/17 13:11:40 $ by $Author: civilis $
 */
public class FormVariablesHandler {

	private Document xform;
	private Node variablesContainer;
	private static XPathUtil mappingsXPath = new XPathUtil(".//@mapping[contains(., concat('_', $variableName))]");
	private static XPathUtil mappingElementXPath = new XPathUtil(".//node()[@mapping = $variableMapping]");
	private final String variableNameParameter = "variableName";
	private final String variableMappingParameter = "variableMapping";
	
	public FormVariablesHandler(Document xform) {
		
		if(xform == null)
			throw new NullPointerException("XForm document not provided");
		
		this.xform = xform; 
	}
	
	public FormVariablesHandler(Node variablesContainer) {
		
		if(variablesContainer == null)
			throw new NullPointerException("Variables container not provided");
		
		this.variablesContainer = variablesContainer; 
	}
	
	/**
	 * return variable mapped in the variables container provided
	 * 
	 * @param variableName - variable name to lookup. Without data type. I.e. if variable is mapped as string_actionTaken, only actionTaken should be provided here.
	 * @return
	 * 
	 * @throws UnsupportedOperationException - if data type found in the variable mapping is unsupported by process engine.
	 */
	public Variable getVariable(String variableName) {
		
		if(FormManagerUtil.isEmpty(variableName))
			throw new NullPointerException("Variable name not provided");
		
		Node instance = xform != null ? FormManagerUtil.getFormSubmissionInstanceDataElement(xform) : variablesContainer;
		
		NodeList mappings;
		
		synchronized (mappingsXPath) {
			
			mappingsXPath.clearVariables();
			mappingsXPath.setVariable(variableNameParameter, variableName);
			mappings = mappingsXPath.getNodeset(instance);
		}
		
		if(mappings == null || mappings.getLength() == 0)
			return null;
		
		for (int i = 0; i < mappings.getLength(); i++) {
			
			Node mapping = mappings.item(i);

			String mappingValue = mapping.getNodeValue();
			
			Variable variable = Variable.parseDefaultStringRepresentation(mappingValue);
			
			if(variableName.equals(variable.getName()))
				return variable;
		}
		
		return null;
	}
	
	/***
	 * 
	 * @param variable
	 * @param variableValue
	 * 
	 * @throws NullPointerException - if variable element not found for provided Variable. So check with getVariable first.
	 */
	public void setVariableValue(Variable variable, String variableValue) {
		
		if(variable == null)
			throw new NullPointerException("Variable not provided");
		
		Node instance = xform != null ? FormManagerUtil.getFormSubmissionInstanceDataElement(xform) : variablesContainer;
		
		String variableMapping = new StringBuilder(variable.getDataType().toString()).append(CoreConstants.COLON).append(variable.getName()).toString();
		Element mappingElement;
		
		synchronized (mappingElementXPath) {

			mappingElementXPath.clearVariables();
			mappingElementXPath.setVariable(variableMappingParameter, variableMapping);
			mappingElement = (Element)mappingElementXPath.getNode(instance);
		}
		
		if(mappingElement == null)
			throw new NullPointerException("Variable element not found for variable provided: "+variable);
		
		mappingElement.setTextContent(variableValue == null ? CoreConstants.EMPTY : variableValue);
	}
	
	/**
	 * creates variable element and mapping for it in the variables container
	 * @param elementName - variable element name, if not present, the variable name is used
	 * @param variable - variable to add to variables container
	 */
	public void createVariable(String elementName, Variable variable) {
		
		if(variable == null)
			throw new NullPointerException("Variable not provided");
		
		Variable existingVariable = getVariable(variable.getName());
		
		if(existingVariable != null) {
			variable.setName(existingVariable.getName());
			
			variable.setDataType(existingVariable.getDataType());
			return;
		}
		
		Node instance = xform != null ? FormManagerUtil.getFormSubmissionInstanceDataElement(xform) : variablesContainer;
		
		elementName = FormManagerUtil.isEmpty(elementName) ? variable.getName() : elementName;
		
		Element variableElement = instance.getOwnerDocument().createElement(elementName);
		instance.appendChild(variableElement);
		
		variableElement.setAttribute(FormManagerUtil.mapping_att, new StringBuilder(variable.getDataType().toString()).append(CoreConstants.COLON).append(variable.getName()).toString());
	}

	public static void main(String[] args) {
		
		try {
			
//			DocumentBuilder db = XmlUtil.getDocumentBuilder();
//			Document doc = db.parse("/Users/civilis/dev/workspace/eplatform-4/is.idega.idegaweb.egov.cases/resources/casesJbpmTemplates/simpleCasesProcessTemplate/forms/overviewCaseTask.xhtml");
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}