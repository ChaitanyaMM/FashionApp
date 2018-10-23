//package com.fashionapp.Controller;
//
//import java.io.IOException;
//import java.text.ParseException;
//import java.util.HashMap;
//import java.util.Map;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.ResponseBody;
//import com.fashionapp.Entity.UserDetails;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//
//@Controller
//@RequestMapping(value = "/user")
//@Api(value="UserController")
//public class UserController {
//	
//	 @Autowired
////	 private UserRepository userRepository;
//	 
//	    @ApiOperation(value = "user-signup",response = UserDetails.class)
//		@RequestMapping(value = "/signup", method = RequestMethod.POST)
//		@ResponseBody
//		public ResponseEntity<Map<String, Object>> Createsection(@RequestBody String data)
//				throws IOException, ParseException {
//			User user = new User();
//			try {
//				user = new ObjectMapper().readValue(data, User.class);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			Map<String, Object> map = new HashMap<String, Object>();
//	 	    User userData = userRepository.save(user);
//			map.put("Data", userData);
//			map.put("message", "Successfull !.");
//			map.put("status", true);
//			return ResponseEntity.ok().body(map);
//		}
//	    
//	    @ApiOperation(value = "user signup",response = UserDetails.class)
//		@RequestMapping(value = "/signup", method = RequestMethod.POST)
//		@ResponseBody
//		public ResponseEntity<Map<String, Object>> userlogin(@RequestBody String data)
//				throws IOException, ParseException {
//			User user = new User();
//			try {
//				user = new ObjectMapper().readValue(data, User.class);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			Map<String, Object> map = new HashMap<String, Object>();
//	 	    User userData = userRepository.save(user);
//			map.put("Data", userData);
//			map.put("message", "Successfull !.");
//			map.put("status", true);
//			return ResponseEntity.ok().body(map);
//		}
//	    @ApiOperation(value = "user signup",response = UserDetails.class)
//		@RequestMapping(value = "/signup", method = RequestMethod.POST)
//		@ResponseBody
//		public ResponseEntity<Map<String, Object>> userlogout(@RequestBody String data)
//				throws IOException, ParseException {
//			User user = new User();
//			try {
//				user = new ObjectMapper().readValue(data, User.class);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			Map<String, Object> map = new HashMap<String, Object>();
//	 	    User userData = userRepository.save(user);
//			map.put("Data", userData);
//			map.put("message", "Successfull !.");
//			map.put("status", true);
//			return ResponseEntity.ok().body(map);
//		}
//	
//	
//
//}
