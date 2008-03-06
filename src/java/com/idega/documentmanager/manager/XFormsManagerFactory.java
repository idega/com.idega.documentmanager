package com.idega.documentmanager.manager;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.2 $
 *
 * Last modified: $Date: 2008/03/06 12:11:38 $ by $Author: arunas $
 */
public class XFormsManagerFactory {

	private XFormsManager 				xformsManager;
	private XFormsManagerButton 		xformsManagerButton;
	private XFormsManagerContainer 		xformsManagerContainer;
	private XFormsManagerDocument		xformsManagerDocument;
	private XFormsManagerPage			xformsManagerPage;
	private XFormsManagerPlain 			xformsManagerPlain;
	private XFormsManagerSelect 		xformsManagerSelect;
	private XFormsManagerMultiUpload	xformsManagerMultiUpload;
	private XFormsManagerThankYouPage 	xformsManagerThankYouPage;
	
	
	public XFormsManagerFactory() { }
	
	public XFormsManager getXformsManager() {
		return xformsManager;
	}
	public void setXformsManager(XFormsManager xformsManager) {
		this.xformsManager = xformsManager;
	}
	public XFormsManagerButton getXformsManagerButton() {
		return xformsManagerButton;
	}
	public void setXformsManagerButton(XFormsManagerButton xformsManagerButton) {
		this.xformsManagerButton = xformsManagerButton;
	}
	public XFormsManagerContainer getXformsManagerContainer() {
		return xformsManagerContainer;
	}
	public void setXformsManagerContainer(
			XFormsManagerContainer xformsManagerContainer) {
		this.xformsManagerContainer = xformsManagerContainer;
	}
	public XFormsManagerDocument getXformsManagerDocument() {
		return xformsManagerDocument;
	}
	public void setXformsManagerDocument(XFormsManagerDocument xformsManagerDocument) {
		this.xformsManagerDocument = xformsManagerDocument;
	}
	public XFormsManagerPage getXformsManagerPage() {
		return xformsManagerPage;
	}
	public void setXformsManagerPage(XFormsManagerPage xformsManagerPage) {
		this.xformsManagerPage = xformsManagerPage;
	}
	public XFormsManagerPlain getXformsManagerPlain() {
		return xformsManagerPlain;
	}
	public void setXformsManagerPlain(XFormsManagerPlain xformsManagerPlain) {
		this.xformsManagerPlain = xformsManagerPlain;
	}
	public XFormsManagerSelect getXformsManagerSelect() {
		return xformsManagerSelect;
	}
	public XFormsManagerMultiUpload getXformsManagerMultiUpload() {
		return xformsManagerMultiUpload;
	}
	public void setXformsManagerSelect(XFormsManagerSelect xformsManagerSelect) {
		this.xformsManagerSelect = xformsManagerSelect;
	}
	public XFormsManagerThankYouPage getXformsManagerThankYouPage() {
		return xformsManagerThankYouPage;
	}
	public void setXformsManagerThankYouPage(
			XFormsManagerThankYouPage xformsManagerThankYouPage) {
		this.xformsManagerThankYouPage = xformsManagerThankYouPage;
	}

	public void setXformsManagerMultiUpload(
			XFormsManagerMultiUpload xformsManagerMultiUpload) {
		this.xformsManagerMultiUpload = xformsManagerMultiUpload;
	}
}