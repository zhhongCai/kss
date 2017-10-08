/**
 * 
 */
package com.kss;

import com.kss.persistence.DataTypeDefinition;
import com.kss.sqlengine.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.fail;


//@ContextConfiguration(locations={"classpath:*/*.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ISQLEngineTest {
	private static Logger log = LoggerFactory.getLogger(ISQLEngineTest.class);
	private DbDataSource ds = null;

//	private ApplicationContext ctx;

	@Before
	public void setUp() throws Exception {
		ds = new DbDataSource();
		ds.setDriverClassName("com.mysql.jdbc.OracleDriver");
		ds.setUrl("jdbc:mysql://localhost:3306/manage-web");
		ds.setUserName("manage-web");
		ds.setPassword("manage-web");

	}


	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testExecuteQuery() throws DBAccessCheckedException {
		ISQLEngine sqlEngine = new SQLEngine();
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
		paramA.setParamValue("2");
		list.add(paramA);

		SqlParam paramB = new SqlParam();
		paramB.setDataType(DataTypeDefinition.DATA_TYPE_DATE);
		paramB.setInOut("in");
		paramB.setParamName("createTime");
		paramB.setParamValue("2012-05-16 00:00:00");
		list.add(paramB);

		Result res = sqlEngine
				.executeQuery(
						"select * from base_user " +
						"where id = #id# and state = #username# and create_time <= #createTime#",
						list, ds);
		log.debug("result:" + res.getResult());
	}

	@Test
	public void testExecuteUpdate() {
		fail("Not yet implemented");
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
