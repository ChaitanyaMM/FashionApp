package com.fashionapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.fashionapp.Repository.UserDetailsRepository;


@Service("userService")
public class UserDetailsServiceImpl implements UserDetailsService{
	
	@Autowired
	UserDetailsRepository userDetailsRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}
	
	
/*	
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
	}*/

}
