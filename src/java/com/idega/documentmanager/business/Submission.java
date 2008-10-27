package com.idega.documentmanager.business;

import java.util.Date;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.1 $
 * 
 *          Last modified: $Date: 2008/10/27 20:21:00 $ by $Author: civilis $
 */
public interface Submission {

	public abstract Long getSubmissionId();

	public abstract String getSubmissionIdentifier();

	public abstract String getSubmissionStorageIdentifier();

	public abstract String getSubmissionStorageType();

	public abstract Date getDateSubmitted();

	public abstract Form getXform();

	public abstract Boolean getIsFinalSubmission();
}