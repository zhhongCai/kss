package com.kss.persistence.routing;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.util.Assert;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * <b>功能：</b>数据源路由器<br>
 * <b>版本：</b>1.0；2011-02-10；创建
 */
public class RoutingDataSource extends AbstractRoutingDataSource {
	private String defaultKey;
	private Map<Object,Object> targetDataSources;
	private Class<AbstractRoutingDataSourceStrategy> strategy;
	public AbstractRoutingDataSourceStrategy routingStrategy;
	public static final List<String> RESOLVED_DATASOURCE_NAME=new LinkedList<String>();
	public void setDefaultKey(String defaultKey){
		this.defaultKey=defaultKey;
	}
	public void setTargetDataSources(Map<Object,Object> targetDataSources){
		this.targetDataSources=targetDataSources;
	}
	public void setStrategy(Class<AbstractRoutingDataSourceStrategy> strategy){
		if(!AbstractRoutingDataSourceStrategy.class.isAssignableFrom(strategy))throw new IllegalArgumentException("指定的路由策略实现类："+strategy.getName()+" 不正确，原因：必须继承抽象类：com.al.persistence.routing.AbstractRoutingDataSourceStrategy");
		try{
			this.strategy=strategy;
			this.routingStrategy=(AbstractRoutingDataSourceStrategy)strategy.newInstance();
		}catch(IllegalAccessException e){
			throw new IllegalArgumentException("加载路由策略实现类："+strategy.getName()+" 异常，原因："+e.getMessage());
		}catch(InstantiationException e){
			throw new IllegalArgumentException("实例化路由策略实现类："+strategy.getName()+" 异常，原因："+e.getMessage());
		}
	}
	protected String determineCurrentLookupKey(){
		this.routingStrategy.registerCurrentDataSource();
		return RoutingContext.getName();
	}
	public void afterPropertiesSet(){
		if(null!=this.targetDataSources){
			for(Object name:this.targetDataSources.keySet()){
				RESOLVED_DATASOURCE_NAME.add(name.toString());
			}
			super.setTargetDataSources(this.targetDataSources);
			if(null==this.defaultKey)this.defaultKey=this.targetDataSources.keySet().toArray()[0].toString();
			RoutingContext.setName(this.defaultKey);
		}
		Assert.notNull(this.strategy,"请指定路由策略实现类");
		super.afterPropertiesSet();
	}
}