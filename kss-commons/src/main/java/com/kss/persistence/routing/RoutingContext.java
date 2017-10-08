package com.kss.persistence.routing;
/**
 * <b>功能：</b>路由器上下文对象<br>
 * <b>版本：</b>1.0；2011-02-28；创建
 */
public class RoutingContext{
	private static final ThreadLocal<String> contextHolder=new ThreadLocal<String>();
	public static void setName(String name){
		contextHolder.set(name.toString());
	}
	public static String getName(){
		return contextHolder.get();
	}
	public static void clearName(){
		contextHolder.remove();
	}
}