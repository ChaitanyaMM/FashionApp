package com.fashionapp.Controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fashionapp.Entity.Admin;
import com.fashionapp.Entity.UserDetails;
import com.fashionapp.Repository.AdminRepository;
import com.fashionapp.Repository.UserDetailsRepository;
import com.fashionapp.util.ServerResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping(value = "/admin")
@Api(value="AdminController")
public class AdminController {
	
	@Autowired
	private UserDetailsRepository userDetailsRepository;
	@Autowired
	private AdminRepository adminRepository;
	
	@ApiOperation(value = "admin-creation", response = Admin.class)
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> Createsection(@RequestBody String data)
			throws IOException, ParseException {
		Admin admin = new Admin();
		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			admin = new ObjectMapper().readValue(data, Admin.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Admin adminData = adminRepository.save(admin);
		response = server.getSuccessResponse("Uploded Successfully", adminData);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@ApiOperation(value = "list_of_users", response = UserDetails.class)
	@RequestMapping(value = "/getusers", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getAll() throws IOException, ParseException {
		Map<String, Object> map = new HashMap<String, Object>();
		Iterable<UserDetails> fecthed = userDetailsRepository.findAll();
		map.put("Data", fecthed);
		map.put("message", "Successfull !.");
		map.put("status", true);
		return ResponseEntity.ok().body(map);
	}
	
	
	
	/*
	 * TO-DO:
	 * 
	    1.admin can block user
		2.admin can block data
		3.admin can uplod files
		4.admin can update files
		5.admin can delete user
		6.admin can delete data
	
	*/

	
	
	
	

}
