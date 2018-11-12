package com.fashionapp.Repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.fashionapp.Entity.UserInfo;

public interface UserDetailsRepository extends CrudRepository<UserInfo, Long>,PagingAndSortingRepository<UserInfo, Long>{

	UserInfo findByEmail(String email);
	UserInfo findByUserName(String username);
	UserInfo findByUserNameAndPhoneNo(String username ,String phoneno);
	
	
  

}
