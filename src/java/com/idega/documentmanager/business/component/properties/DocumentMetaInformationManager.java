package com.idega.documentmanager.business.component.properties;

import com.idega.documentmanager.component.FormDocument;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.1 $
 *
 * Last modified: $Date: 2007/10/22 15:38:17 $ by $Author: civilis $
 */
public interface DocumentMetaInformationManager {

	public abstract void update(Object metaInf);
	
	public abstract Object resolve();
	
	public abstract void setDocumentComponent(FormDocument document);
}