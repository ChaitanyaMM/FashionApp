package com.fashionapp.Repository;

import org.springframework.data.repository.CrudRepository;

import com.fashionapp.Entity.UserGroupMap;

public interface UserGroupMapRepository extends CrudRepository<UserGroupMap, Long>{

	UserGroupMap findByUserId(long userId);

}
