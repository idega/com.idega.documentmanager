package com.idega.documentmanager.context;

import org.w3c.dom.Document;

import com.idega.documentmanager.business.PersistenceManager;
import com.idega.documentmanager.manager.HtmlManagerFactory;
import com.idega.documentmanager.manager.XFormsManagerFactory;
import com.idega.documentmanager.manager.impl.CacheManager;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.3 $
 *
 * Last modified: $Date: 2007/10/06 07:05:40 $ by $Author: civilis $
 */
public class DMContext {

	private CacheManager cacheManager;
	private PersistenceManager persistenceManager;
	private Document xformsXmlDoc;
	private XFormsManagerFactory xformsManagerFactory;
	private HtmlManagerFactory htmlManagerFactory;
	
	public XFormsManagerFactory getXformsManagerFactory() {
		return xformsManagerFactory;
	}
	public void setXformsManagerFactory(XFormsManagerFactory xformsManagerFactory) {
		this.xformsManagerFactory = xformsManagerFactory;
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
	public HtmlManagerFactory getHtmlManagerFactory() {
		return htmlManagerFactory;
	}
	public void setHtmlManagerFactory(HtmlManagerFactory htmlManagerFactory) {
		this.htmlManagerFactory = htmlManagerFactory;
	}
}