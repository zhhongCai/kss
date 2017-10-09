/**
 * 
 */
package com.kss.sqlengine;

import com.kss.commons.util.CommonUtil;
import com.kss.commons.util.EncryptDecryptUtil;
import com.kss.persistence.atomikos.AtomikosDBHelper;
import com.kss.persistence.atomikos.IDistDBHelper;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.util.*;

@Component
public class SQLEngine implements ISQLEngine {
	//缺省参数前后缀
	public static final String DEFAULT_PARAM_PREFIX_SUFFIX = "#";
	//如一个参数名前加了$，则说明该参数需要直接替换。
	private static final String PREFIX_OF_PARAM_TO_BE_REPLACED = "$";
	private static final String REGX_PREFIX_OF_PARAM_TO_BE_REPLACED = "\\$";
	private static ISQLEngine defaultSqlEngine;
	private static Logger log = LoggerFactory.getLogger(SQLEngine.class);
	
	//允许注入参数前后缀
	private String paramPrefixSuffix = DEFAULT_PARAM_PREFIX_SUFFIX;

	@Autowired
	private ISQLExecutor sqlExecutor;
	@Autowired
	private IDistDBHelper dbHelper;
	
	public static ISQLEngine getSQLEngineInstance(){
		if(defaultSqlEngine == null){
			defaultSqlEngine = new SQLEngine();
		}
		return defaultSqlEngine;
	}
	
	/* (non-Javadoc)
	 * @see com.al.crm.bmm.sqlengine.ISQLEngine#beginTransaction(java.util.List, int)
	 */
	public String beginTransaction(List<DbDataSource> dsList, int timout) {
		String transId = getDbHelper().newDistTransaction(convertDataSourceList(dsList), timout);
		getDbHelper().beginTransaction(transId);
		
		return transId;
	}
	
	private List<com.kss.persistence.atomikos.DbDataSource> convertDataSourceList(List<DbDataSource> dsList){
		List<com.kss.persistence.atomikos.DbDataSource> ddsList =
				new ArrayList<com.kss.persistence.atomikos.DbDataSource>();
		for(DbDataSource ds : dsList){
			ddsList.add(convertDataSource(ds));
		}
		
		return ddsList;
	}
	
	private com.kss.persistence.atomikos.DbDataSource convertDataSource(DbDataSource ds){
		com.kss.persistence.atomikos.DbDataSource dds = new com.kss.persistence.atomikos.DbDataSource();
		dds.setDataSourceName(ds.getDataSourceName());
		dds.setDriverClassName(ds.getDriverClassName());
		dds.setUrl(ds.getUrl());		
		dds.setUserName(ds.getUserName());
//		dds.setIsSupportJta(ds.getIsSupportJta());
		//如果长度超过50（目前数据库加密后都是超过50的），则认为该密码是加密过
		if(ds.getPassword().length() > 50){
			dds.setPassword(EncryptDecryptUtil.decryptPwd(ds.getPassword()));
		}else{
			dds.setPassword(ds.getPassword());
		}
		
		return dds;
	}
	
	/* (non-Javadoc)
	 * @see com.al.crm.bmm.sqlengine.ISQLEngine#executeQuery(java.lang.String, 
	 * java.util.List, com.al.crm.bmm.model.DbDataSource)
	 */
	public Result executeQuery(String sql, List<SqlParam> params,  DbDataSource ds) throws DBAccessCheckedException{
		return executeQuery(null, sql, params, ds);
	}


	/* (non-Javadoc)
	 * 返回的Result对象中result属性值是个JSON串。
	 * @see com.al.crm.bmm.sqlengine.ISQLEngine#executeQuery(java.lang.String, 
	 * java.util.Map, com.al.crm.bmm.model.DbDataSource)
	 */
	public Result executeQuery(String transId, String sql, List<SqlParam> params,
							   DbDataSource ds) throws DBAccessCheckedException{
		Result result = null;
		try {
			Connection conn = null;
			if(transId == null){
				conn = getDbHelper().getConnection(convertDataSource(ds));
			}else{
				conn = getDbHelper().getConnection(transId, convertDataSource(ds));
			}
			
			//SQL中参数替换处理
			String newSql = replaceParamInSql(sql, params);
			
			//SQL中参数解析成?
			List<SqlParam> realParams = new ArrayList<SqlParam>();
			newSql = convertSql(newSql, params, realParams);	
			result = new Result(Result.SUCCESS);
			
			// 如果在一个事务中，则不关闭连接
			if(transId == null){
				result.setResult(getSqlExecutor().executeQuery(conn, newSql, 
						realParams.size() > 0 ? realParams : params));
			}else{
				result.setResult(getSqlExecutor().executeQuery(conn, newSql, 
						realParams.size() > 0 ? realParams : params, false));
			}
		} catch (Exception e) {		
			//result = new Result(Result.FAILURE);
			//result.setResultMsg(CommonUtil.getExceptionDetail(e, 2000));
			throw new DBAccessCheckedException(e);
		}
		
		return result;
	}
	
	public Result executeUpdate(String sql, List<SqlParam> params, DbDataSource ds) throws DBAccessCheckedException{
		return executeUpdate(null, sql, params, ds);
	}
	
	public Result executeUpdate(String sql, List<SqlParam> params, DbDataSource ds,
			boolean closeConnAfterFinish)throws DBAccessCheckedException{
		return executeUpdate(null, sql, params, ds, closeConnAfterFinish);
	}

	public Result executeUpdate(String transId, String sql,
			List<SqlParam> params, DbDataSource ds) throws DBAccessCheckedException{
		// 默认不关闭连接
		return executeUpdate(transId, sql, params, ds, false);
	}
	
	private Result executeUpdate(String transId, String sql,
			List<SqlParam> params, DbDataSource ds, boolean closeConnAfterFinish) throws DBAccessCheckedException{
		Result result = null;
		try {
			Connection conn = transId == null ? 
					getDbHelper().getConnection(convertDataSource(ds)) : 
						getDbHelper().getConnection(transId, convertDataSource(ds));
			List<SqlParam> realParams = new ArrayList<SqlParam>();
			sql = convertSql(sql, params, realParams);
			getSqlExecutor().executeUpdate(conn, sql, realParams, closeConnAfterFinish);
			
			result = new Result(Result.SUCCESS);
			result.setResult(getUpdateResult(params));
		}catch (Exception e) {
			log.error("执行失败 {}", e);
			result = new Result(Result.FAILURE);
			result.setResultMsg(CommonUtil.getExceptionDetail(e, 2000));
			throw new DBAccessCheckedException(e);
		}
		
		return result;
	}
	

	/* (non-Javadoc)
	 * @see com.al.crm.bmm.sqlengine.ISQLEngine#rollback(java.lang.String)
	 */
	public void rollback(String transId) {
		//回滚并关闭有关数据库连接
		getDbHelper().rollbackTransaction(transId);
	}
	
	/* (non-Javadoc)
	 * @see com.al.crm.bmm.sqlengine.ISQLEngine#commit(java.lang.String)
	 */
	public void commit(String transId) {
		//提交事务，并关闭有关数据库连接
		getDbHelper().commitTransaction(transId);
	}

	/**
	 * 获取update操作脚本中返回的参数
	 * @param params
	 * @return
	 */
	private Map<String, String> getUpdateResult(List<SqlParam> params){
		Map<String, String> result = new HashMap<String, String>();
		if(CollectionUtils.isEmpty(params)) {
			return result;
		}
		for(SqlParam param : params){
			if("out".equalsIgnoreCase(param.getInOut())){
				result.put(param.getParamName(), param.getParamValue());
			}
		}
		
		return result;		
	}

	public ISQLExecutor getSqlExecutor() {
		if(this.sqlExecutor == null){
			this.sqlExecutor = new SQLExecutor();
		}
		return this.sqlExecutor;
	}


	public void setSqlExecutor(ISQLExecutor sqlExecutor) {
		this.sqlExecutor = sqlExecutor;
	}
	
	public IDistDBHelper getDbHelper() {
		if(this.dbHelper == null){
			this.dbHelper = new AtomikosDBHelper();
		}
		return this.dbHelper;
	}


	public void setDbHelper(IDistDBHelper dbHelper) {
		this.dbHelper = dbHelper;
	}


	public void setParamPrefixSuffix(String paramPrefixSuffix) {
		this.paramPrefixSuffix = paramPrefixSuffix;
	}


	public String getParamPrefixSuffix() {
		return paramPrefixSuffix;
	}
	
	/**
	 * 对SQL进行转换：将带有前后缀的参数名替换成?
	 * @param sql
	 * @param paramList
	 * @param realParamList
	 * @return
	 */
	private String convertSql(String sql, List<SqlParam> paramList, List<SqlParam> realParamList)
		throws DBAccessCheckedException{
		if(paramList == null || paramList.size() == 0){
			return sql;
		}
		
		//删除掉传入NULL的可选参数所在语句的行
		sql = deleteNullParamClauseInSQL(sql, paramList);
		
		String prefixSuffix = this.paramPrefixSuffix == null 
				? DEFAULT_PARAM_PREFIX_SUFFIX : this.paramPrefixSuffix;
		int beginIndex = sql.indexOf(prefixSuffix, 0);
		int endIndex = 0;
		String paramName;
		SqlParam param;
				
		while(beginIndex >0){
			endIndex = sql.indexOf(prefixSuffix, beginIndex+1);
			paramName = sql.substring(beginIndex+1, endIndex);
			param = findSqlParamByParamName(paramName, paramList);
			if(param != null){				
				realParamList.add(param);
			}else{
				throw new DBAccessCheckedException("SQL中的参数【" + paramName  + "】未找到对应的输入参数！");
			}
			sql = sql.substring(0, beginIndex) + "?" + sql.substring(endIndex+1);
			
			beginIndex = sql.indexOf(prefixSuffix, 0);
		}
				
		log.debug("sql: {}", sql);
		log.debug("paramList size: {}", realParamList.size());
		
		return sql;
	}
	
	/**
	 * 删除掉传入NULL的可选参数所在语句的行。
	 * @param sql
	 * @param paramList
	 * @return
	 */
	private String deleteNullParamClauseInSQL(String sql, List<SqlParam> paramList){
		String[] sqlArr = sql.split("\\n");
		for(int i=0; i<sqlArr.length; i++){
			for(SqlParam sqlParam : paramList){
				if(!sqlParam.isRequired() && (sqlParam.getParamValue() == null) || "".equals(sqlParam.getParamValue().trim()) ){
					if(sqlArr[i].contains(paramPrefixSuffix + sqlParam.getParamName().trim() + paramPrefixSuffix)){
						log.debug("Clause[{}] will be delete, because param value is null. param name={}.", sqlArr[i], 
								sqlParam.getParamName());						
						sqlArr[i] = null;
					}
				}
			}
		}
		
		StringBuilder newSql = new StringBuilder();
		for(String clause : sqlArr){
			if(clause != null){
				newSql.append(clause);
				newSql.append("\n");
			}
		}
		
		log.debug("deleteNullParamClauseInSQL return new sql:{}", newSql);
		return newSql.toString();
	}
	
	private String replaceParamInSql(String sql, List<SqlParam> paramList) {
		String newSql = sql;
		if(newSql.indexOf(paramPrefixSuffix + PREFIX_OF_PARAM_TO_BE_REPLACED) > 0){
			List<SqlParam> removeList = new LinkedList<SqlParam>();
			for(SqlParam sqlParam : paramList){				
				String replaceStr = paramPrefixSuffix + REGX_PREFIX_OF_PARAM_TO_BE_REPLACED 
					+ sqlParam.getParamName().trim() + paramPrefixSuffix;				
				String replaceSql = newSql.replaceAll(replaceStr, sqlParam.getParamValue());
				if(!newSql.equals(replaceSql)){
					newSql = replaceSql;
					removeList.add(sqlParam);
				}				
			}
			paramList.removeAll(removeList);
		}
		
		log.debug("sql after replace: {}", newSql);
		return newSql;
	}
	
	private SqlParam findSqlParamByParamName(String paramName, List<SqlParam> paramList){
		for(SqlParam param : paramList){
			if(param.getParamName().equalsIgnoreCase(paramName)){
				return param;
			}
		}
		
		return null;
	}
	
	public int getTransactionStatus(String transactionId){
		return getDbHelper().getTransactionStatus(transactionId);
	}

	public Result execute(String sql, DbDataSource ds, boolean closeConnAfterFinish)
            throws DBAccessCheckedException{
		Result result = null;
		try {
			Connection conn = getDbHelper().getConnection(convertDataSource(ds));
//			List<SqlParam> realParams = new ArrayList<SqlParam>();
			getSqlExecutor().execute(conn, sql, closeConnAfterFinish);
			result = new Result(Result.SUCCESS);
//			result.setResult(getUpdateResult(params));
		}catch (Exception e) {
			log.error("执行失败 {}", e);
			//result = new Result(Result.FAILURE);
			//result.setResultMsg(CommonUtil.getExceptionDetail(e, 2000));
			throw new DBAccessCheckedException(e.getMessage(), e);
		}
		
		return result;
    }

	public Result executeBatchInsert(String sql, DbDataSource ds,List<List<Object>> data,
            boolean closeConnAfterFinish) throws DBAccessCheckedException{
		Result result = null;
		try {
			Connection conn = getDbHelper().getConnection(convertDataSource(ds));
//			List<SqlParam> realParams = new ArrayList<SqlParam>();
			getSqlExecutor().executeBatchInsert(conn, sql, data, closeConnAfterFinish);
			result = new Result(Result.SUCCESS);
//			result.setResult(getUpdateResult(params));
		}catch (Exception e) {
			log.error("执行失败 {}", e);
			//result = new Result(Result.FAILURE);
			//result.setResultMsg(CommonUtil.getExceptionDetail(e, 2000));
			throw new DBAccessCheckedException(e.getMessage(), e);
		}
		
		return result;
    }
}
