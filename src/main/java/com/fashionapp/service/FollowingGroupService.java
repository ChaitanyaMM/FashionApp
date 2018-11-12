package com.fashionapp.service;

import java.util.Optional;

import com.fashionapp.Entity.FollowingGroup;

public interface FollowingGroupService {

	Optional<FollowingGroup> findByUserId(Long id);

	FollowingGroup save(FollowingGroup groupData);

}
