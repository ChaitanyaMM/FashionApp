package com.fashionapp.serviceImpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fashionapp.Entity.UserInfo;
import com.fashionapp.Repository.UserDetailsRepository;
import com.fashionapp.service.UserService;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserDetailsRepository userDetailsRepository;

	@Override
	public Iterable<UserInfo> findAll() {
	 
		return userDetailsRepository.findAll();
	}

	@Override
	public Optional<UserInfo> findById(Long id) {
 		return userDetailsRepository.findById(id);
	}

	@Override
	public void deleteById(Long id) {
 		userDetailsRepository.deleteById(id);
 		return;
		
	}

	@Override
	public UserInfo findByEmail(String email) {
 		return userDetailsRepository.findByEmail(email);
	}

	@Override
	public UserInfo save(UserInfo userDetails) {
 		return userDetailsRepository.save(userDetails);
	}

}
