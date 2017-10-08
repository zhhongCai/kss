/**
 * 
 */
package com.kss.commons.exceptions;


/**
 * @author linrq
 *
 */
public class TransactionException extends RuntimeException {
	private static final long serialVersionUID = 4244457701791667316L;
	
	public TransactionException(String message) {
		super(message);
	}
	public TransactionException(Throwable cause) {
		super(cause);
	}
	public TransactionException(String message, Throwable cause) {
		super(message, cause);
	}
    
}
