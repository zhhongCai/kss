package com.kss.commons;

import java.util.List;

/**
 * Created by chenchw on 2017/5/11.
 */
public class ResponseData<T> {
    private int total;

    private List<T> rows;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}
