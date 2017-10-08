/**
 * 
 */
package com.kss.sqlengine;



import java.util.List;


/**
 * SQL执行引擎
 *
 */

public interface ISQLEngine {
	/**
	 * 执行查询类SQL
	 * @param sql
	 * @param params
	 * @param ds
	 * @return
	 */
	Result executeQuery(String sql, List<SqlParam> params, DbDataSource ds)throws DBAccessCheckedException;
	
	/**
	 * @param transId
	 * @param sql
	 * @param params
	 * @param ds
	 * @return
	 * @throws DBAccessCheckedException
	 */
	Result executeQuery(String transId, String sql, List<SqlParam> params,
						DbDataSource ds)throws DBAccessCheckedException;
	
	/**
	 * 执行更新类SQL，自动事务
	 * @param sql
	 * @param params
	 * @param ds
	 * @return
	 */
	Result executeUpdate(String sql, List<SqlParam> params, DbDataSource ds)throws DBAccessCheckedException;
	
	/**
	 * @param sql
	 * @param params
	 * @param ds
	 * @param closeConnAfterFinish
	 * @return
	 * @throws DBAccessCheckedException
	 */
	Result executeUpdate(String sql, List<SqlParam> params, DbDataSource ds,
						 boolean closeConnAfterFinish)throws DBAccessCheckedException;
	
	/**
	 * 执行更新类SQL，人工控制事务
	 * @param transId
	 * @param sql
	 * @param params
	 * @param ds
	 * @return
	 */
	Result executeUpdate(String transId, String sql, List<SqlParam> params,
						 DbDataSource ds)throws DBAccessCheckedException;
	
	/**
	 * 执行sql语句（建表）.
	 * @param sql
	 * @param ds
	 * @param closeConnAfterFinish
	 * @return
	 * @throws DBAccessCheckedException
	 * @author caizh
	 * @version 2012-08-14
	 */
	Result execute(String sql, DbDataSource ds, boolean closeConnAfterFinish) throws DBAccessCheckedException;
	
	/**
	 * 执行批量插入
	 * @param sql
	 * @param ds
	 * @param data
	 * @param closeConnAfterFinish
	 * @return
	 * @throws DBAccessCheckedException
	 */
	Result executeBatchInsert(String sql, DbDataSource ds,
							  List<List<Object>> data, boolean closeConnAfterFinish) throws DBAccessCheckedException;
	
	/**
	 * 开始事务
	 * @param dsList
	 * @param timout
	 * @return 返回事务ID
	 */
	String beginTransaction(List<DbDataSource> dsList, int timout);
	
	/**
	 * 回滚事务
	 * @param transId
	 */
	void rollback(String transId);
	
	/**
	 * 提交事务
	 * @param transId
	 */
	void commit(String transId);
	
	/**
	 * 获取事务状态
	 * @param transactionId
	 * @return
	 */
	public int getTransactionStatus(String transactionId);
	
}
