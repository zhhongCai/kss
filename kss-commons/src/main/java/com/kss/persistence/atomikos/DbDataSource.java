package com.kss.persistence.atomikos;
import java.io.Serializable;

/**
 * <b>功能：</b>数据源<br>
 * <b>版本：</b>1.0；2011-03-30；创建
 */
public class DbDataSource implements Serializable{
	private static final long serialVersionUID=-6027997106003824666L;
	//private Integer dataSourceId;
	private String dataSourceName;
	private String driverClassName;
	private String url;
	private String userName;
	private String password;
	
	public String getDataSourceName(){
		return dataSourceName;
	}
	public void setDataSourceName(String dataSourceName){
		this.dataSourceName=dataSourceName;
	}
	public String getDriverClassName(){
		return driverClassName;
	}
	public void setDriverClassName(String driverClassName){
		this.driverClassName=driverClassName;
	}
	public String getUrl(){
		return url;
	}
	public void setUrl(String url){
		this.url=url;
	}
	public String getUserName(){
		return userName;
	}
	public void setUserName(String userName){
		this.userName=userName;
	}
	public String getPassword(){
		return password;
	}
	public void setPassword(String password){
		this.password=password;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((dataSourceName == null) ? 0 : dataSourceName.hashCode());
		result = prime * result
				+ ((driverClassName == null) ? 0 : driverClassName.hashCode());
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		result = prime * result
				+ ((userName == null) ? 0 : userName.hashCode());
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DbDataSource other = (DbDataSource) obj;
		if (dataSourceName == null) {
			if (other.dataSourceName != null)
				return false;
		} else if (!dataSourceName.equals(other.dataSourceName))
			return false;
		if (driverClassName == null) {
			if (other.driverClassName != null)
				return false;
		} else if (!driverClassName.equals(other.driverClassName))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}
	
	
}