package com.kss.persistence.dialect;
/**
 * <b>功能：</b>ORACLE物理分页方言<br>
 * <b>版本：</b>1.0；2012-02-02；创建
 */
public class OraclePaginationDialect extends PaginationDialect{
	public String getLimitString(String sql,int offset,int limit){
		StringBuffer pagingSql;
		try{
			pagingSql=new StringBuffer();
			pagingSql.append("SELECT * FROM(SELECT ROW_.*,ROWNUM ROWNUM_ FROM(");
			pagingSql.append(sql.trim());
			pagingSql.append(")ROW_ )WHERE ROWNUM_>"+offset+" AND ROWNUM_<="+(offset+limit));
			return pagingSql.toString();
		}finally{
			pagingSql=null;
		}
	}
}