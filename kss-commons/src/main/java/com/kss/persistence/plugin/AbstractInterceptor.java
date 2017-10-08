package com.kss.persistence.plugin;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;

import java.lang.reflect.Proxy;
import java.util.Properties;

/**
 * <b>功能：</b>插件抽象类<br>
 * <b>版本：</b>1.0；2012-02-02；创建
 */
public abstract class AbstractInterceptor implements Interceptor{
	protected final static String BOUNDSQL_PARAMETERMAPPINGS="delegate.boundSql.parameterMappings";
	protected final static String BOUNDSQL_PARAMETEROBJECT="delegate.boundSql.parameterObject";
	protected final static String BOUNDSQL_SQL="delegate.boundSql.sql";
	protected final static String CONFIGURATION="delegate.configuration";
	protected final static String MAPPEDSTATEMENT_RESULTMAPS="delegate.mappedStatement.resultMaps";
	protected final static String ROWBOUNDS="delegate.rowBounds";
	protected final static String ROWBOUNDS_LIMIT="delegate.rowBounds.limit";
	protected final static String ROWBOUNDS_OFFSET="delegate.rowBounds.offset";
	public Object getTarget(Invocation invocation){
		return this.getObject(invocation.getTarget());
	}
	private Object getObject(Object target){
		if(Proxy.isProxyClass(target.getClass())){
			try{
				target=FieldUtils.readDeclaredField(Proxy.getInvocationHandler(target),"target",true);
			}catch(IllegalAccessException e){
				return null;
			}
			if(Proxy.isProxyClass(target.getClass()))target=this.getObject(target);
		}
		return target;
	}
	public Object plugin(Object target){
		return Plugin.wrap(target,this);
	}
	public void setProperties(Properties properties){
	}
}