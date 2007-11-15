package com.idega.documentmanager.manager;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.2 $
 *
 * Last modified: $Date: 2007/11/15 09:24:15 $ by $Author: civilis $
 */
public class HtmlManagerFactory {

	private HtmlManager 		htmlManager;
	
	public HtmlManager getHtmlManager() {
		return htmlManager;
	}
	public void setHtmlManager(HtmlManager htmlManager) {
		this.htmlManager = htmlManager;
	}
}