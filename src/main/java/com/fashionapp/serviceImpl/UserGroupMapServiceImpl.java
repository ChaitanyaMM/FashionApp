package com.fashionapp.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fashionapp.Entity.UserGroupMap;
import com.fashionapp.Repository.UserGroupMapRepository;
import com.fashionapp.service.UserGroupMapService;

@Service
public class UserGroupMapServiceImpl implements UserGroupMapService {
	
	@Autowired
	private UserGroupMapRepository userGroupMapRepository;

	@Override
	public UserGroupMap findByUserIdAndFollowinguserId(Long id, Long followingId) {
		return userGroupMapRepository.findByUserIdAndFollowinguserId(id, followingId);
	}

	@Override
	public List<UserGroupMap> findByUserId(Long id) {
		return userGroupMapRepository.findByUserId(id);
	}

	@Override
	public void deleteById(Long id) {
		userGroupMapRepository.deleteById(id);

	}

	@Override
	public UserGroupMap save(UserGroupMap userGroupMap) {
		return userGroupMapRepository.save(userGroupMap);
	}

}
