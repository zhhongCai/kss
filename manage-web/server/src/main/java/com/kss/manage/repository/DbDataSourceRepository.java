package com.kss.manage.repository;

import com.kss.manage.po.DbDataSource;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "dbDataSource", path = "dbDataSource")
public interface DbDataSourceRepository extends PagingAndSortingRepository<DbDataSource, Long> {

    List<DbDataSource> findAll();
}
