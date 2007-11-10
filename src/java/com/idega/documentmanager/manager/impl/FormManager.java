package com.idega.documentmanager.manager.impl;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.chiba.xml.xslt.TransformerService;
import org.w3c.dom.Document;

import com.idega.documentmanager.business.DocumentManager;
import com.idega.documentmanager.business.FormLockException;
import com.idega.documentmanager.business.PersistenceManager;
import com.idega.documentmanager.business.component.ConstComponentCategory;
import com.idega.documentmanager.component.beans.LocalizedStringBean;
import com.idega.documentmanager.component.datatypes.ComponentType;
import com.idega.documentmanager.component.datatypes.ConstComponentDatatype;
import com.idega.documentmanager.context.DMContext;
import com.idega.documentmanager.form.impl.Form;
import com.idega.documentmanager.generator.impl.ComponentsGeneratorImpl;
import com.idega.documentmanager.manager.HtmlManagerFactory;
import com.idega.documentmanager.manager.XFormsManagerFactory;
import com.idega.documentmanager.util.FormManagerUtil;
import com.idega.documentmanager.util.InitializationException;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.4 $
 *
 * Last modified: $Date: 2007/11/10 13:52:48 $ by $Author: alexis $
 */
public class FormManager implements DocumentManager {
	
	private static Logger logger = Logger.getLogger(FormManager.class.getName());
	
	private boolean inited = false;
	
	private PersistenceManager persistenceManager;
	private TransformerService transformerService;
	private CacheManager cacheManager;
	private XFormsManagerFactory xformsManagerFactory;
	private HtmlManagerFactory htmlManagerFactory;
	
	private Document componentsXforms;
	private Document componentsXsd;
	private Document formXformsTemplate;
	
	public com.idega.documentmanager.business.Document createForm(String formId, LocalizedStringBean formName) throws NullPointerException, Exception {
		
		Form form = Form.createDocument(formId, formName, getNewDMContext());
		
		return form.getDocument();
	}
	
	private DMContext getNewDMContext() {
		
		DMContext context = new DMContext();
		context.setCacheManager(getCacheManager());
		context.setPersistenceManager(getPersistenceManager());
		context.setXformsManagerFactory(getXformsManagerFactory());
		context.setHtmlManagerFactory(getHtmlManagerFactory());
		return context;
	}
	
	public com.idega.documentmanager.business.Document openForm(String formId) throws NullPointerException, FormLockException, Exception {
		
		Form form = Form.loadDocument(formId, getNewDMContext());
		return form.getDocument();
	}
	
	public com.idega.documentmanager.business.Document openForm(Document xforms_doc) throws NullPointerException, Exception {
		
		Form form = Form.loadDocument(xforms_doc, getNewDMContext());
		return form.getDocument();
	}
	
	public com.idega.documentmanager.business.Document openForm(Document xforms_doc, String formId) throws NullPointerException, Exception {
		
		Form form = Form.loadDocument(formId, xforms_doc, getNewDMContext());
		return form.getDocument();
	}
	
	public FormManager() { }
	
	public List<String> getAvailableFormComponentsTypesList(ConstComponentCategory category) {
		
		return category == null ? getCacheManager().getAvailableFormComponentsTypesList()
				: getCacheManager().getComponentTypesByCategory(category.getComponentsCategory());
	}
	
	public List<ComponentType> getComponentsByDatatype(ConstComponentDatatype datatype) {
		return getCacheManager().getComponentTypesByDatatype(datatype.getDatatype());
	}
	
	public void init() throws InitializationException {
		if(inited) {			
			logger.log(Level.WARNING, "init(): tried to call, when already inited");
			return;
		}

		long start = System.currentTimeMillis();
		try {
			// setup ComponentsGenerator
			ComponentsGeneratorImpl componentsGenerator = ComponentsGeneratorImpl.getInstance();
			componentsGenerator.setTransformerService(getTransformerService());
			componentsGenerator.setDocument(getComponentsXforms());
			
			Document componentsXml = componentsGenerator.generateBaseComponentsDocument();
			List<String> componentsTypes = FormManagerUtil.gatherAvailableComponentsTypes(componentsXml);
			
			CacheManager cacheManager = getCacheManager();
			
			cacheManager.setFormXformsTemplate(getFormXformsTemplate());
			cacheManager.setAllComponentsTypes(componentsTypes);
			cacheManager.setCategorizedComponentTypes(FormManagerUtil.getCategorizedComponentsTypes(getComponentsXforms()));
			cacheManager.setTypesByDatatype(FormManagerUtil.getComponentsTypesByDatatype(getComponentsXforms()));
			cacheManager.setComponentsXforms(getComponentsXforms());
			cacheManager.setComponentsXml(componentsXml);
			cacheManager.setComponentsXsd(getComponentsXsd());
			
			inited = true;
			
			long end = System.currentTimeMillis();
			logger.info("FormManager initialized in: "+(end-start));
			
		} catch (Exception e) {

			logger.log(Level.SEVERE, "Could not initialize FormManager. See \"caused by\" for details.", e);
			throw new InitializationException("Could not initialize FormManager. See \"caused by\" for details.", e);
		}
	}
	
	public boolean isInited() {
		return inited;
	}
	
	public TransformerService getTransformerService() {
		return transformerService;
	}

	public void setTransformerService(TransformerService transformerService) {
		this.transformerService = transformerService;
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

	public Document getComponentsXforms() {
		return componentsXforms;
	}

	public void setComponentsXforms(Document componentsXforms) {
		this.componentsXforms = componentsXforms;
	}

	public Document getComponentsXsd() {
		return componentsXsd;
	}

	public void setComponentsXsd(Document componentsXsd) {
		this.componentsXsd = componentsXsd;
	}

	public Document getFormXformsTemplate() {
		return formXformsTemplate;
	}

	public void setFormXformsTemplate(Document formXformsTemplate) {
		this.formXformsTemplate = formXformsTemplate;
	}

	public XFormsManagerFactory getXformsManagerFactory() {
		return xformsManagerFactory;
	}

	public void setXformsManagerFactory(XFormsManagerFactory xformsManagerFactory) {
		this.xformsManagerFactory = xformsManagerFactory;
	}

	public HtmlManagerFactory getHtmlManagerFactory() {
		return htmlManagerFactory;
	}

	public void setHtmlManagerFactory(HtmlManagerFactory htmlManagerFactory) {
		this.htmlManagerFactory = htmlManagerFactory;
	}
}