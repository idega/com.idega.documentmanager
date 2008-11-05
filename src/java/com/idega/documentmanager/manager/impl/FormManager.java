package com.idega.documentmanager.manager.impl;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.chiba.xml.xslt.TransformerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import com.idega.documentmanager.business.DocumentManager;
import com.idega.documentmanager.business.PersistenceManager;
import com.idega.documentmanager.business.XFormPersistenceType;
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
import com.idega.idegaweb.IWMainApplication;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.10 $
 *
 * Last modified: $Date: 2008/11/05 08:54:43 $ by $Author: civilis $
 */
@Scope("singleton")
@Service("xformsDocumentManager")
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
	
	public com.idega.documentmanager.business.Document createForm(LocalizedStringBean formName, String formType) {
		
		Form form = Form.createDocument(formName, getNewDMContext(), formType);
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
	
	public com.idega.documentmanager.business.Document openForm(Long formId) {
		
		Form form = Form.loadDocument(formId, getNewDMContext());
		return form == null ? null : form.getDocument();
	}
	
	public com.idega.documentmanager.business.Document openForm(Document xformsDoc) {
		
		Form form = Form.loadDocument(xformsDoc, getNewDMContext());
		return form.getDocument();
	}
	
	public com.idega.documentmanager.business.Document takeForm(Long formIdToTakeFrom) {
		
		Form form = Form.takeAndLoadDocument(formIdToTakeFrom, getNewDMContext());
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
	
	public void init(IWMainApplication iwma) throws InitializationException {
		if(inited) {			
			logger.log(Level.WARNING, "init(): tried to call, when already inited");
			return;
		}

		long start = System.currentTimeMillis();
		try {
			// setup ComponentsGenerator
			ComponentsGeneratorImpl.init(iwma);
			ComponentsGeneratorImpl componentsGenerator = ComponentsGeneratorImpl.getInstance();
			componentsGenerator.setTransformerService(getTransformerService());
			
			List<String> componentsTypes = null;
			
			Map<String, List<String>> categorizied = FormManagerUtil.getCategorizedComponentsTypes(getComponentsXforms());
			
			for (List<String> vals : categorizied.values()) {

				if(componentsTypes == null)
					componentsTypes = vals;
				else
					componentsTypes.addAll(vals);
			}
			
			CacheManager cacheManager = getCacheManager();
			
			cacheManager.setFormXformsTemplate(getFormXformsTemplate());
			cacheManager.setAllComponentsTypes(componentsTypes);
			cacheManager.setCategorizedComponentTypes(FormManagerUtil.getCategorizedComponentsTypes(getComponentsXforms()));
			cacheManager.setTypesByDatatype(FormManagerUtil.getComponentsTypesByDatatype(getComponentsXforms()));
			cacheManager.setComponentsXforms(getComponentsXforms());
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

//	@Autowired
//	@XFormPersistenceType("slide")
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

	@Autowired
	public void setXformsManagerFactory(XFormsManagerFactory xformsManagerFactory) {
		this.xformsManagerFactory = xformsManagerFactory;
	}

	public HtmlManagerFactory getHtmlManagerFactory() {
		return htmlManagerFactory;
	}

	@Autowired
	public void setHtmlManagerFactory(HtmlManagerFactory htmlManagerFactory) {
		this.htmlManagerFactory = htmlManagerFactory;
	}
}