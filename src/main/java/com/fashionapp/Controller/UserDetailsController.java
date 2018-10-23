package com.fashionapp.Controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
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

import com.fashionapp.Entity.User;
import com.fashionapp.Entity.UserDetails;
import com.fashionapp.Repository.UserDetailsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping(value = "/userdetails")
@Api(value="UserDetailsController")

public class UserDetailsController {
	
	@Autowired
	private UserDetailsRepository userDetailsRepository;
	
    @ApiOperation(value = "sampleService")
	@RequestMapping(value = "/sample", method = RequestMethod.GET)
	@ResponseBody
	public String sample() {

		return "Sample!";
	}
	
    @ApiOperation(value = "saving userdetails",response = UserDetails.class)
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> Createsection(@RequestBody String data)
			throws IOException, ParseException {
		UserDetails userdetails = new UserDetails();
		try {
			userdetails = new ObjectMapper().readValue(data, UserDetails.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map<String, Object> map = new HashMap<String, Object>();
 	    UserDetails userDetailsData = userDetailsRepository.save(userdetails);
		map.put("Data", userDetailsData);
		map.put("message", "Successfull !.");
		map.put("status", true);
		return ResponseEntity.ok().body(map);
	}
    
    @ApiOperation(value = "list of users",response = UserDetails.class)
	@RequestMapping(value = "/getusers", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getAll()
			throws IOException, ParseException {
		Map<String, Object> map = new HashMap<String, Object>();
 	    Iterable<UserDetails> fecthed = userDetailsRepository.findAll();
		map.put("Data", fecthed);
		map.put("message", "Successfull !.");
		map.put("status", true);
		return ResponseEntity.ok().body(map);
	}
	
    @ApiOperation(value = "updating userdetails",response = UserDetails.class)
	@RequestMapping(value = "/update-user", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> update(@RequestParam Long id,@RequestBody String data)
			throws IOException, ParseException {
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

    @ApiOperation(value = "retreieving by userid",response = UserDetails.class)
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
    
    @ApiOperation(value = "delete-user",response = UserDetails.class)
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
	
	

	
    @ApiOperation(value = "user-signup",response = UserDetails.class)
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> usersignup(@RequestBody String data)
			throws IOException, ParseException {
		UserDetails userDetails = null;
		try {
			userDetails = new ObjectMapper().readValue(data, UserDetails.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		Date date = new Date(System.currentTimeMillis());
        userDetails.setCreationDate(date);
		UserDetails userData = userDetailsRepository.save(userDetails);
		map.put("Data", userData);
		map.put("message", "Successfull !.");
		map.put("status", true);
		return ResponseEntity.ok().body(map);
	}
	
	

	
	
}
