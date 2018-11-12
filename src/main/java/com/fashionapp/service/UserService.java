package com.fashionapp.service;

import java.util.Optional;

import com.fashionapp.Entity.UserInfo;

public interface UserService {

	Iterable<UserInfo> findAll();

	Optional<UserInfo> findById(Long id);

	void deleteById(Long id);

	UserInfo findByEmail(String email);

	UserInfo save(UserInfo userDetails);

}
