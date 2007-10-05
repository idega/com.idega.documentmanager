package com.idega.documentmanager.business;

import java.io.FileInputStream;
import java.io.InputStream;

import javax.faces.context.FacesContext;
import javax.xml.parsers.DocumentBuilder;

import org.chiba.xml.xslt.TransformerService;
import org.w3c.dom.Document;

import com.idega.documentmanager.IWBundleStarter;
import com.idega.documentmanager.manager.impl.CacheManager;
import com.idega.documentmanager.util.FormManagerUtil;
import com.idega.idegaweb.DefaultIWBundle;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWMainApplication;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.1 $
 *
 * Last modified: $Date: 2007/10/05 11:42:32 $ by $Author: civilis $
 */
public class DocumentManagerFactory {
	
	private static final long serialVersionUID = 2503096487027486624L;
	
	private DocumentManager documentManager;
	
	public static final String COMPONENTS_XFORMS_CONTEXT_PATH = "resources/templates/form-components.xhtml";
	public static final String COMPONENTS_XSD_CONTEXT_PATH = "resources/templates/default-components.xsd";
	public static final String FORM_XFORMS_TEMPLATE_RESOURCES_PATH = "resources/templates/form-template.xhtml";
	
	private CacheManager cacheManager;

	public CacheManager getCacheManager() {
		return cacheManager;
	}
	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}
	public DocumentManagerFactory() { }
	/**
	 * @see DocumentManagerService
	 */
	public synchronized DocumentManager newDocumentManager(FacesContext ctx) {
		
		DocumentManager documentManager = getDocumentManager();
		
		if(!documentManager.isInited()) {
			
			IWMainApplication iwma = IWMainApplication.getIWMainApplication(ctx);
			TransformerService transformerService = (TransformerService)iwma.getAttribute(TransformerService.class.getName());
			
			CacheManager cacheManager = getCacheManager();
			cacheManager.initAppContext(ctx);
			
			String workspaceDir = System.getProperty(DefaultIWBundle.SYSTEM_BUNDLES_RESOURCE_DIR);
			
			if(workspaceDir == null)
				throw new NullPointerException("Workspace directory couldn't be resolved from system property: "+DefaultIWBundle.SYSTEM_BUNDLES_RESOURCE_DIR);
			
			String bundleInWorkspace = new StringBuilder(workspaceDir).append(FormManagerUtil.slash).append(IWBundleStarter.IW_BUNDLE_IDENTIFIER).append(FormManagerUtil.slash).toString();
			
			IWBundle bundle = iwma.getBundle(IWBundleStarter.IW_BUNDLE_IDENTIFIER);
			
			try {
				Document componentsXforms = getDocumentFromBundle(bundleInWorkspace, bundle, COMPONENTS_XFORMS_CONTEXT_PATH);
				Document componentsXsd = getDocumentFromBundle(bundleInWorkspace, bundle, COMPONENTS_XSD_CONTEXT_PATH);
				Document formXformsTemplate = getDocumentFromBundle(bundleInWorkspace, bundle, FORM_XFORMS_TEMPLATE_RESOURCES_PATH);
				
				documentManager.setComponentsXforms(componentsXforms);
				documentManager.setComponentsXsd(componentsXsd);
				documentManager.setFormXformsTemplate(formXformsTemplate);
				documentManager.setCacheManager(cacheManager);
				documentManager.setTransformerService(transformerService);
				documentManager.init();
				
			} catch (Exception e) {
				throw new RuntimeException("Failed to initialize document manager", e);
			}
		}
		
		return documentManager;
	}
	
	public DocumentManager getDocumentManager() {
		return documentManager;
	}
	public void setDocumentManager(DocumentManager documentManager) {
		this.documentManager = documentManager;
	}
	
	private static Document getDocumentFromBundle(String bundleInWorkspace, IWBundle bundle, String pathWithinBundle) throws Exception {
		Document doc = null;
		InputStream stream = null;
		if (bundleInWorkspace != null) {
			stream = new FileInputStream(bundleInWorkspace + pathWithinBundle);
		}
		else {
			stream = bundle.getResourceInputStream(pathWithinBundle);
		}

		DocumentBuilder doc_builder = FormManagerUtil.getDocumentBuilder();
		doc = doc_builder.parse(stream);
		stream.close();

		return doc;
	}
}