package com.kss.persistence.routing;
import org.springframework.util.Assert;

/**
 * <b>功能：</b>路由策略抽象类<br>
 * <b>版本：</b>1.0；2011-03-16；创建
 */
public abstract class AbstractRoutingDataSourceStrategy{
	/**
	 * 确定当前使用数据源
	 * @return 数据源名称
	 */
	protected abstract String determineCurrentDataSourceName();
	/**
	 * 注册当前数据源
	 */
	public void registerCurrentDataSource(){
		String name;
		try{
			name=this.determineCurrentDataSourceName();
			Assert.notNull(name,"请指定当前路由的数据源名称");
			if(!RoutingDataSource.RESOLVED_DATASOURCE_NAME.contains(name))throw new IllegalArgumentException("数据源："+name+" 不存在");
			RoutingContext.setName(name);
		}finally{
			name=null;
		}
	}
}