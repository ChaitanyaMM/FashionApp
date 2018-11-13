package com.fashionapp.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fashionapp.Entity.FollowersGroup;
import com.fashionapp.Repository.FollowersGroupRepository;
import com.fashionapp.service.FollowersGroupService;

@Service
public class FollowersGroupServiceImpl implements FollowersGroupService {
	
	@Autowired
	private FollowersGroupRepository followersGroupRepository;

	@Override
	public FollowersGroup save(FollowersGroup groupData) {
 		return followersGroupRepository.save(groupData);
	}

	@Override
	public void deleteByUserId(Long userId) {
		followersGroupRepository.deleteByUserId(userId);
		
	}

}
