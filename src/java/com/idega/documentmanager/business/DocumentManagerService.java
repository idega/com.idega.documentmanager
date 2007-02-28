package com.idega.documentmanager.business;

import javax.faces.context.FacesContext;

import com.idega.business.IBOService;
import com.idega.documentmanager.business.form.DocumentManager;
import com.idega.documentmanager.business.form.manager.util.InitializationException;

/**
 * 
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version 1.0
 *
 */
public interface DocumentManagerService extends IBOService {

	/**
	 * initiates Documentmanager if needed and returns instance
	 * 
	 * @return new fresh DocumentManager instance
	 * @throws InitializationException - DocumentManager could not be initialized
	 */
	public abstract DocumentManager newDocumentManager(FacesContext ctx) throws InitializationException;
}