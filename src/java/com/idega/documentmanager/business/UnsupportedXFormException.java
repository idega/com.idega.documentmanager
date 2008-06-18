package com.idega.documentmanager.business;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.1 $
 *
 * Last modified: $Date: 2008/06/18 08:00:27 $ by $Author: civilis $
 */
public class UnsupportedXFormException extends IllegalStateException {

	private static final long serialVersionUID = 3311245306534653014L;

	public UnsupportedXFormException() { }

    public UnsupportedXFormException(String s) {
        super(s);
    }

    public UnsupportedXFormException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public UnsupportedXFormException(Throwable throwable) {
        super(throwable);
    }
}