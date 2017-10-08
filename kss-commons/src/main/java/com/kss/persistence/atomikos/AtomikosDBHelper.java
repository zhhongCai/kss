package com.kss.persistence.atomikos;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.jdbc.AtomikosDataSourceBean;
import org.apache.ibatis.transaction.TransactionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AtomikosDBHelper implements IDistDBHelper{
	private static Logger logger = LoggerFactory.getLogger(AtomikosDBHelper.class);
	private Map<String,Map<DbDataSource,Object>> connCache=new ConcurrentHashMap<String,Map<DbDataSource,Object>>();
	private Map<String,Map<DbDataSource,Object>> dsCache=new ConcurrentHashMap<String,Map<DbDataSource,Object>>();
	private Map<String,UserTransaction> utxCache=new ConcurrentHashMap<String,UserTransaction>();
	/* 
	 * timeout以秒为单位
	 * @see com.al.persistence.atomikos.IDistDBHelper#newDistTransaction(java.util.List, int)
	 */
	public String newDistTransaction(List<DbDataSource> dsList,int timeout){
		String transId=null;
		try{
			UserTransaction utx=new UserTransactionImp();
			utx.setTransactionTimeout(timeout);
			transId=String.valueOf(utx.hashCode());
			utxCache.put(transId,utx);
						
			Map<DbDataSource,Object> dsMap=new HashMap<DbDataSource,Object>();			
			for(DbDataSource dds:dsList){
				AtomikosDataSourceBean ads=new AtomikosDataSourceBean();
				ads.setUniqueResourceName(dds.getDataSourceName() + "-" + transId);
				ads.setXaDataSourceClassName(dds.getDriverClassName());
				Properties properties=new Properties();
				properties.setProperty("user",dds.getUserName());
				properties.setProperty("password",dds.getPassword());
				properties.setProperty("URL",dds.getUrl());
				ads.setXaProperties(properties);
				dsMap.put(dds,ads);
			}
			dsCache.put(transId,dsMap);			
		}catch(Exception e){
			throw new TransactionException("新建事务时发生异常",e);
		}
		return transId;
	}
	
	public void beginTransaction(String transactionId){
		UserTransaction utx=utxCache.get(transactionId);
		if(utx == null)
			throw new TransactionException("事务尚未初始化！");
		try{
			utx.begin();
		}catch(Exception e){
			throw new TransactionException("开始事务时出现异常",e);
		}
	}
	
	public void commitTransaction(String transactionId){
		UserTransaction utx=utxCache.get(transactionId);
		if(utx == null)
			throw new TransactionException("事务尚未初始化！");
		
		if(logger.isDebugEnabled()){
			logger.debug("Before commit, transaction status code:{}",getTransactionStatus(utx));
		}
		if(getTransactionStatus(utx) != Status.STATUS_NO_TRANSACTION){
			try{
				utx.commit();
			}catch(Exception e){
				throw new TransactionException("提交事务时出现异常",e);
			}finally{						
				// 关闭与该事务有关的数据库连接
				if(getTransactionStatus(utx) == Status.STATUS_NO_TRANSACTION){
					closeConnection(transactionId);					
				}					
			}
		}else{
			// 事务已经不存在，则关闭与该事务有关的数据库连接
			closeConnection(transactionId);	
			throw new TransactionException("不存在对应的数据库事务，可能该事务已完成（即已经提交或回滚）！");
		}
		if(logger.isDebugEnabled()){
			logger.debug("After commit, transaction status code:{}",getTransactionStatus(utx));
		}		
	}
	
	public void commitAllTransaction(){
		for(UserTransaction utx:utxCache.values()){
			if(utx == null)
				throw new TransactionException("事务尚未初始化！");
			try{
				utx.commit();
			}catch(Exception e){
				throw new TransactionException("提交事务时出现异常",e);
			}
		}
		// 关闭所有数据库连接
		for(String transId:utxCache.keySet()){
			closeConnection(transId);
		}
	}
	public Connection getConnection(String transactionId,DbDataSource ds){
		Connection conn=null;
		System.out.println("ds hash:" + ds.hashCode());
		if(connCache.get(transactionId) != null
				&& ((Map<DbDataSource,Object>)connCache.get(transactionId))
						.get(ds) != null)
			conn=(Connection)((Map<DbDataSource,Object>)connCache
					.get(transactionId)).get(ds);
		else{
			Map<DbDataSource,Object> dsMap=dsCache.get(transactionId);
			if(dsMap == null)
				throw new TransactionException("事务尚未初始化！dsMap==null");
			AtomikosDataSourceBean xads=(AtomikosDataSourceBean)dsMap.get(ds);
			if(xads == null)
				throw new TransactionException("事务尚未初始化！xads==null");
			try{
				conn=xads.getConnection();
			}catch(SQLException e){
				e.printStackTrace();
//				throw new SQLException("获取数据库连接时出现异常");
			}
			if(conn != null){
				if(connCache.get(transactionId) != null)
					connCache.get(transactionId).put(ds,conn);
				else{
					Map<DbDataSource,Object> connMap=new HashMap<DbDataSource,Object>();
					connMap.put(ds,conn);
					connCache.put(transactionId,connMap);
				}
			}
		}
		return conn;
	}
	public Connection getConnection(DbDataSource ds){
		Connection conn=null;
		try{
			Class.forName(ds.getDriverClassName());
			conn=DriverManager.getConnection(ds.getUrl(),ds.getUserName(),ds.getPassword());
		}catch(ClassNotFoundException e){
			e.printStackTrace();
		}catch(SQLException e){
			e.printStackTrace();
		}
		return conn;
	}
	public void rollAllTransaction(){
		for(UserTransaction utx:utxCache.values()){
			if(utx == null)
				throw new TransactionException("事务尚未初始化！");
			try{
				utx.rollback();
			}catch(Exception e){
				throw new TransactionException("回滚事务时出现异常",e);
			}
		}
		for(String transId:utxCache.keySet()){//关闭所有数据库连接
			closeConnection(transId);
		}
	}
	public void rollbackTransaction(String transactionId){
		UserTransaction utx=utxCache.get(transactionId);
		if(utx == null)
			throw new TransactionException("事务尚未初始化！");
		
		if(logger.isDebugEnabled()){
			logger.debug("Before rollback, transaction status code:{}",getTransactionStatus(utx));
		}			
		if(getTransactionStatus(utx) != Status.STATUS_NO_TRANSACTION){
			try{
				utx.rollback();
			}catch(Exception e){
				throw new TransactionException("回滚事务时出现异常",e);
			}finally{						
				// 关闭与该事务有关的数据库连接
				if(getTransactionStatus(utx) == Status.STATUS_NO_TRANSACTION){
					closeConnection(transactionId);					
				}					
			}
		}else{
			// 事务已经不存在，则关闭与该事务有关的数据库连接
			closeConnection(transactionId);	
			throw new TransactionException("不存在对应的数据库事务，可能该事务已完成（即已经提交或回滚）！");
		}
		if(logger.isDebugEnabled()){
			logger.debug("After rollback, transaction status code:{}",getTransactionStatus(utx));
		}		
	}
		
	private void closeConnection(String transactionId){
		try{
			Map<DbDataSource,Object> connMap=connCache.get(transactionId);
			for(Object conn:connMap.values()){
				((Connection)conn).close();
			}
			connCache.remove(transactionId);
			
			Map<DbDataSource,Object>  dsMap= dsCache.get(transactionId);
			dsMap.clear();
			dsCache.remove(transactionId);
		}catch(SQLException e){
			throw new TransactionException("关闭数据库连接出现异常",e);
		}
	}
	
	public void deleteDistTransaction(String transactionId){
		UserTransaction utx=utxCache.get(transactionId);
		if(utx == null)
			throw new TransactionException("事务尚未初始化！");
		dsCache.remove(transactionId);
		utxCache.remove(transactionId);
		utx=null;
	}
	
	public void deleteAllDistTransaction(){
		utxCache.clear();
	}
	
	public int getTransactionStatus(String transactionId){
		UserTransaction utx=utxCache.get(transactionId);
		return getTransactionStatus(utx);
	}
	
	private int getTransactionStatus(UserTransaction utx){
		if(utx == null){
			throw new TransactionException("事务尚未初始化！");
		}
		try {
			return utx.getStatus();			
		} catch (SystemException e) {
			logger.error("获取事务状态失败！", e);
			throw new TransactionException("获取事务状态失败！", e);
		}
	}
}