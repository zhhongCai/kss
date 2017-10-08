package com.kss.persistence.atomikos;
import java.sql.Connection;
import java.util.List;

public interface IDistDBHelper{
	/**
	 * 开始一个分布式事务，返回一个自定义的事务ID
	 * 
	 * @param dsList
	 * @param timout
	 * @return
	 */
	public String newDistTransaction(List<DbDataSource> dsList, int timout);
	/**
	 * 删除一个分布式事务。 如果一个分布式事务不再使用，则最后要删除，避免程序出现异常。
	 * @param transactionId
	 */
	public void deleteDistTransaction(String transactionId);
	/**
	 * 删除所有的分布式事务。
	 */
	public void deleteAllDistTransaction();
	/**
	 * 开始事务
	 * @param transactionId
	 */
	public void beginTransaction(String transactionId);
	/**
	 * 提交事务，并关闭相关的数据库连接
	 * @param transactionId
	 */
	public void commitTransaction(String transactionId);
	/**
	 * 回滚事务，并关闭相关数据库连接
	 * @param transactionId
	 */
	public void rollbackTransaction(String transactionId);
	/**
	 * 提交所有的事务，并关闭所有数据库连接
	 */
	public void commitAllTransaction();
	/**
	 * 回滚所有的事务,并关闭所有数据库连接
	 */
	public void rollAllTransaction();
	/**
	 * 根据transactionId及一个数据源配置，获取一个有分布式事务管理的数据库连接
	 * @param transactionId
	 * @param ds
	 * @return
	 */
	public Connection getConnection(String transactionId, DbDataSource ds);
	/**
	 * 根据数据源配置，获取一个数据库连接，无事务管理
	 * @param ds
	 * @return
	 */
	public Connection getConnection(DbDataSource ds);
	
	/**
	 * 获取事务状态
	 * @param transactionId
	 * @return
	 */
	public int getTransactionStatus(String transactionId);
}