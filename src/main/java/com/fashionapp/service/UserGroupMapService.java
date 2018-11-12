package com.fashionapp.service;

import java.util.List;

import com.fashionapp.Entity.UserGroupMap;

public interface UserGroupMapService {

	UserGroupMap findByUserIdAndFollowinguserId(Long id, Long followingId);

	List<UserGroupMap> findByUserId(Long id);

	void deleteById(Long id);

	UserGroupMap save(UserGroupMap userGroupMap);

}
