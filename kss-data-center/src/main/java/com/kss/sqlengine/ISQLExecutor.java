/**
 * 
 */
package com.kss.sqlengine;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;


public interface ISQLExecutor {
	/**
	 * 执行查询，完毕后关闭conn
	 * @param conn
	 * @param sql
	 * @param params
	 * @return 对结果集转换成JSON返回
	 */
	List<Map<String,Object>> executeQuery(Connection conn, String sql,
										  List<SqlParam> params) throws SQLException, ParseException;
	
	/**
	 * @param conn
	 * @param sql
	 * @param params
	 * @param closeAfterFinish
	 * @return
	 * @throws Exception
	 */
	List<Map<String,Object>> executeQuery(Connection conn, String sql,
										  List<SqlParam> params, boolean closeAfterFinish) throws SQLException, ParseException;
	
	/**
	 * 执行update，完毕后关闭conn
	 * @param conn
	 * @param sql
	 * @param params
	 * @return
	 */
	void executeUpdate(Connection conn, String sql, List<SqlParam> params) throws SQLException, ParseException;
	
	/**
	 * 执行update，完毕后并根据closeAfterFinish标志决定是否关闭conn
	 * @param conn
	 * @param sql
	 * @param params
	 * @param closeAfterFinish
	 * @return
	 */
	void executeUpdate(Connection conn, String sql, List<SqlParam> params,
					   boolean closeAfterFinish) throws SQLException, ParseException;
	
	/**
	 * 执行sql语句, 完毕后并根据closeAfterFinish标志决定是否关闭conn.
	 * @param conn
	 * @param sql
	 * @throws Exception
	 * @author caizh
	 * @version 2012-08-14
	 */
	void execute(Connection conn, String sql, boolean closeAfterFinish) throws Exception;
	
	/**
	 * 执行批量插入，完毕后并根据closeAfterFinish标志决定是否关闭conn.
	 * @param conn
	 * @param sql
	 * @param data
	 * @param closeAfterFinish
	 * @throws Exception
	 */
	void executeBatchInsert(Connection conn, String sql, List<List<Object>> data,
							boolean closeAfterFinish) throws Exception;
}
