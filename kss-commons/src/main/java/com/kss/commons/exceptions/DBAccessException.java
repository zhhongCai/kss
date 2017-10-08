/**
 * 
 */
package com.kss.commons.exceptions;

import java.sql.SQLException;

/**
 * @author linrq
 *
 */
public class DBAccessException extends RuntimeException {
	private static final long serialVersionUID = 4244337701781667316L;
	private int sqlErrCode;
	private String sqlState;

	public DBAccessException(String message) {
        super(message);
    }

    public DBAccessException(String message, Throwable cause) {
        super(message, cause);
        init(cause);
    }

    public DBAccessException(Throwable cause) {
        super("数据库访问异常！", cause);
        init(cause);
    }
    
    private void init(Throwable cause) {
    	if(cause instanceof SQLException){
        	SQLException ex = (SQLException)cause;
        	this.setSqlErrCode(ex.getErrorCode());
        	this.setSqlState(ex.getSQLState());
        }
    }

	/**
	 * @param sqlErrCode the sqlErrCode to set
	 */
	public void setSqlErrCode(int sqlErrCode) {
		this.sqlErrCode = sqlErrCode;
	}

	/**
	 * @return the sqlErrCode
	 */
	public int getSqlErrCode() {
		return sqlErrCode;
	}

	/**
	 * @param sqlState the sqlState to set
	 */
	public void setSqlState(String sqlState) {
		this.sqlState = sqlState;
	}

	/**
	 * @return the sqlState
	 */
	public String getSqlState() {
		return sqlState;
	}
}
