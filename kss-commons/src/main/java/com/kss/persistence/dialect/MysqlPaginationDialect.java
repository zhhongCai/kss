package com.kss.persistence.dialect;

/**
 * MYSQL物理分页方言
 */
public class MysqlPaginationDialect extends PaginationDialect{
    @Override
    public String getLimitString(String sql, int offset, int limit) {
        StringBuffer pagingSql;
        try{
            pagingSql=new StringBuffer();
            pagingSql.append(sql.trim()).append(" offset ").append(offset).append(" limit ").append(limit);
            return pagingSql.toString();
        }finally{
            pagingSql=null;
        }
    }
}
