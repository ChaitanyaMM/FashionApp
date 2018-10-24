package com.fashionapp.Controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fashionapp.Entity.UserDetails;
import com.fashionapp.Repository.UserDetailsRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping(value = "/admin")
@Api(value="AdminController")
public class AdminController {
	
	@Autowired
	private UserDetailsRepository userDetailsRepository;
	
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

}
