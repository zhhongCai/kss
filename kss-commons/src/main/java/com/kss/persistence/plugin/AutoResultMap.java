package com.kss.persistence.plugin;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.mapping.ResultMapping.Builder;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaClass;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ClassUtils;

import java.sql.Statement;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <b>功能：</b>自动转换结果集插件<br>
 * <b>版本：</b>1.0；2012-02-02；创建
 */
@Intercepts({@Signature(type=StatementHandler.class,method="query",args={Statement.class,ResultHandler.class})})
public class AutoResultMap extends AbstractInterceptor{
	private final static Logger logger= LoggerFactory.getLogger(AutoResultMap.class);
	private final static Map<Class<?>,ResultMap> CACHED_RESULT_MAP=new ConcurrentHashMap<Class<?>,ResultMap>();
	@SuppressWarnings("unchecked")
	public Object intercept(Invocation invocation)throws Throwable{
		StatementHandler statementHandler;
		MetaObject metaStatementHandler;
		List<ResultMap> resultMapList;
		ResultMap resultMap;
		ResultMap tmpResultMap;
		List<ResultMapping> resultMappings;
		Class<?> type;
		Configuration configuration;
		try{
			statementHandler=(StatementHandler)super.getTarget(invocation);
			metaStatementHandler=MetaObject.forObject(statementHandler, null, null);
			resultMapList=(List<ResultMap>)metaStatementHandler.getValue(MAPPEDSTATEMENT_RESULTMAPS);
			if(resultMapList!=null&&!resultMapList.isEmpty()){
				resultMap=resultMapList.get(0);
				resultMappings=resultMap.getResultMappings();
				if(resultMappings==null||resultMappings.isEmpty()){//如果没有映射的话，自动生成
					type=resultMap.getType();
					if(!this.isPrimitiveOrWrapper(type)){//排除基本类型
						if(CACHED_RESULT_MAP.containsKey(type)){
							tmpResultMap=CACHED_RESULT_MAP.get(type);
							FieldUtils.writeField(resultMap,"resultMappings",tmpResultMap.getResultMappings(),true);
							FieldUtils.writeField(resultMap,"propertyResultMappings",tmpResultMap.getPropertyResultMappings(),true);
							FieldUtils.writeField(resultMap,"mappedColumns",tmpResultMap.getMappedColumns(),true);
							FieldUtils.writeField(resultMap,"idResultMappings",tmpResultMap.getIdResultMappings(),true);
						}else{
							configuration=(Configuration)metaStatementHandler.getValue(CONFIGURATION);
							this.buildResultMap(configuration,type,resultMap);
							CACHED_RESULT_MAP.put(type,resultMap);
						}
					}
				}
			}
			return invocation.proceed();
		}finally{
			statementHandler=null;
			metaStatementHandler=null;
			resultMapList=null;
			resultMap=null;
			tmpResultMap=null;
			resultMappings=null;
			type=null;
			configuration=null;
		}
	}
	private void buildResultMap(Configuration configuration,Class<?> type,ResultMap resultMap)throws IllegalAccessException{//根据类对象构造出ResultMapping集合
		logger.debug("类[{}]中所有字段自动映射",type);
		Class<?> clazz;
		String columnName;
		MetaClass metaClass;
		String[] setterNames;
		Set<String> mappedColumns;
		List<ResultMapping> resultMappings;
		try{
			metaClass=MetaClass.forClass(type);
			mappedColumns=new HashSet<String>();
			setterNames=metaClass.getSetterNames();
			resultMappings=new ArrayList<ResultMapping>();
			for(String setterName:setterNames){
				columnName=this.getColumnName(setterName).toUpperCase();
				clazz=metaClass.getSetterType(setterName);
				logger.debug("映射信息[property={},column={},javaType={}]",new String[]{setterName,columnName,clazz.toString()});
				resultMappings.add(new Builder(configuration,setterName,columnName,clazz).build());
				mappedColumns.add(columnName);
			}
			if(!resultMappings.isEmpty()){
				FieldUtils.writeField(resultMap,"resultMappings",resultMappings,true);
				FieldUtils.writeField(resultMap,"propertyResultMappings",resultMappings,true);
				FieldUtils.writeField(resultMap,"mappedColumns",mappedColumns,true);
				FieldUtils.writeField(resultMap,"idResultMappings",resultMappings,true);
			}
		}finally{
			clazz=null;
			columnName=null;
			metaClass=null;
			setterNames=null;
			mappedColumns=null;
			resultMappings=null;
		}
	}
	private String getColumnName(String properteName){//将属性名称转成数据字段名称
		StringBuffer name;
		try{
			name=new StringBuffer();
			for(int i=0;i<properteName.length();i++){
				char c=properteName.charAt(i);
				if(c>=65&&c<=90&&name.length()>0)name.append("_");
				name.append(Character.toUpperCase(c));
			}
			return name.toString();
		}finally{
			name=null;
		}
	}
	private boolean isPrimitiveOrWrapper(Class<?> clazz){//判断是否基本类型
		return clazz.isAssignableFrom(Map.class)||clazz.isAssignableFrom(List.class)||clazz.isAssignableFrom(String.class)|| ClassUtils.isPrimitiveOrWrapper(clazz);
	}
}