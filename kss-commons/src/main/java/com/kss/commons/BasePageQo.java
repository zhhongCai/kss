package com.kss.commons;

import java.io.Serializable;

/**
 * 分页查询基础qo，使得涉及分页查询的业务无需再关注分页设定，使其标准化
 */
public class BasePageQo implements Serializable{

    /**
     * 每页记录数(默认20条每页)
     */
    protected Integer pageSize = 20;

    /**
     * 当前页码(默认第一页)
     */
    protected Integer pageNumber = 1;


    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
