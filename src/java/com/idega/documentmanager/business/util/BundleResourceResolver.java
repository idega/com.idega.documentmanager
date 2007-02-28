package com.idega.documentmanager.business.util;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import org.chiba.xml.xslt.impl.FileResource;
import org.chiba.xml.xslt.impl.Resource;
import org.chiba.xml.xslt.impl.ResourceResolver;
import com.idega.idegaweb.DefaultIWBundle;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWMainApplication;

public class BundleResourceResolver implements ResourceResolver {

	private String sBundlesDirectory;
	private IWMainApplication iwma;
	
	public BundleResourceResolver(IWMainApplication iwma) {
		this.iwma = iwma;
		this.sBundlesDirectory = System.getProperty(DefaultIWBundle.SYSTEM_BUNDLES_RESOURCE_DIR);
	}

	/*
	 * (non-Javadoc)
	 * @see org.chiba.xml.xslt.impl.ResourceResolver#resolve(java.net.URI)
	 */
	public Resource resolve(URI uri) throws IOException {
		if (!uri.getScheme().equals("bundle")) {
        	return null;
		}

		String bundleIdentifier = uri.getHost();
		String pathWithinBundle = uri.getPath();
		
		if (pathWithinBundle.startsWith("/")) {
			pathWithinBundle = pathWithinBundle.substring(1); // drop leading slash
		}

		Resource res = null;

		if (this.sBundlesDirectory != null) {
			String filePath = sBundlesDirectory + File.separator + bundleIdentifier + File.separator + pathWithinBundle;
			File file = new File(filePath);
			res = new FileResource(file);
		}
		else { 
			IWBundle bundle = iwma.getBundle(bundleIdentifier);
			res = new BundleResource(bundle, pathWithinBundle);
		}

		return res;
	}
	
	
}
