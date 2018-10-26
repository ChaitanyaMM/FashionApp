package com.fashionapp.service;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.fashionapp.Entity.UserInfo;
import com.fashionapp.Repository.UserDetailsRepository;
import com.fashionapp.util.PasswordEncryptDecryptor;

@Service("userService")
public class UserDetailsServiceImpl implements UserDetailsService{
	
	@Autowired
	UserDetailsRepository userDetailsRepository;
	
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		UserInfo userInfo =  userDetailsRepository.findByEmail(username);
		if(userInfo == null) {
			throw new UsernameNotFoundException("Invalid Credentials");
		}
		GrantedAuthority authority = new SimpleGrantedAuthority(userInfo.getRole());
		Collection<GrantedAuthority> authorities = new ArrayList<>();
		String pwd = null;
		authorities.add(authority);
		UserDetails userDetails = new User(userInfo.getEmail(), userInfo.getPassword() , authorities);
		return userDetails;
	}

}
