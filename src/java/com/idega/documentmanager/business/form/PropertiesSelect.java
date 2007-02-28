package com.idega.documentmanager.business.form;

import com.idega.documentmanager.business.form.beans.ILocalizedItemset;
import com.idega.documentmanager.business.form.beans.LocalizedStringBean;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version 1.0
 * 
 * <i><b>WARNING: </b></i>for changes to take effect, u need to use setter methods for every property change
 */
public interface PropertiesSelect extends PropertiesComponent {
	
	public static final int LOCAL_DATA_SRC = 1;
	public static final int EXTERNAL_DATA_SRC = 2;
	
	public abstract ILocalizedItemset getItemset();

	public abstract LocalizedStringBean getEmptyElementLabel();

	public abstract void setEmptyElementLabel(LocalizedStringBean empty_element_label);

	public abstract String getExternalDataSrc();

	public abstract void setExternalDataSrc(String external_data_src);

	public abstract Integer getDataSrcUsed();

	public abstract void setDataSrcUsed(Integer data_src_used);
}