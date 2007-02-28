package com.idega.documentmanager.business.form.beans;

import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version 1.0
 * 
 */
public interface ILocalizedItemset {

	public abstract Set<Locale> getItemsetKeySet();

	public abstract void clear();

	public abstract List<ItemBean> getItems(Locale locale);

	public abstract void setItems(Locale locale, List<ItemBean> items);

}