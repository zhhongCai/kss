/**
 * 
 */
package com.kss.sqlengine;


import com.kss.commons.util.CommonUtil;

public class Result {
	public static final String SUCCESS = "0";
	public static final String FAILURE = "-1";
	
	private String resultCode;
	private String resultMsg;
	private String dbCode;
	private Object result;
	
	public Result(){
		super();
	}
	
	public Result(String resultCode, String resultMsg, String dbCode,
			Object result) {
		super();
		this.resultCode = resultCode;
		this.resultMsg = resultMsg;
		this.dbCode = dbCode;
		this.result = result;
	}
	
	public Result(String resultCode, String resultMsg) {
		super();
		this.resultCode = resultCode;
		this.resultMsg = resultMsg;
	}

	public Result(Throwable e){
		super();
		this.resultCode = FAILURE;
		this.resultMsg = CommonUtil.getExceptionDetail(e, -1);
	}

	public Result(String resultCode) {
		super();
		this.resultCode = resultCode;
		if(resultCode.equals(Result.SUCCESS)){
			this.resultMsg = "执行成功";
		}
	}



	/**
	 * @param resultCode the resultCode to set
	 */
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	/**
	 * @return the resultCode
	 */
	public String getResultCode() {
		return resultCode;
	}
	/**
	 * @param resultMsg the resultMsg to set
	 */
	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}
	/**
	 * @return the resultMsg
	 */
	public String getResultMsg() {
		return resultMsg;
	}
	/**
	 * @param dbCode the dbCode to set
	 */
	public void setDbCode(String dbCode) {
		this.dbCode = dbCode;
	}
	/**
	 * @return the dbCode
	 */
	public String getDbCode() {
		return dbCode;
	}
	/**
	 * @param result the result to set
	 */
	public void setResult(Object result) {
		this.result = result;
	}
	/**
	 * @return the result
	 */
	public Object getResult() {
		return result;
	}
}
