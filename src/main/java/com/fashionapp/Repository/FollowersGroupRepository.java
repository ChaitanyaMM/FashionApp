package com.fashionapp.Repository;

import org.springframework.data.repository.CrudRepository;

import com.fashionapp.Entity.FollowersGroup;
import com.fashionapp.Entity.FollowingGroup;

public interface FollowersGroupRepository extends CrudRepository<FollowersGroup,Long> {

	FollowingGroup findByUserIdAndId(Long userId, Long id);

	void deleteByUserId(Long userId);

}
