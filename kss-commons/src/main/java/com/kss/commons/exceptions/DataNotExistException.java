package com.kss.commons.exceptions;

public class DataNotExistException extends RuntimeException {

    private String dataName ;

    private Long dataId;

    public DataNotExistException(String message, String dataName, Long dataId) {
        super(message);
        this.dataName = dataName;
        this.dataId = dataId;
    }

    public DataNotExistException(String message, Throwable cause, String dataName, Long dataId) {
        super(message, cause);
        this.dataName = dataName;
        this.dataId = dataId;
    }

    public String getDataName() {
        return dataName;
    }

    public Long getDataId() {
        return dataId;
    }
}
