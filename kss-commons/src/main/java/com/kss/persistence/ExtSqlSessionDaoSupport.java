package com.kss.persistence;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DaoSupport;
import org.springframework.util.Assert;

/**
 * <b>功能：</b>持久层数据库操作超类<br>
 * <b>版本：</b>1.0；2011-03-14；创建
 */
public class ExtSqlSessionDaoSupport extends DaoSupport{
	private SqlSession sqlSession;
	private boolean externalSqlSession;
	@Autowired(required=false)
	public final void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory){
		if(!this.externalSqlSession)this.sqlSession=new ExtSqlSessionTemplate(sqlSessionFactory);
	}
	@Autowired(required=false)
	public final void setSqlSessionTemplate(ExtSqlSessionTemplate extSqlSessionTemplate){
		this.sqlSession=extSqlSessionTemplate;
		this.externalSqlSession=true;
	}
	public final SqlSession getSqlSession(){
		return this.sqlSession;
	}
	protected void checkDaoConfig(){
		Assert.notNull(this.sqlSession,"Property 'sqlSessionFactory' or 'sqlSessionTemplate' are required");
	}
}