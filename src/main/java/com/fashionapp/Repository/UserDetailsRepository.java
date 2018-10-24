package com.fashionapp.Repository;

import org.springframework.data.repository.CrudRepository;

import com.fashionapp.Entity.UserDetails;

public interface UserDetailsRepository extends CrudRepository<UserDetails, Long>{

	UserDetails findByEmail(String email);
	
	
  

}
