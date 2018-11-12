package com.fashionapp.Repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import com.fashionapp.Entity.FollowingGroup;


public interface FollowingGroupRepository extends CrudRepository<FollowingGroup, Long>{

	Optional<FollowingGroup> findByUserId(Long userId);

	Optional<FollowingGroup> findByUserIdAndId(Long userId, Long id);

}
