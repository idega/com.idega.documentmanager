package com.idega.documentmanager.context;

import java.util.Stack;

import org.w3c.dom.Document;

import com.idega.documentmanager.business.PersistenceManager;
import com.idega.documentmanager.component.FormComponent;
import com.idega.documentmanager.manager.XFormsManagerFactory;
import com.idega.documentmanager.manager.impl.CacheManager;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.1 $
 *
 * Last modified: $Date: 2007/10/05 11:42:37 $ by $Author: civilis $
 */
public class DMContext {

	private CacheManager cacheManager;
	private PersistenceManager persistenceManager;
	private Document xformsXmlDoc;
	private FormComponent component;
	private Stack<FormComponent> contextRelevantState;
	private XFormsManagerFactory xformsManagerFactory;
	
	public XFormsManagerFactory getXformsManagerFactory() {
		return xformsManagerFactory;
	}

	public void setXformsManagerFactory(XFormsManagerFactory xformsManagerFactory) {
		this.xformsManagerFactory = xformsManagerFactory;
	}

	public DMContext() {
		contextRelevantState = new Stack<FormComponent>();
	}
	
	protected Stack<FormComponent> getContextRelevantState() {
		return contextRelevantState;
	}
	
	public FormComponent getComponent() {
		return component;
	}
	public void setComponent(FormComponent component) {
		this.component = component;
	}
	public CacheManager getCacheManager() {
		return cacheManager;
	}
	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}
	public PersistenceManager getPersistenceManager() {
		return persistenceManager;
	}
	public void setPersistenceManager(PersistenceManager persistenceManager) {
		this.persistenceManager = persistenceManager;
	}
	public Document getXformsXmlDoc() {
		return xformsXmlDoc;
	}
	public void setXformsXmlDoc(Document xformsXmlDoc) {
		this.xformsXmlDoc = xformsXmlDoc;
	}
	public void saveState() {
		contextRelevantState.push(getComponent());
	}
	public void restoreState() {
		setComponent(contextRelevantState.pop());
	}
}