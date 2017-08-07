package com.kss.commons;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * Created by chenchw on 2017/5/11.
 */
public class ResponsePage<T> {
    @ApiModelProperty(value = "分页总记录数")
    private int total;
    @ApiModelProperty(value = "当前页列表")
    private List<T> data;
    @ApiModelProperty(value = "统计信息")
    private Object count;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public Object getCount() {
        return count;
    }

    public void setCount(Object count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "ResponsePage{" +
                "total=" + total +
                ", data=" + data +
                ", count=" + count +
                '}';
    }
}
