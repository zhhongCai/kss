package com.kss.persistence;

import com.kss.persistence.routing.RoutingSqlSessionFactoryBean;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.util.Assert;

/**
 * <b>功能：</b>持久层映射实体类<br>
 * <b>版本：</b>1.0；2011-03-14；创建
 */
public class ExtMapperFactoryBean<T> extends ExtSqlSessionDaoSupport implements FactoryBean<T> {
	private Class<T> mapperInterface;
	private boolean addToConfig=true;
	public void setMapperInterface(Class<T> mapperInterface){
		this.mapperInterface=mapperInterface;
	}
	public void setAddToConfig(boolean addToConfig){
		this.addToConfig=addToConfig;
	}
	@Override
	protected void checkDaoConfig(){
		super.checkDaoConfig();
		Assert.notNull(this.mapperInterface,"Property 'mapperInterface' is required");
		if(RoutingSqlSessionFactoryBean.RESOLVED_SQLSESSIONFACTORYS.isEmpty()){//判断是否为JTA多数据源路由
			this.addMapper(super.getSqlSession().getConfiguration());
		}else{
			for(SqlSessionFactory sessionFactory:RoutingSqlSessionFactoryBean.RESOLVED_SQLSESSIONFACTORYS.values()){
				this.addMapper(sessionFactory.getConfiguration());
			}
		}
	}
	public T getObject()throws Exception{
		return super.getSqlSession().getMapper(this.mapperInterface);
	}
	public Class<T> getObjectType(){
		return this.mapperInterface;
	}
	public boolean isSingleton(){
		return true;
	}
	private void addMapper(Configuration configuration){
		if(this.addToConfig&&!configuration.hasMapper(this.mapperInterface)){
			try{
				configuration.addMapper(this.mapperInterface);
			}catch(Throwable t){
				logger.error("Error while adding the mapper '"+this.mapperInterface+"' to configuration.",t);
				throw new IllegalArgumentException(t);
			}finally{
				ErrorContext.instance().reset();
			}
		}
	}
}