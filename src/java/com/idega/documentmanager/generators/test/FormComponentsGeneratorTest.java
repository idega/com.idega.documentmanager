package com.idega.documentmanager.generators.test;

import java.io.FileInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.w3c.dom.Document;

import com.idega.documentmanager.business.form.manager.generators.FormComponentsGenerator;
import com.idega.documentmanager.business.form.manager.generators.IComponentsGenerator;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version 1.0
 * 
 * TODO: implement this test, now we get different implementations, so he cannot understand
 * if documents are equal neither using .isEqualNode method nor .equals
 * 
 * 
 */
public class FormComponentsGeneratorTest extends TestCase {
	
	public static void main(String[] args) {
		
		try {
			
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	        
	        factory.setNamespaceAware(true);
	        factory.setValidating(false);
	        DocumentBuilder documentBuilder = factory.newDocumentBuilder();

	        Document aaa = documentBuilder.parse(new FileInputStream("/Users/civilis/workspace/ePlatform35/com.idega.formbuilder/target/classes/com/idega/formbuilder/generators/test/myComponents.xml"));
	        
	        System.out.println("zzazazaza  xx 22  "+aaa.getClass());
	        
	        
	        if(true)
	        	return;
			
			
//			Class this_class = new FormComponentsGeneratorTest().getClass();
//			String absolute_components_xforms_path = this_class.getResource("myBasicTest.xhtml").getPath();
//			String absolute_components_xforms_stylesheet_path = this_class.getResource("htmlxml.xsl").getPath();
//			String absolute_components_stylesheet_path = this_class.getResource("components.xsl").getPath();
//			
//			IComponentsGenerator fcg = FormComponentsGenerator.getInstance();
//			
//			fcg.init(new String[] {
//					absolute_components_xforms_path,
//					absolute_components_xforms_stylesheet_path,
//					absolute_components_stylesheet_path
//			});
			
//			Document excepted_res_doc = fcg.generateBaseComponentsDocument();
//			
//			OutputStream os = new FileOutputStream(this_class.getResource("myComponents.xml").getPath());
//			DOMUtil.prettyPrintDOM(excepted_res_doc, os);
//			
//			System.out.println("done");
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	private Document getXmlResource(String fileName) throws Exception {
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        
        factory.setNamespaceAware(true);
        factory.setValidating(false);
        DocumentBuilder documentBuilder = factory.newDocumentBuilder();

        Document aaa = documentBuilder.parse(new FileInputStream("/Users/civilis/workspace/ePlatform35/com.idega.formbuilder/target/classes/com/idega/formbuilder/generators/test/myComponents.xml"));
        
        System.out.println("zzazazaza  xx 22  "+aaa.getClass());
        
        
//        
//		
//		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//        
//        factory.setNamespaceAware(true);
//        factory.setValidating(false);
//        DocumentBuilder documentBuilder = factory.newDocumentBuilder();
//        
//        System.out.println("path: "+getClass().getResource(fileName).getPath());
        
        return aaa;//documentBuilder.parse(new FileInputStream(getClass().getResource(fileName).getPath()));
    }
	
	public void testGenerateBaseComponentsDocument() {
		
		try {
			
			/*String absolute_components_xforms_path =*/ getClass().getResource("myBasicTest.xhtml").getPath();
			/*String absolute_components_xforms_stylesheet_path =*/ getClass().getResource("htmlxml.xsl").getPath();
			/*String absolute_components_stylesheet_path =*/ getClass().getResource("components.xsl").getPath();
			
			IComponentsGenerator fcg = FormComponentsGenerator.getInstance();
			
//			fcg.init(new String[] {
//					absolute_components_xforms_path,
//					absolute_components_xforms_stylesheet_path,
//					absolute_components_stylesheet_path
//			});
			
			Document excepted_res_doc = fcg.generateBaseComponentsDocument();
			
			Document expected = getXmlResource("myComponents.xml");
			
			System.out.println("x    444: "+excepted_res_doc.getClass());
			
//			DOMUtil.prettyPrintDOM(excepted_res_doc);
//			
//			System.out.println("_______xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx____________");
//			
//			DOMUtil.prettyPrintDOM(expected);
			
			if(expected.getDocumentElement().isEqualNode(excepted_res_doc.getDocumentElement()))
				System.out.println("wazaaaaaaaaaaaaaaaaaaaaaaa");
			
			else
				System.out.println(":(");
			
			System.out.println("Classsesssss: ");
			System.out.println("x    1: "+expected.getClass());
			System.out.println("x    2: "+excepted_res_doc.getClass());
			
			System.out.println("_____     1  "+expected.getDocumentElement().getChildNodes().getLength());
			System.out.println("_____     2  "+excepted_res_doc.getDocumentElement().getChildNodes().getLength());
			
			//Assert.assertEquals(getXmlResource("myComponents.xml").getDocumentElement(), excepted_res_doc.getDocumentElement());

			//Assert.assertEquals(getXmlResource("myComponents.xml"), excepted_res_doc);
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
			Assert.fail();
		}
	}
}
