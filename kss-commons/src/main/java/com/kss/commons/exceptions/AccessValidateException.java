/**
 * 
 */
package com.kss.commons.exceptions;

/**
 * @author linrq
 *
 */
public class AccessValidateException extends Exception {
	private static final long serialVersionUID = 1788658201202015161L;
	
	public AccessValidateException(String message) {
		super(message);
	}
	public AccessValidateException(Throwable cause) {
		super(cause);
	}
	public AccessValidateException(String message, Throwable cause) {
		super(message, cause);
	}
}
