package com.fashionapp.service;

import com.fashionapp.Entity.Admin;

public interface AdminService {

	Admin save(Admin admin);

	Iterable<Admin> findAll();

	Admin findByEmail(String email);

}
