package com.kss.core.job;

import com.kss.core.datasource.DbConnectionInfo;

import java.util.List;
import java.util.concurrent.Callable;

public abstract class KssTask<V> implements Callable<V> {

    private List<DbConnectionInfo> srcDbConnectionInfos;

    /**
     * 执行业务之前，获取配置信息等
     */
    abstract List<DbConnectionInfo> before();

    /**
     * 执行具体业务代码, 具体逻辑由子类在call中实现
     * @return
     * @throws Exception
     */
    public V doWork() throws Exception {
        this.srcDbConnectionInfos = before();

        V result = call();

        after();

        return result;
    }

    /**
     * 执行之后
     */
    abstract void after();

    public List<DbConnectionInfo> getSrcDbConnectionInfos() {
        return srcDbConnectionInfos;
    }

    public void setSrcDbConnectionInfos(List<DbConnectionInfo> srcDbConnectionInfos) {
        this.srcDbConnectionInfos = srcDbConnectionInfos;
    }
}
