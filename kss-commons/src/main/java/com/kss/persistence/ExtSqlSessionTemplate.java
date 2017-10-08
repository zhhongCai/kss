package com.kss.persistence;

import com.kss.persistence.routing.RoutingContext;
import com.kss.persistence.routing.RoutingSqlSessionFactoryBean;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.reflection.ExceptionUtil;
import org.apache.ibatis.session.*;
import org.mybatis.spring.MyBatisExceptionTranslator;
import org.mybatis.spring.SqlSessionUtils;
import org.springframework.dao.support.PersistenceExceptionTranslator;
import org.springframework.util.Assert;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 * <b>功能：</b>持久层数据库操作类<br>
 * <b>版本：</b>1.0；2011-03-14；创建
 */
public class ExtSqlSessionTemplate implements SqlSession{
	private final SqlSessionFactory sqlSessionFactory;
	private final ExecutorType executorType;
	private final SqlSession sqlSessionProxy;
	private final PersistenceExceptionTranslator exceptionTranslator;
	public ExtSqlSessionTemplate(SqlSessionFactory sqlSessionFactory){
		this(sqlSessionFactory,sqlSessionFactory.getConfiguration().getDefaultExecutorType());
	}
	public ExtSqlSessionTemplate(SqlSessionFactory sqlSessionFactory,ExecutorType executorType){
		this(sqlSessionFactory,executorType,new MyBatisExceptionTranslator(sqlSessionFactory.getConfiguration().getEnvironment().getDataSource(),true));
	}
	public ExtSqlSessionTemplate(SqlSessionFactory sqlSessionFactory,ExecutorType executorType,PersistenceExceptionTranslator exceptionTranslator){
		Assert.notNull(sqlSessionFactory,"Property 'sqlSessionFactory' is required");
		Assert.notNull(executorType,"Property 'executorType' is required");
		this.sqlSessionFactory=sqlSessionFactory;
		this.executorType=executorType;
		this.exceptionTranslator=exceptionTranslator;
		this.sqlSessionProxy=(SqlSession)Proxy.newProxyInstance(SqlSessionFactory.class.getClassLoader(),new Class[]{SqlSession.class},new SqlSessionInterceptor());
	}
	public SqlSessionFactory getSqlSessionFactory(){
		return this.sqlSessionFactory;
	}
	public ExecutorType getExecutorType(){
		return this.executorType;
	}
	public PersistenceExceptionTranslator getPersistenceExceptionTranslator(){
		return this.exceptionTranslator;
	}
	public Object selectOne(String statement){
		return this.sqlSessionProxy.selectOne(statement);
	}
	public Object selectOne(String statement,Object parameter){
		return this.sqlSessionProxy.selectOne(statement,parameter);
	}
	public Map<?,?> selectMap(String statement,String mapKey){
		return this.sqlSessionProxy.selectMap(statement,mapKey);
	}
	public Map<?,?> selectMap(String statement,Object parameter,String mapKey){
		return this.sqlSessionProxy.selectMap(statement,parameter,mapKey);
	}
	public Map<?,?> selectMap(String statement,Object parameter,String mapKey,RowBounds rowBounds){
		return this.sqlSessionProxy.selectMap(statement,parameter,mapKey,rowBounds);
	}
	public List<?> selectList(String statement){
		return this.sqlSessionProxy.selectList(statement);
	}
	public List<?> selectList(String statement,Object parameter){
		return this.sqlSessionProxy.selectList(statement,parameter);
	}
	public List<?> selectList(String statement,Object parameter,RowBounds rowBounds){
		return this.sqlSessionProxy.selectList(statement,parameter,rowBounds);
	}
	public void select(String statement,ResultHandler handler){
		this.sqlSessionProxy.select(statement,handler);
	}
	public void select(String statement,Object parameter,ResultHandler handler){
		this.sqlSessionProxy.select(statement,parameter,handler);
	}
	public void select(String statement,Object parameter,RowBounds rowBounds,ResultHandler handler){
		this.sqlSessionProxy.select(statement,parameter,rowBounds,handler);
	}
	public int insert(String statement){
		return this.sqlSessionProxy.insert(statement);
	}
	public int insert(String statement,Object parameter){
		return this.sqlSessionProxy.insert(statement,parameter);
	}
	public int update(String statement){
		return this.sqlSessionProxy.update(statement);
	}
	public int update(String statement,Object parameter){
		return this.sqlSessionProxy.update(statement,parameter);
	}
	public int delete(String statement){
		return this.sqlSessionProxy.delete(statement);
	}
	public int delete(String statement,Object parameter){
		return this.sqlSessionProxy.delete(statement,parameter);
	}
	public <T>T getMapper(Class<T> type){
		return this.getConfiguration().getMapper(type,this);
	}
	public void commit(){
		throw new UnsupportedOperationException("Manual commit is not allowed over a Spring managed SqlSession");
	}
	public void commit(boolean force){
		throw new UnsupportedOperationException("Manual commit is not allowed over a Spring managed SqlSession");
	}
	public void rollback(){
		throw new UnsupportedOperationException("Manual rollback is not allowed over a Spring managed SqlSession");
	}
	public void rollback(boolean force){
		throw new UnsupportedOperationException("Manual rollback is not allowed over a Spring managed SqlSession");
	}
	public void close(){
		throw new UnsupportedOperationException("Manual close is not allowed over a Spring managed SqlSession");
	}
	public void clearCache(){
		this.sqlSessionProxy.clearCache();
	}
	public Configuration getConfiguration(){
		return this.sqlSessionFactory.getConfiguration();
	}
	public Connection getConnection(){
		return this.sqlSessionProxy.getConnection();
	}
	public List<BatchResult> flushStatements(){
		return this.sqlSessionProxy.flushStatements();
	}
	private class SqlSessionInterceptor implements InvocationHandler{
		public Object invoke(Object proxy,Method method,Object[] args)throws Throwable{
			SqlSessionFactory sqlSessionFactory;
			if(RoutingSqlSessionFactoryBean.RESOLVED_SQLSESSIONFACTORYS.isEmpty()){
				sqlSessionFactory=ExtSqlSessionTemplate.this.sqlSessionFactory;
			}else{
				String statementId=(String)args[0];
				Object[] parameters=new Object[args.length-1];
				for(int i=1;i<args.length;i++)parameters[i-1]=args[i];
				RoutingSqlSessionFactoryBean.ROUTING_STRATEGY.registerCurrentDataSource(statementId,parameters);
				sqlSessionFactory=RoutingSqlSessionFactoryBean.RESOLVED_SQLSESSIONFACTORYS.get(RoutingContext.getName());
			}
			final SqlSession sqlSession=SqlSessionUtils.getSqlSession(sqlSessionFactory,ExtSqlSessionTemplate.this.executorType,ExtSqlSessionTemplate.this.exceptionTranslator);
			try{
				Object result=method.invoke(sqlSession,args);
				if(!SqlSessionUtils.isSqlSessionTransactional(sqlSession,sqlSessionFactory))sqlSession.commit();
				return result;
			}catch(Throwable t){
				Throwable unwrapped=ExceptionUtil.unwrapThrowable(t);
				if(ExtSqlSessionTemplate.this.exceptionTranslator!=null&&unwrapped instanceof PersistenceException){
					Throwable translated=ExtSqlSessionTemplate.this.exceptionTranslator.translateExceptionIfPossible((PersistenceException)unwrapped);
					if(translated!=null)unwrapped=translated;
				}
				throw unwrapped;
			}finally{
				SqlSessionUtils.closeSqlSession(sqlSession,sqlSessionFactory);
			}
		}
	}
}