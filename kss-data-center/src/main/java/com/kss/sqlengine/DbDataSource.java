package com.kss.sqlengine;

public class DbDataSource extends ModelObject{
	
    /**
     * This field was generated by Abator for iBATIS.
     * This field corresponds to the database column DB_DATA_SOURCE.DATA_SOURCE_ID
     *
     * @abatorgenerated Thu Feb 02 10:33:09 CST 2012
     */
    private Integer dataSourceId;

    /**
     * This field was generated by Abator for iBATIS.
     * This field corresponds to the database column DB_DATA_SOURCE.DATA_SOURCE_NAME
     *
     * @abatorgenerated Thu Feb 02 10:33:09 CST 2012
     */
    private String dataSourceName;

    /**
     * This field was generated by Abator for iBATIS.
     * This field corresponds to the database column DB_DATA_SOURCE.DRIVER_CLASS_NAME
     *
     * @abatorgenerated Thu Feb 02 10:33:09 CST 2012
     */
    private String driverClassName;

    /**
     * This field was generated by Abator for iBATIS.
     * This field corresponds to the database column DB_DATA_SOURCE.URL
     *
     * @abatorgenerated Thu Feb 02 10:33:09 CST 2012
     */
    private String url;

    /**
     * This field was generated by Abator for iBATIS.
     * This field corresponds to the database column DB_DATA_SOURCE.USER_NAME
     *
     * @abatorgenerated Thu Feb 02 10:33:09 CST 2012
     */
    private String userName;

    /**
     * This field was generated by Abator for iBATIS.
     * This field corresponds to the database column DB_DATA_SOURCE.PASSWORD
     *
     * @abatorgenerated Thu Feb 02 10:33:09 CST 2012
     */
    private String password;
    
    
    private String dsEnvType;
    
    private Boolean isSupportJta;

    public Boolean getIsSupportJta() {
		return isSupportJta;
	}

	public void setIsSupportJta(Boolean isSupportJta) {
		this.isSupportJta = isSupportJta;
	}

	/**
     * This method was generated by Abator for iBATIS.
     * This method returns the value of the database column DB_DATA_SOURCE.DATA_SOURCE_ID
     *
     * @return the value of DB_DATA_SOURCE.DATA_SOURCE_ID
     *
     * @abatorgenerated Thu Feb 02 10:33:09 CST 2012
     */
    public Integer getDataSourceId() {
        return dataSourceId;
    }

    /**
     * This method was generated by Abator for iBATIS.
     * This method sets the value of the database column DB_DATA_SOURCE.DATA_SOURCE_ID
     *
     * @param dataSourceId the value for DB_DATA_SOURCE.DATA_SOURCE_ID
     *
     * @abatorgenerated Thu Feb 02 10:33:09 CST 2012
     */
    public void setDataSourceId(Integer dataSourceId) {
        this.dataSourceId = dataSourceId;
    }

    /**
     * This method was generated by Abator for iBATIS.
     * This method returns the value of the database column DB_DATA_SOURCE.DATA_SOURCE_NAME
     *
     * @return the value of DB_DATA_SOURCE.DATA_SOURCE_NAME
     *
     * @abatorgenerated Thu Feb 02 10:33:09 CST 2012
     */
    public String getDataSourceName() {
        return dataSourceName;
    }

    /**
     * This method was generated by Abator for iBATIS.
     * This method sets the value of the database column DB_DATA_SOURCE.DATA_SOURCE_NAME
     *
     * @param dataSourceName the value for DB_DATA_SOURCE.DATA_SOURCE_NAME
     *
     * @abatorgenerated Thu Feb 02 10:33:09 CST 2012
     */
    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    /**
     * This method was generated by Abator for iBATIS.
     * This method returns the value of the database column DB_DATA_SOURCE.DRIVER_CLASS_NAME
     *
     * @return the value of DB_DATA_SOURCE.DRIVER_CLASS_NAME
     *
     * @abatorgenerated Thu Feb 02 10:33:09 CST 2012
     */
    public String getDriverClassName() {
        return driverClassName;
    }

    /**
     * This method was generated by Abator for iBATIS.
     * This method sets the value of the database column DB_DATA_SOURCE.DRIVER_CLASS_NAME
     *
     * @param driverClassName the value for DB_DATA_SOURCE.DRIVER_CLASS_NAME
     *
     * @abatorgenerated Thu Feb 02 10:33:09 CST 2012
     */
    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    /**
     * This method was generated by Abator for iBATIS.
     * This method returns the value of the database column DB_DATA_SOURCE.URL
     *
     * @return the value of DB_DATA_SOURCE.URL
     *
     * @abatorgenerated Thu Feb 02 10:33:09 CST 2012
     */
    public String getUrl() {
        return url;
    }

    /**
     * This method was generated by Abator for iBATIS.
     * This method sets the value of the database column DB_DATA_SOURCE.URL
     *
     * @param url the value for DB_DATA_SOURCE.URL
     *
     * @abatorgenerated Thu Feb 02 10:33:09 CST 2012
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * This method was generated by Abator for iBATIS.
     * This method returns the value of the database column DB_DATA_SOURCE.USER_NAME
     *
     * @return the value of DB_DATA_SOURCE.USER_NAME
     *
     * @abatorgenerated Thu Feb 02 10:33:09 CST 2012
     */
    public String getUserName() {
        return userName;
    }

    /**
     * This method was generated by Abator for iBATIS.
     * This method sets the value of the database column DB_DATA_SOURCE.USER_NAME
     *
     * @param userName the value for DB_DATA_SOURCE.USER_NAME
     *
     * @abatorgenerated Thu Feb 02 10:33:09 CST 2012
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * This method was generated by Abator for iBATIS.
     * This method returns the value of the database column DB_DATA_SOURCE.PASSWORD
     *
     * @return the value of DB_DATA_SOURCE.PASSWORD
     *
     * @abatorgenerated Thu Feb 02 10:33:09 CST 2012
     */
    public String getPassword() {
        return password;
    }

    /**
     * This method was generated by Abator for iBATIS.
     * This method sets the value of the database column DB_DATA_SOURCE.PASSWORD
     *
     * @param password the value for DB_DATA_SOURCE.PASSWORD
     *
     * @abatorgenerated Thu Feb 02 10:33:09 CST 2012
     */
    public void setPassword(String password) {
        this.password = password;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((dataSourceId == null) ? 0 : dataSourceId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj){
			return true;
		}
		if (obj == null){
			return false;
		}
		if (getClass() != obj.getClass()){
			return false;
		}
		DbDataSource other = (DbDataSource) obj;
		if (dataSourceId == null) {
			if (other.dataSourceId != null){
				return false;
			}
		} else if (!dataSourceId.equals(other.dataSourceId)){
			return false;
		}
		return true;
	}

	public void setDsEnvType(String dsEnvType) {
		this.dsEnvType = dsEnvType;
	}

	public String getDsEnvType() {
		return dsEnvType;
	}
    
    
    
}