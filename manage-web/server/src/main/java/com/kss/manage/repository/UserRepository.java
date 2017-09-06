package com.kss.manage.repository;

import com.kss.manage.po.UserPo;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "userRepository", path = "userRepository")
public interface UserRepository extends PagingAndSortingRepository<UserPo, Long> {
}
