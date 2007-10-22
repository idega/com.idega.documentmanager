package com.idega.documentmanager.business.ext;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.1 $
 *
 * Last modified: $Date: 2007/10/22 15:38:17 $ by $Author: civilis $
 */
public class SimpleCaseFormCreateMetaInf {

	private String processDefinitionId;
	private String initiatorId;
	private String caseCategoryId;
	private String caseTypeId;
	
	public String getProcessDefinitionId() {
		return processDefinitionId;
	}
	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}
	public String getInitiatorId() {
		return initiatorId;
	}
	public void setInitiatorId(String initiatorId) {
		this.initiatorId = initiatorId;
	}
	public String getCaseCategoryId() {
		return caseCategoryId;
	}
	public void setCaseCategoryId(String caseCategoryId) {
		this.caseCategoryId = caseCategoryId;
	}
	public String getCaseTypeId() {
		return caseTypeId;
	}
	public void setCaseTypeId(String caseTypeId) {
		this.caseTypeId = caseTypeId;
	}
}