package com.idega.documentmanager.business;

import java.util.Date;


/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.1 $
 *
 * Last modified: $Date: 2008/04/10 01:08:00 $ by $Author: civilis $
 */
public class PersistedForm {

	private String displayName;
	private Long formId;
	private Date dateCreated;

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public Long getFormId() {
		return formId;
	}

	public void setFormId(Long formId) {
		this.formId = formId;
	}
}