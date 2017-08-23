package com.kss.manage.client;

import com.kss.manage.model.DbDataSource;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.hateoas.PagedResources;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("manage-web")
public interface DbDataSourceClient {

    @RequestMapping(method = RequestMethod.GET, value = "/api/dbDataSource")
    PagedResources<DbDataSource> findAll();
}
