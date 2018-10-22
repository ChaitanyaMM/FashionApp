package com.fashionapp.Controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fashionapp.Entity.UserDetails;
import com.fashionapp.Repository.UserDetailsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping(value = "/user")

public class UserController {
	
	@Autowired
	private UserDetailsRepository userDetailsRepository;

	@RequestMapping(value = "/sample")
	@ResponseBody
	public String sample() {

		return "Sample!";
	}
	
	
	@RequestMapping(value = "/insert")
	@ResponseBody
	public String save() {

		return "Sample!";
	}
	
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> Createsection(@RequestBody String data)
			throws IOException, ParseException {
		System.out.println("user save service is calling!.");
		UserDetails userdetails = new UserDetails();
		try {
			userdetails = new ObjectMapper().readValue(data, UserDetails.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map<String, Object> map = new HashMap<String, Object>();
 	    UserDetails fecthed = userDetailsRepository.save(userdetails);
		map.put("Data", fecthed);
		map.put("message", "Successfull !.");
		map.put("status", true);
		return ResponseEntity.ok().body(map);
	}
	
	@RequestMapping(value = "/getusers", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getAll()
			throws IOException, ParseException {
		System.out.println("user get service is calling!.");
		
		Map<String, Object> map = new HashMap<String, Object>();
 	    Iterable<UserDetails> fecthed = userDetailsRepository.findAll();
		map.put("Data", fecthed);
		map.put("message", "Successfull !.");
		map.put("status", true);
		return ResponseEntity.ok().body(map);
	}
	
	@RequestMapping(value = "/update-user", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> update(@RequestParam Long id,@RequestBody String data)
			throws IOException, ParseException {
		System.out.println("user get service is calling!.");
		UserDetails userdetails = null;
		try {
			userdetails = new ObjectMapper().readValue(data, UserDetails.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		userdetails.setId(id);
 	    UserDetails fecthed = userDetailsRepository.save(userdetails);
		map.put("Data", fecthed);
		map.put("message", "updated Successfully!.");
		map.put("status", true);
		return ResponseEntity.ok().body(map);
	}

	@RequestMapping(value = "/find-user-by-id", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> findUser(@RequestParam Long id)
			throws IOException, ParseException {
		Map<String, Object> map = new HashMap<String, Object>();
 	    Optional<UserDetails> fecthed = userDetailsRepository.findById(id);
		map.put("Data", fecthed);
		map.put("message", "fetched User Details!.");
		map.put("status", true);
		return ResponseEntity.ok().body(map);
	}
	
	@RequestMapping(value = "/delete-user", method = RequestMethod.DELETE)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> delete(@RequestParam Long id)
			throws IOException, ParseException {
		
		Map<String, Object> map = new HashMap<String, Object>();
        userDetailsRepository.deleteById(id);
		map.put("message", "user: "+id + "deleted");
		map.put("status", true);
		return ResponseEntity.ok().body(map);
	}
	
	

	
	
	
	

	
	
}
