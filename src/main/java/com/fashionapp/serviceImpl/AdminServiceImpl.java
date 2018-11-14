package com.fashionapp.serviceImpl;

 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fashionapp.Entity.Admin;
import com.fashionapp.Repository.AdminRepository;
import com.fashionapp.service.AdminService;

@Service
public class AdminServiceImpl implements AdminService {

	@Autowired
	private AdminRepository adminRepository;

	@Override
	public Admin save(Admin admin) {

		return adminRepository.save(admin);
	}

	@Override
	public Iterable<Admin> findAll() {
		Iterable<Admin> fecthed =  adminRepository.findAll();
		return fecthed;
	}

	@Override
	public Admin findByEmail(String email) {
 		return adminRepository.findByEmail(email);
	}

}
