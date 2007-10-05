package com.idega.documentmanager.manager;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.1 $
 *
 * Last modified: $Date: 2007/10/05 11:42:35 $ by $Author: civilis $
 */
public class HtmlManagerFactory {

	private HtmlManager 		htmlManager;
	private HtmlManagerButton	htmlManagerButton;
	
	public HtmlManager getHtmlManager() {
		return htmlManager;
	}
	public void setHtmlManager(HtmlManager htmlManager) {
		this.htmlManager = htmlManager;
	}
	public HtmlManagerButton getHtmlManagerButton() {
		return htmlManagerButton;
	}
	public void setHtmlManagerButton(HtmlManagerButton htmlManagerButton) {
		this.htmlManagerButton = htmlManagerButton;
	}
}