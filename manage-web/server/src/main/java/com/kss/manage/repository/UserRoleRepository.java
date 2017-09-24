package com.kss.manage.repository;

import com.kss.manage.po.UserRolePo;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "userRoleRepository", path = "userRoleRepository")
public interface UserRoleRepository  extends PagingAndSortingRepository<UserRolePo, Long> {
}
