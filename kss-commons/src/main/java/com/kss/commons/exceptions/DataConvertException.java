/**
 * 
 */
package com.kss.commons.exceptions;

import org.springframework.core.NestedRuntimeException;

/**
 * @author linrq
 *
 */
public class DataConvertException extends NestedRuntimeException {
	private static final long serialVersionUID = 7881021415199931654L;

	public DataConvertException(String msg) {
		super(msg);		
	}	
	
	public DataConvertException(Throwable e) {
		super("Data Convert Error!", e);		
	}	
	
	public DataConvertException(String msg, Throwable e) {
		super(msg, e);		
	}
	
}
