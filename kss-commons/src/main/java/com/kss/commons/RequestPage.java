package com.kss.commons;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

public class RequestPage  implements Serializable {

    private static final long serialVersionUID = -1500765438278015344L;

    @ApiModelProperty(value = "当前页",required = true)
    private Integer pageNumber = 1;//当前页
    @ApiModelProperty(value = "每页大小",required = true)
    private Integer pageSize = 20;//每页大小

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public RequestPage(Integer pageNumber, Integer pageSize) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    public RequestPage() {
    }
}
