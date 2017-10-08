package com.kss.persistence.routing;
import org.springframework.util.Assert;

/**
 * <b>功能：</b>分布式策略抽象类<br>
 * <b>版本：</b>1.0；2011-03-16；创建
 */
public abstract class AbstractRoutingSessionStrategy{
	/**
	 * 确定执行SQL语句使用的数据源
	 * @param statementId 执行语句的ID
	 * @param parameters 执行语句的参数
	 * @return 数据源名称
	 */
	protected abstract String determineCurrentDataSourceName(String statementId,Object[] parameters);
	/**
	 * 注册当前数据源
	 */
	public void registerCurrentDataSource(String statementId,Object[] parameters){
		String name;
		try{
			name=this.determineCurrentDataSourceName(statementId,parameters);
			Assert.notNull(name,"请指定当前路由的数据源名称");
			if(!RoutingSqlSessionFactoryBean.RESOLVED_SQLSESSIONFACTORYS.containsKey(name))throw new IllegalArgumentException("数据源："+name+" 不存在");
			RoutingContext.setName(name);
		}finally{
			name=null;
		}
	}
}