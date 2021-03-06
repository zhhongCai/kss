/**
 * 
 */
package com.kss;

import com.kss.persistence.DataTypeDefinition;
import com.kss.sqlengine.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.fail;


public class ISQLEngineTest extends BaseJunit4Test {
	private static Logger log = LoggerFactory.getLogger(ISQLEngineTest.class);

	private DbDataSource ds = null;

	@Autowired
	private ISQLEngine sqlEngine = null;

	@Before
	public void setUp() throws Exception {
		ds = new DbDataSource();
		ds.setDriverClassName("com.mysql.jdbc.Driver");
		ds.setUrl("jdbc:mysql://localhost:3306/manage-web");
		ds.setUserName("manage-web");
		ds.setPassword("manage-web");
	}


	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testExecuteQuery() throws DBAccessCheckedException {
		List<SqlParam> list = new ArrayList<SqlParam>();
		SqlParam param = new SqlParam();
		param.setDataType(DataTypeDefinition.DATA_TYPE_INTEGER);
		param.setInOut("in");
		param.setParamName("id");
		param.setParamValue("1");
		list.add(param);

		SqlParam paramA = new SqlParam();
		paramA.setDataType(DataTypeDefinition.DATA_TYPE_STRING);
		paramA.setInOut("in");
		paramA.setParamName("username");
		paramA.setParamValue("admin");
		list.add(paramA);

		SqlParam paramB = new SqlParam();
		paramB.setDataType(DataTypeDefinition.DATA_TYPE_DATE);
		paramB.setInOut("in");
		paramB.setParamName("createTime");
		paramB.setParamValue("2012-05-16 00:00:00");
		list.add(paramB);

		Result res = sqlEngine.executeQuery("select * from base_user where id = #id# " +
				" and username = #username# and create_time > #createTime#", list, ds);

		log.info("result:" + res.getResult());
	}

	@Test
	public void testExecuteUpdate() throws DBAccessCheckedException {
		List<SqlParam> list = new ArrayList<SqlParam>();
		SqlParam param = new SqlParam();
		param.setDataType(DataTypeDefinition.DATA_TYPE_STRING);
		param.setInOut("in");
		param.setParamName("department");
		param.setParamValue("testupdate");
		list.add(param);

		Result res = sqlEngine.executeUpdate("update base_user set department=#department# where id=1", list, ds);
		log.info("result:" + res.getResult());
	}

	@Test
	public void testCreateDb() throws DBAccessCheckedException {
		Result res = sqlEngine.executeUpdate(
				" DROP DATABASE IF EXISTS `database-test` ", null, ds);
		log.info("result:{}, code={}", res.getResult(), res.getDbCode());
		res = sqlEngine.executeUpdate(
				" CREATE DATABASE `database-test`", null, ds);
		log.info("result:{}, code={}", res.getResult(), res.getDbCode());
	}

	@Test
	public void testExecuteUpdateWithTransId() {
		fail("Not yet implemented");
	}

	@Test
	public void testBeginTransaction() {
		fail("Not yet implemented");
	}

	@Test
	public void testRollback() {
		fail("Not yet implemented");
	}

	@Test
	public void testCommit() {
		fail("Not yet implemented");
	}

}
