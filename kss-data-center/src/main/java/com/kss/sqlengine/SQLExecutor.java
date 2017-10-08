/**
 * 
 */
package com.kss.sqlengine;

import com.kss.commons.exceptions.ServiceException;
import com.kss.commons.util.DateUtil;
import com.kss.commons.util.ResultSetUtil;
import com.kss.persistence.DataTypeDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class SQLExecutor implements ISQLExecutor {
	private static Logger logger = LoggerFactory.getLogger(SQLExecutor.class);

	/* (non-Javadoc)
	 * @see com.al.crm.bmm.tool.ISQLExecutor#executeQuery(java.sql.Connection, java.lang.String, java.util.Map)
	 */
	public List<Map<String,Object>> executeQuery(Connection conn, String sql,
			List<SqlParam> params, boolean closeAfterFinish) throws SQLException, ParseException{
		//String result = null;
		PreparedStatement ps = null;
		
		try {
			ps = conn.prepareStatement(sql); 
			setParameters(ps, params);
			ResultSet rs;
			
			rs = ps.executeQuery();
			
			//result = Result2JSON.convertResultSetToJSON(rs);
			return ResultSetUtil.convertResultSetToList(rs);
		
		} catch (SQLException e) {
			logger.error("executeQuery执行失败 {}", e);
			throw e;
		} finally{
			if(ps != null){
				ps.close();
			}
			
			if(closeAfterFinish){
				conn.close();
			}
		}
			
		//return result;
	}
	
	/* (non-Javadoc)
	 * @see com.al.crm.bmm.sqlengine.ISQLExecutor#executeQuery(java.sql.Connection, java.lang.String, java.util.List)
	 */
	public List<Map<String,Object>> executeQuery(Connection conn, String sql,
			List<SqlParam> params) throws SQLException, ParseException{
		return executeQuery(conn, sql, params, true);
	}

	/* (non-Javadoc)
	 * @see com.al.crm.bmm.tool.ISQLExecutor#executeUpdate(java.sql.Connection, java.lang.String, java.util.Map)
	 */
	public void executeUpdate(Connection conn, String sql,
			List<SqlParam> params) throws SQLException, ParseException{
		CallableStatement cs = null;
		logger.debug("SQL: {}" +sql);
		try{
			cs = conn.prepareCall(sql); 
			List<Integer> outParamIdxs = new ArrayList<Integer>();			
			setParameters(cs, params, outParamIdxs);						
			cs.executeUpdate();			
			getOutParameters(cs, params, outParamIdxs);
		}catch (SQLException e) {
			logger.error("executeUpdate执行失败 {}", e);
			throw e;
		}catch (ParseException e) {
			logger.error("executeUpdate执行失败 {}", e);
			throw e;
		} finally{
			if(cs != null){
				cs.close();
			}
			//注意：执行后不能关闭连接。在提交或回滚事务时统一关闭。
		}				
	}

	/* (non-Javadoc)
	 * @see com.al.crm.bmm.tool.ISQLExecutor#executeUpdate(java.sql.Connection, 
	 * java.lang.String, java.util.Map, boolean)
	 */
	public void executeUpdate(Connection conn, String sql,
			List<SqlParam> params, boolean closeAfterFinish) throws SQLException, ParseException{
		try{
			executeUpdate(conn, sql, params);
		}finally{
			if(closeAfterFinish){
				conn.close();
			}
		}
	}
	
	private void getOutParameters(CallableStatement stat, List<SqlParam> params, 
			List<Integer> outParamIdxs) throws SQLException{
		for(Integer idx : outParamIdxs){
			getOutParameter(stat, idx.intValue(), params.get(idx.intValue()));
		}
	}
	
	private void getOutParameter(CallableStatement stat, int idx, SqlParam param) throws SQLException{
		// 注意，必须加上param.getParamValue() == null判断，这是因为SQL中可能同名的输出参数会有多出，
		// 如果不加判断，则返回值可能被覆盖
		if(param.getParamValue() == null){
			param.setParamValue(stat.getString(idx + 1));
		}
		/*if(DataTypeDefinition.DATA_TYPE_STRING.equalsIgnoreCase(param.getDataType()))
			param.setParamValue(stat.getString(idx));
		else if(DataTypeDefinition.DATA_TYPE_INTEGER.equalsIgnoreCase(param.getDataType()))
			param.setParamValue(stat.getString(idx));
		else if(DataTypeDefinition.DATA_TYPE_LONG.equalsIgnoreCase(param.getDataType()))
			param.setParamValue(stat.getString(idx));
		else if(DataTypeDefinition.DATA_TYPE_FLOAT.equalsIgnoreCase(param.getDataType()))
			param.setParamValue(stat.getString(idx));
		else if(DataTypeDefinition.DATA_TYPE_DOUBLE.equalsIgnoreCase(param.getDataType()))
			param.setParamValue(stat.getString(idx));
		else if(DataTypeDefinition.DATA_TYPE_DATE.equalsIgnoreCase(param.getDataType()))
		*/
	}
	
	private void setParameters(PreparedStatement stat, List<SqlParam> params) throws SQLException, ParseException{
		setParameters(stat, params, null);
	}
		
	private void setParameters(PreparedStatement stat, List<SqlParam> params, 
			List<Integer> outParamIdxs) throws SQLException, ParseException{
		if(params == null){
			return;
		}
		for(int i=0; i<params.size(); i++){
			setParameter(stat, i+1, params.get(i));
			if(outParamIdxs != null && "out".equalsIgnoreCase(params.get(i).getInOut())){ 
				outParamIdxs.add(new Integer(i));
			}
		}
	}
	
	private void setParameter(PreparedStatement stat, int idx, SqlParam param) throws SQLException, ParseException{
		if("in".equalsIgnoreCase(param.getInOut())){
			if(DataTypeDefinition.DATA_TYPE_STRING.equalsIgnoreCase(param.getDataType())){
				stat.setString(idx, param.getParamValue());
			}else if(DataTypeDefinition.DATA_TYPE_INTEGER.equalsIgnoreCase(param.getDataType())){
				stat.setInt(idx, Integer.parseInt(param.getParamValue()));
			}else if(DataTypeDefinition.DATA_TYPE_LONG.equalsIgnoreCase(param.getDataType())){
				stat.setLong(idx, Long.parseLong(param.getParamValue()));
			}else if(DataTypeDefinition.DATA_TYPE_FLOAT.equalsIgnoreCase(param.getDataType())){
				stat.setFloat(idx, Float.parseFloat(param.getParamValue()));
			}else if(DataTypeDefinition.DATA_TYPE_DOUBLE.equalsIgnoreCase(param.getDataType())){
				stat.setDouble(idx, Double.parseDouble(param.getParamValue()));
			}else if(DataTypeDefinition.DATA_TYPE_DATE.equalsIgnoreCase(param.getDataType())){
				stat.setDate(idx, new java.sql.Date(DateUtil.getDateFromString(param.getParamValue(),
						DateUtil.DATE_FORMATE_STRING_DEFAULT).getTime()));
			}
		}else if(stat instanceof CallableStatement){
			CallableStatement cs = (CallableStatement)stat;
			if(DataTypeDefinition.DATA_TYPE_STRING.equalsIgnoreCase(param.getDataType())){
				cs.registerOutParameter(idx, Types.VARCHAR);
			}else if(DataTypeDefinition.DATA_TYPE_INTEGER.equalsIgnoreCase(param.getDataType())){
				cs.registerOutParameter(idx, Types.INTEGER);
			}else if(DataTypeDefinition.DATA_TYPE_LONG.equalsIgnoreCase(param.getDataType())){
				cs.registerOutParameter(idx, Types.INTEGER);
			}else if(DataTypeDefinition.DATA_TYPE_FLOAT.equalsIgnoreCase(param.getDataType())){
				cs.registerOutParameter(idx, Types.FLOAT);
			}else if(DataTypeDefinition.DATA_TYPE_DOUBLE.equalsIgnoreCase(param.getDataType())){
				cs.registerOutParameter(idx, Types.DOUBLE);
			}else if(DataTypeDefinition.DATA_TYPE_DATE.equalsIgnoreCase(param.getDataType())){
				cs.registerOutParameter(idx, Types.DATE);
			}
		}
	}

	public void execute(Connection conn, String sql, boolean closeAfterFinish) throws Exception{
		PreparedStatement ps = null;
		
		try {
			ps = conn.prepareStatement(sql); 
//			ps.executeBatch();
			ps.execute();
		} catch (Exception e) {
			logger.error("execute sql: {}, 执行失败 {}", sql, e);
			throw new ServiceException(e);
		} finally{
			if(ps != null){
				ps.close();
			}
			if(closeAfterFinish){
				conn.close();
			}
		}
    }
	
	public void executeBatchInsert(Connection conn, String sql, List<List<Object>> data,
			boolean closeAfterFinish) throws Exception{
		PreparedStatement ps = null;
		
		try {
			ps = conn.prepareStatement(sql); 
			logger.debug("应插入记录数: {}", data.size());
			for(List<Object> list : data){
	            for(int i = 0; i < list.size(); i++){
	            	Object tmp = list.get(i);
	                ps.setObject(i+1,  tmp);
                }
	            ps.addBatch();
            }
			ps.executeBatch();
			conn.commit();
			int count = ps.getUpdateCount();
			logger.debug("修改记录数: {}", count);
		} catch (Exception e) {
			logger.error("执行失败 {}", e);
			throw new ServiceException(e);
		} finally{
			if(ps != null){
				ps.close();
			}
			if(closeAfterFinish){
				conn.close();
			}
		}
		
	}
}
