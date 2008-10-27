package com.idega.documentmanager.business;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.idega.documentmanager.component.FormDocument;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.8 $
 * 
 *          Last modified: $Date: 2008/10/27 20:21:20 $ by $Author: civilis $
 */
public interface PersistenceManager {

	public abstract PersistedFormDocument loadForm(Long formId);

	public abstract PersistedFormDocument loadPopulatedForm(Long submissionId);

	public abstract PersistedFormDocument saveForm(FormDocument document)
			throws IllegalAccessException;

	public abstract PersistedFormDocument takeForm(Long formId);

	public abstract List<Form> getStandaloneForms();

	/**
	 * @param formId
	 *            - not null
	 * @param is
	 *            - not null
	 * @param identifier
	 *            - could be null, would be generated some random identifier
	 * @return submission id
	 * @throws IOException
	 */
	public abstract Long saveSubmittedData(Long formId, InputStream is,
			String identifier) throws IOException;

	/**
	 * 
	 * @param submissionId
	 * @param formId
	 *            - not null
	 * @param is
	 *            - not null
	 * @param identifier
	 *            - could be null, would be generated some random identifier
	 * @return submission id
	 * @throws IOException
	 */
	public abstract Long saveSubmittedDataByExistingSubmission(
			Long submissionId, Long formId, InputStream is, String identifier)
			throws IOException;

	public abstract List<Submission> getAllStandaloneFormsSubmissions();
}