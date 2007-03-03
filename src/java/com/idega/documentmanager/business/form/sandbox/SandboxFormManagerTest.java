package com.idega.documentmanager.business.form.sandbox;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilderFactory;

import org.chiba.xml.dom.DOMUtil;

import com.idega.documentmanager.business.DocumentManagerServiceBean;
import com.idega.documentmanager.business.form.Button;
import com.idega.documentmanager.business.form.ButtonArea;
import com.idega.documentmanager.business.form.Component;
import com.idega.documentmanager.business.form.ConstButtonType;
import com.idega.documentmanager.business.form.ConstComponentCategory;
import com.idega.documentmanager.business.form.Container;
import com.idega.documentmanager.business.form.Document;
import com.idega.documentmanager.business.form.DocumentManager;
import com.idega.documentmanager.business.form.Page;
import com.idega.documentmanager.business.form.PropertiesPage;
import com.idega.documentmanager.business.form.beans.LocalizedStringBean;
import com.idega.documentmanager.business.form.manager.FormManager;
import com.idega.documentmanager.business.form.manager.util.FormManagerUtil;

public class SandboxFormManagerTest {

	/**
	 * @param args 
	 * when launching:
	 * VM argument
	 * -Didegaweb.bundles.resource.dir=<your workspace dir>
	 * should be provided
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		try {
			
			DocumentManager fm = DocumentManagerServiceBean.sandboxNewFormManager();
			Document xx = fm.createForm("xx", null);
			xx = fm.getCurrentDocument();
			
			System.out.println("pages id list: "+xx.getContainedPagesIdList());
			Page page = xx.getPage(xx.getContainedPagesIdList().get(0));
			PropertiesPage page_properties = page.getProperties();
			System.out.println("page label: "+page_properties.getLabel());
			ButtonArea ba = page.getButtonArea();
			Button next = ba.getButton(new ConstButtonType(ConstButtonType.next_page_button));
			System.out.println("next button: "+next);
			System.out.println("button_area components _________________ : "+ba.getContainedComponentsIdList());
			Component c = ba.getComponent(ba.getContainedComponentsIdList().get(0));
			System.out.println("and got: ___ "+c);
			DOMUtil.prettyPrintDOM(c.getHtmlRepresentation(new Locale("en")));
			
			String xx0 = xx.getContainedPagesIdList().get(0);
			String xx1 = xx.getContainedPagesIdList().get(1);
			xx.getContainedPagesIdList().set(1, xx0);
			xx.getContainedPagesIdList().set(0, xx1);
			xx.rearrangeDocument();
			
			System.out.println("preview: "+xx.getConfirmationPage());
			System.out.println("thx: "+xx.getThxPage());
			Page np = xx.addPage(null);
			System.out.println("new page added: "+np);
			ba = np.createButtonArea(null);
			System.out.println("av ______: "+fm.getAvailableFormComponentsTypesList(new ConstComponentCategory(ConstComponentCategory.BASIC)));
			Component some_component = np.addComponent(fm.getAvailableFormComponentsTypesList(null).get(0), null);
			System.out.println("ir ka: "+some_component.getProperties().getLabel());
			some_component = np.addComponent(fm.getAvailableFormComponentsTypesList(null).get(3), null);
			System.out.println("ir ka selectas: "+some_component.getProperties().getLabel());
//			Page confirmation_page_added = xx.addConfirmationPage(null);
			
			System.out.println("thx txt: "+xx.getThxPage().getProperties().getText());
			LocalizedStringBean zz = xx.getThxPage().getProperties().getText();
			zz.setString(new Locale("en"), "and here it goes");
			xx.getThxPage().getProperties().setText(zz);
			
//			System.out.println("added conf page: "+confirmation_page_added);
			
			c = np.addComponent(fm.getAvailableFormComponentsTypesList(null).get(0), null);
			String xxx = np.getId();
			String xxx1 = c.getId();
			c.getProperties().setAutofillKey("raktukas");
			
			System.out.println("av: "+fm.getAvailableFormComponentsTypesList(new ConstComponentCategory(ConstComponentCategory.BASIC)));
			
			xx0 = np.getContainedComponentsIdList().get(0);
			xx1 = np.getContainedComponentsIdList().get(2);
			np.getContainedComponentsIdList().set(2, xx0);
			np.getContainedComponentsIdList().set(0, xx1);
			np.rearrangeComponents();
			
			System.out.println("opening ____________________");
			Document opened = ((FormManager)fm).openForm(xx.getXformsDocument());
			c = opened.getPage(xxx).getComponent(xxx1);
			System.out.println("autofill key: "+c.getProperties().getAutofillKey());
			
			System.out.println("opening ________badform____________");
			InputStream stream = new FileInputStream(new File("/Users/civilis/tmp/badform.xml"));
	        org.w3c.dom.Document doc = FormManagerUtil.getDocumentBuilder().parse(stream);
	        stream.close();
	        opened = ((FormManager)fm).openForm(doc);
	        
	        System.out.println("opening ________badform2____________");
	        Page some_super_cool_page = opened.getPage(opened.getContainedPagesIdList().get(0));
	        Component some_c = some_super_cool_page.getComponent(some_super_cool_page.getContainedComponentsIdList().get(1));
	        System.out.println("some c lab: "+some_c.getProperties().getLabel());
	        DOMUtil.prettyPrintDOM(some_c.getHtmlRepresentation(new Locale("en")));
	        
	        stream = new FileInputStream(new File("/Users/civilis/tmp/badform.xml"));
	        doc = FormManagerUtil.getDocumentBuilder().parse(stream);
	        stream.close();
	        opened = ((FormManager)fm).openForm(doc);
	        
	        for (String pid : opened.getContainedPagesIdList()) {
				Page p = opened.getPage(pid);
				
				for (String cid : p.getContainedComponentsIdList()) {
					
					Component pc = p.getComponent(cid);
					
					if(pc instanceof Container) {
		        		
		        		for (String cc : ((Container)pc).getContainedComponentsIdList()) {
		        			System.out.println("c component: "+((Container)pc).getComponent(cc).getProperties().getLabel());
		        			DOMUtil.prettyPrintDOM(((Container)pc).getComponent(cc).getHtmlRepresentation(new Locale("en")));
						}
		        		
		        	} else {
		        		System.out.println("component: "+pc.getProperties().getLabel());
		        		DOMUtil.prettyPrintDOM(pc.getHtmlRepresentation(new Locale("en")));
		        	}
				}
			}
	        
	        xx = fm.createForm("xx", null);
	        
	        Page p = xx.getPage(xx.getContainedPagesIdList().get(0));
	        System.out.println("____________________________ av " +fm.getAvailableFormComponentsTypesList(new ConstComponentCategory(ConstComponentCategory.BASIC)));
	        Component upload = p.addComponent(fm.getAvailableFormComponentsTypesList(new ConstComponentCategory(ConstComponentCategory.BASIC)).get(8), null);
	        
	        fm.getCurrentDocument().setFormSourceCode(fm.getCurrentDocument().getFormSourceCode());
	        
	        xx = fm.createForm("xx", null);
	        Page cp = xx.addConfirmationPage(null);
	        System.out.println("cp dzdz: "+xx.getConfirmationPage());
	        xx = ((FormManager)fm).openForm(xx.getXformsDocument());
	        System.out.println("cp dzdz2x: "+xx.getConfirmationPage());
	        
	        p = xx.getPage(xx.getContainedPagesIdList().get(0));
	        System.out.println("____________________________ av " +fm.getAvailableFormComponentsTypesList(new ConstComponentCategory(ConstComponentCategory.BASIC)));
	        Component select = p.addComponent(fm.getAvailableFormComponentsTypesList(new ConstComponentCategory(ConstComponentCategory.BASIC)).get(3), null);
	        System.out.println("sel: "+select.getHtmlRepresentation(new Locale("en")));
	        DOMUtil.prettyPrintDOM(select.getHtmlRepresentation(new Locale("en")));
	        select = p.addComponent(fm.getAvailableFormComponentsTypesList(new ConstComponentCategory(ConstComponentCategory.BASIC)).get(4), null);
	        System.out.println("sel: "+select.getHtmlRepresentation(new Locale("en")));
	        DOMUtil.prettyPrintDOM(select.getHtmlRepresentation(new Locale("en")));
	        select = p.addComponent(fm.getAvailableFormComponentsTypesList(new ConstComponentCategory(ConstComponentCategory.BASIC)).get(5), null);
	        System.out.println("sel: "+select.getHtmlRepresentation(new Locale("en")));
	        DOMUtil.prettyPrintDOM(select.getHtmlRepresentation(new Locale("en")));
	        select = p.addComponent(fm.getAvailableFormComponentsTypesList(new ConstComponentCategory(ConstComponentCategory.BASIC)).get(6), null);
	        System.out.println("sel: "+select.getHtmlRepresentation(new Locale("en")));
	        DOMUtil.prettyPrintDOM(select.getHtmlRepresentation(new Locale("en")));
	        
	        xx = ((FormManager)fm).openForm(xx.getXformsDocument());
	        p = xx.getPage(xx.getContainedPagesIdList().get(0));
	        
	        for (String contained : p.getContainedComponentsIdList()) {
				
	        	Component coco = p.getComponent(contained);
	        	
	        	if(!(coco instanceof Container)) {
	        		System.out.println("selxsxs: "+coco.getHtmlRepresentation(new Locale("en")));
	    	        DOMUtil.prettyPrintDOM(coco.getHtmlRepresentation(new Locale("en")));
	    	        LocalizedStringBean lab = coco.getProperties().getLabel();
	    	        lab.setString(new Locale("en"), "fuckinfuck");
	    	        coco.getProperties().setLabel(lab);
	    	        System.out.println("label: "+coco.getProperties().getLabel());
	        	}
			}
	        
	        List<String> new_order = new ArrayList<String>();
	        List<String> old_order = p.getContainedComponentsIdList();
	        
	        int i = 0;
	        
	        for (String contained : old_order) {
				
	        	if(i != 0) {
	        		new_order.add(p.getContainedComponentsIdList().get(i));
	        	}
	        	i++;
			}
	        new_order.add(p.getContainedComponentsIdList().get(0));
	        
	        p.rearrangeComponents();
	        
	        for (String contained : p.getContainedComponentsIdList()) {
				
	        	Component coco = p.getComponent(contained);
	        	
	        	if(!(coco instanceof Container)) {
	        		System.out.println("selxsxs: "+coco.getHtmlRepresentation(new Locale("en")));
	    	        DOMUtil.prettyPrintDOM(coco.getHtmlRepresentation(new Locale("en")));
	    	        LocalizedStringBean lab = coco.getProperties().getLabel();
	    	        lab.setString(new Locale("en"), "fuckinfuck");
	    	        coco.getProperties().setLabel(lab);
	    	        System.out.println("label: "+coco.getProperties().getLabel());
	        	}
			}
	        
	        Page cpp = xx.getConfirmationPage();
	        cpp.remove();
	        xx.addConfirmationPage(null);
	        
//	        sss-Thu_Mar_01_15_42_07_EET_2007.xhtml
	        System.out.println("__________________________________3 ___________________");
	        stream = new FileInputStream(new File("/Users/civilis/tmp/badform.xml"));
	        doc = FormManagerUtil.getDocumentBuilder().parse(stream);
	        stream.close();
	        xx = ((FormManager)fm).openForm(doc);
	        System.out.println("faaaaaaaaaaaaaaaaaa: "+xx.getThxPage());
	        
	        
	        Page conf_p = xx.addConfirmationPage(xx.getThxPage().getId());
	        conf_p.createButtonArea(null).addButton(new ConstButtonType(ConstButtonType.submit_form_button), null);
	        conf_p.getButtonArea().addButton(new ConstButtonType(ConstButtonType.next_page_button), null);
	        conf_p.getButtonArea().addButton(new ConstButtonType(ConstButtonType.previous_page_button), null);
	        System.out.println("faaaaaaaaaaaaaaaaaa xxxxxxxxxxx: "+xx.getThxPage());
	        
	        
			System.out.println("source code___________");
			System.out.println(xx.getFormSourceCode());
			
			System.out.println("the end");
			
		} catch (Exception e) {
			System.out.println("exception caught at main");
			e.printStackTrace();
		}
	}
}
