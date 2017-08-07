package com.kss.commons;

import java.io.Serializable;
import java.util.List;

/**
 * 接口分页返回对象
 */
public class PageResponse<T> implements Serializable {

    private static final long serialVersionUID = 8723669999683487369L;

    /**
     * 总页数
     */
    private Integer total = 0;

    /**
     * 当前页
     */
    private Integer page = 0;

    /**
     * 本次查询的总记录数
     */
    private Integer records = 0;

    /**
     * 结果列表
     */
    private List<T> rows;

    public PageResponse() {}

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getRecords() {
        return records;
    }

    public void setRecords(Integer records) {
        this.records = records;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

}
