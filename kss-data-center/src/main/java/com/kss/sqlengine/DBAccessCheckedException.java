/**
 * 
 */
package com.kss.sqlengine;


public class DBAccessCheckedException extends Exception {
	private static final long serialVersionUID = 1430486226377456945L;

	public DBAccessCheckedException(String message) {
        super(message);
    }

    public DBAccessCheckedException(String message, Throwable cause) {
        super(message, cause);
        
    }

    public DBAccessCheckedException(Throwable cause) {
        super("数据库访问异常！", cause);
        
    }
    
    
}
