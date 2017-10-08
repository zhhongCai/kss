/**
 * 
 */
package com.kss.commons.util;

import com.kss.commons.exceptions.DataConvertException;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author linrq
 * 
 */
public class ResultSetUtil {
	public static String convertResultSetToJSON(ResultSet rs) {
		String result = null;
		List<Map<String,Object>> list = convertResultSetToList(rs); 	
		try {
			JsonMapper mapper = new JsonMapper();
			result = mapper.toJson(list);
		} catch (Exception e) {
			throw new DataConvertException("从结果集转换成JSON串失败", e);
		}
		
		return result;		
	}

	public static List<Map<String,Object>> convertResultSetToList(ResultSet rs){
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {			
			ResultSetMetaData rsmd = rs.getMetaData();

			while (rs.next()) {
				int numColumns = rsmd.getColumnCount();
				Map<String, Object> obj = new LinkedHashMap<String, Object>();

				for (int i = 1; i < numColumns + 1; i++) {
					String columnName = rsmd.getColumnName(i);
					
					if("ROWNUM_".equals(columnName)){
						continue;
					}

					if (rsmd.getColumnType(i) == Types.ARRAY) {
						obj.put(columnName, rs.getArray(columnName));
					} else if (rsmd.getColumnType(i) == Types.BIGINT) {
						obj.put(columnName, rs.getInt(columnName));
					} else if (rsmd.getColumnType(i) == Types.BOOLEAN) {
						obj.put(columnName, rs.getBoolean(columnName));
					} else if (rsmd.getColumnType(i) == Types.BLOB) {
						obj.put(columnName, rs.getBlob(columnName));
					} else if (rsmd.getColumnType(i) == Types.DOUBLE) {
						obj.put(columnName, rs.getDouble(columnName));
					} else if (rsmd.getColumnType(i) == Types.FLOAT) {
						obj.put(columnName, rs.getFloat(columnName));
					} else if (rsmd.getColumnType(i) == Types.INTEGER) {
						obj.put(columnName, rs.getInt(columnName));
					} else if (rsmd.getColumnType(i) == Types.VARCHAR) {
						obj.put(columnName, rs.getString(columnName));
					} else if (rsmd.getColumnType(i) == Types.TINYINT) {
						obj.put(columnName, rs.getInt(columnName));
					} else if (rsmd.getColumnType(i) == Types.SMALLINT) {
						obj.put(columnName, rs.getInt(columnName));
					} else if (rsmd.getColumnType(i) == Types.DATE) {
						obj.put(columnName, rs.getDate(columnName));
					} else if (rsmd.getColumnType(i) == Types.TIMESTAMP) {
						obj.put(columnName, rs.getTimestamp(columnName));
					} else {
						obj.put(columnName, rs.getObject(columnName));
					}
				}
				list.add(obj);
			}
			
			
		} catch (SQLException e) {
			throw new DataConvertException("从结果集读取数据失败", e);
		}
		return list;
	}
}
