package com.idega.documentmanager.business;

import javax.ejb.CreateException;
import com.idega.business.IBOHome;

import java.rmi.RemoteException;

/**
 * 
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version 1.0
 *
 */
public interface DocumentManagerServiceHome extends IBOHome {

	public DocumentManagerService create() throws CreateException, RemoteException;
}