package com.fashionapp.serviceImpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fashionapp.Entity.FollowingGroup;
import com.fashionapp.Repository.FollowingGroupRepository;
import com.fashionapp.service.FollowingGroupService;

@Service
public class FollowingGroupServiceImpl implements FollowingGroupService {
	
	@Autowired
	private FollowingGroupRepository followingGroupRepository;

	@Override
	public Optional<FollowingGroup> findByUserId(Long id) {
 		return followingGroupRepository.findByUserId(id);
	}

	@Override
	public FollowingGroup save(FollowingGroup groupData) {
 		return followingGroupRepository.save(groupData);
	}

	@Override
	public void deleteByUserId(Long userId) {
		followingGroupRepository.deleteByUserId(userId);
		
	}

}
