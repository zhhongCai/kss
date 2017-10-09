package com.kss.persistence.plugin;

import com.kss.persistence.dialect.Dialect.Type;
import com.kss.persistence.dialect.MysqlPaginationDialect;
import com.kss.persistence.dialect.OraclePaginationDialect;
import com.kss.persistence.dialect.PaginationDialect;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;

/**
 * <b>功能：</b>物理分页插件<br>
 * <b>版本：</b>1.0；2012-02-02；创建
 */
@Intercepts({@Signature(type=StatementHandler.class,method="prepare",args={Connection.class})})
public class Pagination extends AbstractInterceptor{
	private final static Logger logger= LoggerFactory.getLogger(Pagination.class);
	public Object intercept(Invocation invocation)throws Throwable{
		Type dialect;
		RowBounds rowBounds;
		PaginationDialect paginationDialect;
		StatementHandler statementHandler;
		MetaObject metaStatementHandler;
		try{
			statementHandler=(StatementHandler)super.getTarget(invocation);
			metaStatementHandler=MetaObject.forObject(statementHandler, null, null);//将statementHandler对象转化为元数据对象（其实就是将RoutingStatementHandler类进行转化）
			rowBounds=(RowBounds)metaStatementHandler.getValue(ROWBOUNDS);//获取RoutingStatementHandler类中的delegate属性中的rowBounds属性对象
			if(rowBounds==null||rowBounds==RowBounds.DEFAULT)return invocation.proceed();
			try{
				dialect= Type.valueOf(((Configuration)metaStatementHandler.getValue(CONFIGURATION)).getVariables().getProperty("dialect").toUpperCase());
			}catch(Exception e){
				dialect= Type.ORACLE;
				logger.warn("数据库方言未指定或指定的方言不存在，系统默认指定为数据库方言为：{}",dialect);
			}
			switch(dialect){//其它数据分页语言在此处添加
				case MYSQL:
					paginationDialect=new MysqlPaginationDialect();
				default:
					paginationDialect=new OraclePaginationDialect();
			}
			metaStatementHandler.setValue(BOUNDSQL_SQL,paginationDialect.getLimitString((String)metaStatementHandler.getValue(BOUNDSQL_SQL),rowBounds.getOffset(),rowBounds.getLimit()));
			metaStatementHandler.setValue(ROWBOUNDS_OFFSET,RowBounds.NO_ROW_OFFSET);
			metaStatementHandler.setValue(ROWBOUNDS_LIMIT,RowBounds.NO_ROW_LIMIT);
			logger.debug("生成分页SQL : {}",statementHandler.getBoundSql().getSql());
			return invocation.proceed();
		}finally{
			dialect=null;
			rowBounds=null;
			paginationDialect=null;
			statementHandler=null;
			metaStatementHandler=null;
		}
	}
}