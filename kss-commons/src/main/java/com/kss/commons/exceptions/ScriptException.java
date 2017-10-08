/**
 * 
 */
package com.kss.commons.exceptions;

/**
 * @author linrq
 *
 */
public class ScriptException extends Exception {
	private static final long serialVersionUID = 1788658211202015161L;
	
	public ScriptException(String message) {
		super(message);
	}
	public ScriptException(Throwable cause) {
		super(cause);
	}
	public ScriptException(String message, Throwable cause) {
		super(message, cause);
	}
}
