package com.fashionapp.Controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fashionapp.Entity.Admin;
import com.fashionapp.Entity.BlockedData;
import com.fashionapp.Entity.FileInfo;
import com.fashionapp.Entity.UserInfo;
import com.fashionapp.Repository.AdminRepository;
import com.fashionapp.Repository.BlockedDataRepository;
import com.fashionapp.Repository.FileInfoRepository;
import com.fashionapp.Repository.UserDetailsRepository;
import com.fashionapp.util.ServerResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping(value = "/admin")
@Api(value="AdminController")
public class AdminController {
	Logger log = LoggerFactory.getLogger(this.getClass().getName());

	@Autowired
	private UserDetailsRepository userDetailsRepository;
	@Autowired
	private AdminRepository adminRepository;
	@Autowired
	private FileInfoRepository fileInfoRepository;
	@Autowired
	private BlockedDataRepository blockedDataRepository;
	
/*	@RequestMapping(value="/sample")
	@ResponseBody
	public String sample() {
		
		return "Sample!";
	}*/
	@ApiOperation(value = "admin-creation", response = Admin.class)
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> Createsection(@RequestBody String data)
			throws IOException, ParseException {
		System.out.println("admin save is calling");
		Admin admin = new Admin();
		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			admin = new ObjectMapper().readValue(data, Admin.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		admin.setIsactive(true);
		Admin adminData = adminRepository.save(admin);
		response = server.getSuccessResponse("Uploded Successfully", adminData);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@ApiOperation(value = "list_of_users", response = UserInfo.class)
	@RequestMapping(value = "/getusers", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getAll() throws IOException, ParseException {
		Map<String, Object> map = new HashMap<String, Object>();
		Iterable<UserInfo> fecthed = userDetailsRepository.findAll();
		map.put("Data", fecthed);
		map.put("message", "Successfull !.");
		map.put("status", true);
		return ResponseEntity.ok().body(map);
	}
	
	@ApiOperation(value = "retreieving by userid", response = UserInfo.class)
	@RequestMapping(value = "/find-user-by-id", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> findUser(@RequestParam long id) throws IOException, ParseException {
		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		Optional<UserInfo> fecthed = userDetailsRepository.findById(id);
		response = server.getSuccessResponse("Uploded Successfully", fecthed);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "delete-user", response = UserInfo.class)
	@RequestMapping(value = "/delete-user", method = RequestMethod.DELETE)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> delete(@RequestParam long id) throws IOException, ParseException {
		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		userDetailsRepository.deleteById(id);
		response = server.getSuccessResponse("deleted successfully", null);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	@ApiOperation(value = "delete-file-by-id", response = FileInfo.class)
	@RequestMapping(value = "/delete-file", method = RequestMethod.DELETE)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> deletefile(@RequestParam long id) throws IOException, ParseException {
		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		fileInfoRepository.deleteById(id);
		response = server.getSuccessResponse("deleted successfully", "video"+ id );
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	
	@ApiOperation(value = "block", response = BlockedData.class)
	@RequestMapping(value = "/block-user", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> blockuserordata(@RequestParam long id,@RequestParam long adminid, @RequestBody String data)
			throws IOException, ParseException {
		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();

		BlockedData blockedDetails = null;
		try {
			blockedDetails = new ObjectMapper().readValue(data, BlockedData.class);
		} catch (Exception e) {

		}

		Optional<UserInfo> fetchedUser =userDetailsRepository.findById(id);
		Optional<Admin>  fecthedAdmin = adminRepository.findById(adminid);
		//Optional<FileInfo> fetchedFile =fileInfoRepository.findById(fetchedUser.get)
		blockedDetails.setAdminId(adminid);
		blockedDetails.setUserId(id);
		blockedDetails.setUserName(fetchedUser.get().getUsername());
		blockedDetails.setAdminName(fecthedAdmin.get().getUsername());
		//blockedDetails.setVideoId(fetchedUser.get().get);
		//blockedDetails.setReason("invalid Data");
		
		//Optional<FileInfo> fetchedFile =fileInfoRepository.findById(id);
		
		
		
		
		BlockedData blockeData = blockedDataRepository.save(blockedDetails);

		response = server.getSuccessResponse("User have been blocked" , blockeData);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	@ApiOperation(value = "block", response = BlockedData.class)
	@RequestMapping(value = "/block-file", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> blockFile(@RequestParam("id") long fileid,@RequestParam long adminid, @RequestBody String data)
			throws IOException, ParseException {
		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();

		BlockedData blockedDetails = null;
		try {
			blockedDetails = new ObjectMapper().readValue(data, BlockedData.class);
		} catch (Exception e) {

		}

		Optional<FileInfo> fetchedFile =fileInfoRepository.findById(fileid);
		Optional<UserInfo> fetchedUser =userDetailsRepository.findById(fetchedFile.get().getUser_id());

		Optional<Admin>  fecthedAdmin = adminRepository.findById(adminid);
		blockedDetails.setAdminId(adminid);
		blockedDetails.setUserId(fetchedFile.get().getUser_id());
		blockedDetails.setUserName(fetchedUser.get().getUsername());
		blockedDetails.setAdminName(fecthedAdmin.get().getUsername());
        blockedDetails.setVideoId(fileid);
        
		//Optional<FileInfo> fetchedFile =fileInfoRepository.findById(id);
		
		
		
		
		BlockedData blockeData = blockedDataRepository.save(blockedDetails);

		response = server.getSuccessResponse("file have been blocked" , blockeData);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	

	@ApiOperation(value = "unblock", response = BlockedData.class)
	@RequestMapping(value = "/unblock", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> Unblockuserordata(@RequestParam long userid)
			throws IOException, ParseException {
		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();

		BlockedData fetchedByUser = blockedDataRepository.findByUserId(userid);
		if (fetchedByUser == null) {
			response = server.getNotAceptableResponse("No Data found", null);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

		}

		blockedDataRepository.deleteById(fetchedByUser.getId());

		response = server.getSuccessResponse("User un-blocked", userid);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
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
