package com.kss.persistence.plugin;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;

/**
 * <b>功能：</b>自动计算总记录数插件<br>
 * <b>版本：</b>1.0；2012-02-02；创建
 */
@Intercepts({@Signature(type=StatementHandler.class,method="prepare",args={Connection.class})})
public class AgentCountRowNumber extends AbstractInterceptor{
	private final static Logger logger= LoggerFactory.getLogger(AgentCountRowNumber.class);
	public Object intercept(Invocation invocation)throws Throwable{
		String sql;
		String agentSql;
		String agentSqlId;
		BoundSql boundSql;
		Configuration configuration;
		MetaObject metaStatementHandler;
		MappedStatement mappedStatement;
		StatementHandler statementHandler;
		try{
			statementHandler=(StatementHandler)super.getTarget(invocation);
			metaStatementHandler=MetaObject.forObject(statementHandler, null, null);
			configuration=(Configuration)metaStatementHandler.getValue(CONFIGURATION);
			sql=(String)metaStatementHandler.getValue(BOUNDSQL_SQL);
			if(this.isAgentCountSQL(sql)){//判断是否有#号开头
				agentSqlId=sql.substring(1);
				logger.debug("根据：{}生成计算记录数的语句",agentSqlId);
				mappedStatement=configuration.getMappedStatement(agentSqlId);
				boundSql=mappedStatement.getBoundSql(metaStatementHandler.getValue(BOUNDSQL_PARAMETEROBJECT));
				agentSql=parseAgentSql(boundSql.getSql());
				logger.debug("生成的计算记录数的语句：{}",agentSql);
				metaStatementHandler.setValue(BOUNDSQL_PARAMETERMAPPINGS,boundSql.getParameterMappings());
				metaStatementHandler.setValue(BOUNDSQL_SQL,agentSql);
			}
			return invocation.proceed();
		}finally{
			sql=null;
			agentSql=null;
			agentSqlId=null;
			boundSql=null;
			configuration=null;
			metaStatementHandler=null;
			mappedStatement=null;
			statementHandler=null;
		}
	}
	private boolean isAgentCountSQL(String sql){
		return (sql!=null&&sql.charAt(0)=='#')?true:false;
	}
	private String parseAgentSql(String agentSql){//解析出统计的sql,可以考虑将order by段去掉
		StringBuffer buffer;
		String flag=")";
		String preStr="select ";
		String orderBy="order by ";
		try{
			buffer=new StringBuffer(agentSql.toLowerCase());
			int start=buffer.indexOf(preStr);
			int end=buffer.indexOf(" from");
			if(start!=-1&&end!=-1){
				buffer.delete(start+preStr.length(),end);
				buffer.insert(preStr.length()," count(1) ");
			}
			// 解析去掉order by 信息
			// order by语句后有几种情况
			// 1、order by fieldName
			// 2、order by fieldName desc/asc,fieldName desc/asc
			// 3、select * from(select * from table order by fieldName desc/asc)
			// order by order by fieldName
			// 可以总结出,
			// 1、order by 后面要么是空,要么是参右括号结尾;
			// 2、一个sql语句里可能包含多个order by
			while((start=buffer.indexOf(orderBy))!=-1){
				end=buffer.indexOf(flag,start);//是否有右括号
				if(end==-1)end=buffer.length();
				buffer.delete(start,end);
			}
			start=buffer.indexOf(orderBy);
			return buffer.toString();
		}finally{
			buffer=null;
			flag=null;
			preStr=null;
			orderBy=null;
		}
	}
	public static void main(String[] args){
		AgentCountRowNumber agentCount=new AgentCountRowNumber();
		// String selSql = "select * from table";
		// String selSql = "select a,b,c,d,e from table";
		// String selSql = "select a,b,c from (select a,b,c,d,e from table) where a=1";
		// String selSql = "select a,b,c from (select a,b,c,d,e from table order by a) where a=1";
		String selSql="select a,b,c from (select a,b,c,d,e from table order by a asc) where a=1 order by c desc ";
		String countSql=agentCount.parseAgentSql(selSql);
		System.out.println(countSql);
	}
}