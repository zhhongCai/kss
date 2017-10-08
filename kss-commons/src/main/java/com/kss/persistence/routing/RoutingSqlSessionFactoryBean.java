package com.kss.persistence.routing;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.type.TypeHandler;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * <b>功能：</b>IBatis会话工厂路由器<br>
 * <b>版本：</b>1.0；2011-02-28；创建
 */
public class RoutingSqlSessionFactoryBean implements FactoryBean<SqlSessionFactory>,InitializingBean,ApplicationListener<ApplicationEvent> {
	private boolean failFast;
	private Class<AbstractRoutingSessionStrategy> strategy;
	private String defaultKey;
	private String environment;
	private String typeAliasesPackage;
	private String typeHandlersPackage;
	private Class<?>[] typeAliases;
	private Interceptor[] plugins;
	private Resource configLocation;
    private Resource[] mapperLocations;
    private TypeHandler<?>[] typeHandlers;
    private Properties configurationProperties;
	private TransactionFactory transactionFactory;
	private Map<String,DataSource> targetDataSources;
	private SqlSessionFactoryBuilder sqlSessionFactoryBuilder;
	private SqlSessionFactory sqlSessionFactory;
	public static AbstractRoutingSessionStrategy ROUTING_STRATEGY;
	public static final Map<String,SqlSessionFactory> RESOLVED_SQLSESSIONFACTORYS=new HashMap<String,SqlSessionFactory>();
	public void setFailFast(boolean failFast){
		this.failFast=failFast;
	}
	public void setStrategy(Class<AbstractRoutingSessionStrategy> strategy){
		if(!AbstractRoutingSessionStrategy.class.isAssignableFrom(strategy))throw new IllegalArgumentException("指定的路由策略实现类："+strategy.getName()+" 不正确，原因：必须继承抽象类：com.al.persistence.routing.AbstractRoutingStrategy");
		try{
			this.strategy=strategy;
			ROUTING_STRATEGY=(AbstractRoutingSessionStrategy)strategy.newInstance();
		}catch(IllegalAccessException e){
			throw new IllegalArgumentException("加载路由策略实现类："+strategy.getName()+" 异常，原因："+e.getMessage());
		}catch(InstantiationException e){
			throw new IllegalArgumentException("实例化路由策略实现类："+strategy.getName()+" 异常，原因："+e.getMessage());
		}
	}
	public void setDefaultKey(String defaultKey){
		this.defaultKey=defaultKey;
	}
	public void setEnvironment(String environment){
		this.environment=environment;
	}
	public void setTypeAliasesPackage(String typeAliasesPackage){
		this.typeAliasesPackage=typeAliasesPackage;
	}
	public void setTypeHandlersPackage(String typeHandlersPackage){
		this.typeHandlersPackage=typeHandlersPackage;
	}
	public void setTypeAliases(Class<?>[] typeAliases){
		this.typeAliases=typeAliases;
	}
	public void setPlugins(Interceptor[] plugins){
		this.plugins=plugins;
	}
	public void setConfigLocation(Resource configLocation){
		this.configLocation=configLocation;
	}
	public void setMapperLocations(Resource[] mapperLocations){
		this.mapperLocations=mapperLocations;
	}
	public void setTypeHandlers(TypeHandler<?>[] typeHandlers){
		this.typeHandlers=typeHandlers;
	}
	public void setConfigurationProperties(Properties sqlSessionFactoryProperties){
		this.configurationProperties=sqlSessionFactoryProperties;
	}
	public void setTransactionFactory(TransactionFactory transactionFactory){
		this.transactionFactory=transactionFactory;
	}
	public void setTargetDataSources(Map<String,DataSource> targetDataSources){
		this.targetDataSources=targetDataSources;
	}
	public void setSqlSessionFactoryBuilder(SqlSessionFactoryBuilder sqlSessionFactoryBuilder){
		this.sqlSessionFactoryBuilder=sqlSessionFactoryBuilder;
	}
	public void afterPropertiesSet()throws Exception{
		Assert.notNull(this.targetDataSources,"请指定目标数据源列表");
		Assert.notNull(this.strategy,"请指定路由策略实现类");
		if(null==this.defaultKey)this.defaultKey=this.targetDataSources.keySet().toArray()[0].toString();
		RoutingContext.setName(this.defaultKey);
		this.sqlSessionFactory=this.buildSqlSessionFactory();
	}
	protected SqlSessionFactory buildSqlSessionFactory()throws Exception{
		SqlSessionFactoryBean factoryBean;
		for(Map.Entry<String,DataSource> entry:this.targetDataSources.entrySet()){
			factoryBean=new SqlSessionFactoryBean();
			if(null!=this.environment)factoryBean.setEnvironment(this.environment);
			factoryBean.setTypeAliasesPackage(this.typeAliasesPackage);
			factoryBean.setTypeHandlersPackage(this.typeHandlersPackage);
			factoryBean.setTypeAliases(this.typeAliases);
			factoryBean.setPlugins(this.plugins);
			factoryBean.setConfigLocation(this.configLocation);
			factoryBean.setMapperLocations(this.mapperLocations);
			factoryBean.setTypeHandlers(this.typeHandlers);
			factoryBean.setConfigurationProperties(this.configurationProperties);
			factoryBean.setTransactionFactory(this.transactionFactory);
			factoryBean.setDataSource(entry.getValue());
			if(null!=this.sqlSessionFactoryBuilder)factoryBean.setSqlSessionFactoryBuilder(this.sqlSessionFactoryBuilder);
			RESOLVED_SQLSESSIONFACTORYS.put(entry.getKey(),factoryBean.getObject());
		}
		return RESOLVED_SQLSESSIONFACTORYS.get(RoutingContext.getName());
	}
	public SqlSessionFactory getObject()throws Exception{
		if(this.sqlSessionFactory==null)this.afterPropertiesSet();
        return this.sqlSessionFactory;
	}
	public Class<? extends SqlSessionFactory> getObjectType(){
        return this.sqlSessionFactory==null?SqlSessionFactory.class:this.sqlSessionFactory.getClass();
    }
	public void onApplicationEvent(ApplicationEvent event){
		if(this.failFast && event instanceof ContextRefreshedEvent){
			for(SqlSessionFactory sqlSessionFactory:RESOLVED_SQLSESSIONFACTORYS.values()){
				sqlSessionFactory.getConfiguration().getMappedStatementNames();
			}
		}
    }
	public boolean isSingleton(){
		return true;
	}
}