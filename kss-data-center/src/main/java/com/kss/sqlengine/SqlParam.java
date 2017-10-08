/**
 * 
 */
package com.kss.sqlengine;

public class SqlParam {
	private String paramName;
	private String inOut;
	private String paramValue;
	private String dataType;
	private boolean isRequired = true;
	
	public void setParamName(String paramName) {
		this.paramName = paramName;
	}
	public String getParamName() {
		return paramName;
	}
	public void setInOut(String inOut) {
		this.inOut = inOut;
	}
	public String getInOut() {
		return inOut;
	}
	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}
	public String getParamValue() {
		return paramValue;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getDataType() {
		return dataType;
	}
	public void setRequired(boolean isRequired) {
		this.isRequired = isRequired;
	}
	public boolean isRequired() {
		return isRequired;
	}
}
