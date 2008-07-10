package com.idega.documentmanager.generator.impl;

import java.net.URI;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.chiba.adapter.ui.UIGenerator;
import org.chiba.adapter.ui.XSLTGenerator;
import org.chiba.web.flux.FluxAdapter;
import org.chiba.xml.xforms.exception.XFormsException;
import org.chiba.xml.xslt.TransformerService;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.idega.documentmanager.generator.ComponentsGenerator;
import com.idega.documentmanager.util.FormManagerUtil;
import com.idega.idegaweb.IWMainApplication;
import com.idega.repository.data.Singleton;
import com.idega.util.xml.XmlUtil;


/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version 1.0
 * 
 * Initial components description is kept in xforms document.<br />
 * This class parses all those components to html format components into xml document.
 * 
 * Last modified: $Date: 2008/07/10 14:21:14 $ by $Author: civilis $
 * 
 */
public class ComponentsGeneratorImpl implements Singleton, ComponentsGenerator  {
	
	protected static ComponentsGeneratorImpl me = null;
	
	private final URI final_xml_stylesheet_uri;
	private final URI temporal_xml_stylesheet_uri;
	
	private TransformerService transfService;
	private Document xformsDoc;

	private UIGenerator temporal_xml_components_generator;
	private UIGenerator final_xml_components_generator;
	
	public static ComponentsGeneratorImpl getInstance() {
		
		return me;
	}
	
	public synchronized static void init(IWMainApplication iwma) {
		
		if(me == null)
			me = new ComponentsGeneratorImpl(iwma);
	}
	
	protected ComponentsGeneratorImpl(IWMainApplication iwma) {
		
		String finalStyleshhetUriStr = "file:" + iwma.getApplicationRealPath() +
		"idegaweb/bundles/org.chiba.web.bundle/resources/xslt/components.xsl";
		final_xml_stylesheet_uri = URI.create(finalStyleshhetUriStr);
		
		String temporalStyleSheetUriStr = "file:" + iwma.getApplicationRealPath() +
		"idegaweb/bundles/org.chiba.web.bundle/resources/xslt/html4.xsl";
		
		temporal_xml_stylesheet_uri = URI.create(temporalStyleSheetUriStr);
	}
	
	public boolean isInitiated() {
		return xformsDoc != null && transfService != null;
	}
	
	public void setDocument(Document xforms_doc) {
		this.xformsDoc = xforms_doc;
	}
	
	public Document generateHtmlComponentsDocument() throws NullPointerException, ParserConfigurationException, XFormsException, Exception {
		
		return generateHtmlComponentsDocument(true);
	}
	
	protected Document generateHtmlComponentsDocument(boolean useAdapter) throws NullPointerException, ParserConfigurationException, XFormsException, Exception {
		
		if(!isInitiated()) {
			
			String errMsg = new StringBuffer("Either is not provided:")
			.append("\nxforms doc: ")
			.append(xformsDoc)
			.append("\ntransformer service: ")
			.append(transfService)
			.toString();
			
			throw new NullPointerException(errMsg);
		}
		
        /*
         * generate temporal xml document from components xforms document
         */
        UIGenerator gen = getTemporalXmlComponentsGenerator();
        
    	if(useAdapter) {
    		
    		FluxAdapter adapter = new FluxAdapter();
        	adapter.setXForms(xformsDoc);
        	adapter.init();
    	} else
    		copyLocalizationKeysToElements(xformsDoc);
    	
    	gen.setInput(xformsDoc);
    	
    	DocumentBuilder documentBuilder = XmlUtil.getDocumentBuilder();
        Document tempXmlDoc = documentBuilder.newDocument();
        
        gen.setOutput(tempXmlDoc);
    	gen.generate();
    	
    	return tempXmlDoc;
	}
	
	public Document generateBaseComponentsDocument() throws NullPointerException, ParserConfigurationException, XFormsException, Exception {
		
		return generateHtmlComponentsDocument(false);
	}
	
	private static void copyLocalizationKeysToElements(Document managed_doc) {
		
		Element components_container = (Element)managed_doc.getElementsByTagName(FormManagerUtil.group_tag).item(0);
		
		NodeList child_elements = components_container.getElementsByTagName("*");
		
		for (int i = 0; i < child_elements.getLength(); i++) {
			Element child = (Element)child_elements.item(i);
			
			if(child.hasAttribute(FormManagerUtil.ref_s_att)) {
				
				String ref = child.getAttribute(FormManagerUtil.ref_s_att);
				
				if(!FormManagerUtil.isRefFormCorrect(ref))
					continue;
				
				String key = FormManagerUtil.getKeyFromRef(ref);
				Node key_node = managed_doc.createTextNode(key);
				child.appendChild(key_node);
			}
		}
	}
	
	public void setTransformerService(TransformerService transf_service) {
		this.transfService = transf_service;
	}
	
	protected UIGenerator getTemporalXmlComponentsGenerator() {
		
		if(temporal_xml_components_generator == null || true) {
			
			synchronized (this) {
				
				if(temporal_xml_components_generator == null || true) {
					XSLTGenerator gen = new XSLTGenerator();
					gen.setTransformerService(transfService);
					gen.setStylesheetURI(temporal_xml_stylesheet_uri);
					gen.setParameter("selector-prefix", "s_");
					//gen.setParameter("remove-upload-prefix", removeUploadPrefix);
					gen.setParameter("data-prefix", "d_");
					gen.setParameter("trigger-prefix", "t_");
					//gen.setParameter("user-agent", "user-agent");
					//gen.setParameter("scripted", true);
					//gen.setParameter("scriptPath", "/idegaweb/bundles/" + IWBundleStarter.BUNDLE_IDENTIFIER + ".bundle/resources/javascript/");
					
					temporal_xml_components_generator = gen;
				}
			}
		}
		return temporal_xml_components_generator;
	}
	
	protected UIGenerator getFinalXmlComponentsGenerator() {
		
		if(final_xml_components_generator == null) {
			
			synchronized (this) {
				
				if(final_xml_components_generator == null) {
					XSLTGenerator gen = new XSLTGenerator();
					gen.setTransformerService(transfService);
					gen.setStylesheetURI(final_xml_stylesheet_uri);
					
					final_xml_components_generator = gen;
				}
			}
		}
		return final_xml_components_generator;
	}
}