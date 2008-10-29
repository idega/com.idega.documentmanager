package com.idega.documentmanager.business;

import java.util.Date;

import org.w3c.dom.Document;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.2 $
 * 
 *          Last modified: $Date: 2008/10/29 11:25:09 $ by $Author: civilis $
 */
public interface Submission {

	public abstract Long getSubmissionId();

	public abstract String getSubmissionIdentifier();

	public abstract String getSubmissionStorageIdentifier();

	public abstract String getSubmissionStorageType();

	public abstract Date getDateSubmitted();

	public abstract Form getXform();

	public abstract Boolean getIsFinalSubmission();

	public abstract Document getSubmissionDocument();
}