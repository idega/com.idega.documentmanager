package com.idega.documentmanager.business;

import javax.faces.context.FacesContext;

import com.idega.business.IBOServiceBean;
import com.idega.documentmanager.business.form.DocumentManager;
import com.idega.documentmanager.business.form.manager.FormManager;
import com.idega.documentmanager.business.form.manager.util.InitializationException;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version 1.0
 *
 */
public class DocumentManagerServiceBean extends IBOServiceBean implements DocumentManagerService {
	
	private static final long serialVersionUID = 2503096487027486624L;

	/**
	 * @see DocumentManagerService
	 */
	public DocumentManager newDocumentManager(FacesContext ctx) throws InitializationException {
		
		if(!FormManager.isInited()) {
			
			synchronized(DocumentManagerServiceBean.class) {
					
				if(!FormManager.isInited()) {
						
					FormManager.init(ctx);
				}
			}
		}
		
		return FormManager.getInstance();
	}
	
	public static DocumentManager sandboxNewFormManager() throws Exception {
		
		if(!FormManager.isInited()) {
			
			synchronized(DocumentManagerServiceBean.class) {
					
				if(!FormManager.isInited()) {
						
					FormManager.init(null);
				}
			}
		}
		
		return FormManager.getInstance();
	}
}
