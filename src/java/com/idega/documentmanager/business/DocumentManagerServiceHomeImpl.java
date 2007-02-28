package com.idega.documentmanager.business;


import javax.ejb.CreateException;
import com.idega.business.IBOHomeImpl;

/**
 * 
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version 1.0
 *
 */
public class DocumentManagerServiceHomeImpl extends IBOHomeImpl implements DocumentManagerServiceHome {

	private static final long serialVersionUID = -5487236071491926453L;

	public Class getBeanInterfaceClass() {
		return DocumentManagerService.class;
	}

	public DocumentManagerService create() throws CreateException {
		return (DocumentManagerService) super.createIBO();
	}
}