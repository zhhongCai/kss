package com.kss.persistence;
import org.apache.ibatis.session.RowBounds;

/**
 * <b>功能：</b>分页信息类<br>
 * <b>版本：</b>1.0；2011-02-07；创建
 */
public class PagingInfo extends RowBounds{
	public PagingInfo(){
		super();
	}
	public PagingInfo(int offset,int limit){
		super(offset,limit);
	}
}