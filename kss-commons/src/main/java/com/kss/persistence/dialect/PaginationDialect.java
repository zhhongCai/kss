package com.kss.persistence.dialect;
/**
 * <b>功能：</b>物理分页超类<br>
 * <b>版本：</b>1.0；2012-02-02；创建
 */
public abstract class PaginationDialect extends Dialect{
	/**
	 * 获取物理分页SQL语句
	 * @param sql 原始SQL语句
	 * @param offset 偏移下标
	 * @param limit 分页步长
	 * @return 物理分页SQL语句
	 */
	public abstract String getLimitString(String sql,int offset,int limit);
}