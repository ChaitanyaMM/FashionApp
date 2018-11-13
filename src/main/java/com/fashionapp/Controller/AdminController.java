package com.fashionapp.Controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.websocket.server.PathParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.fashionapp.Entity.Admin;
import com.fashionapp.Entity.BlockedData;
import com.fashionapp.Entity.FileInfo;
import com.fashionapp.Entity.Type;
import com.fashionapp.Entity.UserInfo;
import com.fashionapp.service.AdminService;
import com.fashionapp.service.BlockedDataService;
import com.fashionapp.service.FileInfoService;
import com.fashionapp.service.UserService;
import com.fashionapp.util.ServerResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;
  
@RestController
@RequestMapping(value = "/api/admin")

@Api(value = "admin")
public class AdminController {
	private static final Logger log = LoggerFactory.getLogger(AdminController.class);

	private final UserService userService;
	private final AdminService adminService;
	private final FileInfoService fileInfoService;
	private final BlockedDataService blockedDataService;

	@Autowired
	public AdminController(UserService userService, AdminService adminService, FileInfoService fileInfoService,
			BlockedDataService blockedDataService) {
		this.userService = userService;
		this.adminService = adminService;
		this.fileInfoService = fileInfoService;
		this.blockedDataService = blockedDataService;
	}

	@ApiOperation(value = "Create admin", nickname = "createAdmin",response = Admin.class, notes = "This can only be done by the logged in user.", tags={ "admin", })
    @ApiResponses(value = {  @ApiResponse(code = 200, message = "successful operation") })
    
   @RequestMapping(value = "/", produces = { "application/xml", "application/json" },consumes = { "application/json", "application/xml" },  method = RequestMethod.POST)
 
	public ResponseEntity<Map<String, Object>> addAdmin(@ApiParam(value = "creating admin " ,required=true ) @RequestBody String data)
			throws IOException, ParseException {
		log.info("admin save is calling");
		Admin admin = new Admin();
		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			admin = new ObjectMapper().readValue(data, Admin.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Admin adminData = adminService.save(admin);
		log.info("admin saved!");

		response = server.getSuccessResponse("Uploded Successfully", adminData);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	  @ApiOperation(value = "fetch all admins ", notes = "Number of admins would be listed out !", response = Admin.class, responseContainer = "List" , tags={ "admin", })
		    @ApiResponses(value = {  @ApiResponse(code = 200, message = "successful operation", response = Admin.class, responseContainer = "List"),
		    						 @ApiResponse(code = 400, message = "Invalid tag value") })
	  
    @RequestMapping(value = "/adminList",  produces = { "application/xml", "application/json" },  method = RequestMethod.GET)
 	public ResponseEntity<Map<String, Object>> getAllAdmins() throws IOException, ParseException {
		log.info("adminslist is calling");
		Map<String, Object> map = new HashMap<String, Object>();
		Iterable<Admin> fecthed = adminService.findAll();
		map.put("Data", fecthed);
		map.put("message", "Successfull !.");
		map.put("status", true);
		return ResponseEntity.ok().body(map);
	}

	  @ApiOperation(value = "fetch all users  ", notes = "Number of users would be listed out !", response = UserInfo.class, responseContainer = "List", tags={ "admin", })
	  		 @ApiResponses(value = {  @ApiResponse(code = 200, message = "successful operation", response = UserInfo.class, responseContainer = "List"),
		    						  @ApiResponse(code = 400, message = "Invalid tag value") })  
	  
 	@RequestMapping(value = "/userslist", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getAllUsers() throws IOException, ParseException {
		log.info("userslist is calling");

		Map<String, Object> map = new HashMap<String, Object>();
		Iterable<UserInfo> fecthed = userService.findAll();
		map.put("Data", fecthed);
		map.put("message", "Successfull !.");
		map.put("status", true);
		return ResponseEntity.ok().body(map);
	}

	  @ApiOperation(value = "Find Userby ID", nickname = "getUserById", notes = "Returns a single User", response = UserInfo.class, tags={ "admin", })
		    @ApiResponses(value = {  @ApiResponse(code = 200, message = "successful operation", response = UserInfo.class),
		    						 @ApiResponse(code = 400, message = "Invalid ID supplied"),
		    						 @ApiResponse(code = 404, message = "user not found") })
	  
   @RequestMapping(value = "/findUser/{userId}",  produces = { "application/xml", "application/json" },method = RequestMethod.GET)
 	public ResponseEntity<Map<String, Object>> findUser(@PathVariable("userId") Long userId) throws IOException, ParseException {
		log.info("findUser is calling");
		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		Optional<UserInfo> fecthed = userService.findById(userId);
		response = server.getSuccessResponse("Uploded Successfully", fecthed);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	  @ApiOperation(value = "Delete user", nickname = "deleteUser", notes = "This can only be done by the logged in user.", tags={ "admin", })
	     @ApiResponses(value = {   @ApiResponse(code = 400, message = "Invalid username supplied"),
	    						   @ApiResponse(code = 404, message = "User not found") })  
	    
 	@RequestMapping(value = "/deleteuser/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Map<String, Object>> delete(@ApiParam(value = "The user that needs to be deleted",required=true) @PathVariable("userId") Long id)
			throws IOException, ParseException {
		log.info("deleteuser is calling");
		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		userService.deleteById(id);
		response = server.getSuccessResponse("deleted successfully", null);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	  @ApiOperation(value = "Delete file", nickname = "deleteFile", notes = "This can only be done by the logged in user.", tags={ "admin", })
	     @ApiResponses(value = {   @ApiResponse(code = 400, message = "Invalid filename supplied"),
	    						   @ApiResponse(code = 404, message = "File not found") })  
	@RequestMapping(value = "/deletefile/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> deletefile(@ApiParam(value = "The file that needs to be deleted",required=true) @PathVariable("fileId") Long fileId)
			throws IOException, ParseException {
		log.info("deletefile is calling");
		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		fileInfoService.deleteById(fileId);
		response = server.getSuccessResponse("deleted successfully", "video" + fileId);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	  @ApiOperation(value = "blockuser ", notes = "block a user !", response = UserInfo.class, responseContainer = "List" , tags={ "admin", })
	    @ApiResponses(value = {  @ApiResponse(code = 200, message = "successful operation", response = UserInfo.class, responseContainer = "List"),
	    						 @ApiResponse(code = 400, message = "Invalid tag value") })
	  
	@RequestMapping(value = "/blockuser", method = RequestMethod.GET)
 	public ResponseEntity<Map<String, Object>> blockuserordata(@RequestParam Long userId, @RequestParam Long adminid)
			throws IOException, ParseException {
		log.info("blockuser is calling");
		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();

		BlockedData blockedDetails = new BlockedData();

		Optional<UserInfo> fetchedName = userService.findById(userId);
		blockedDetails.setAdminId(adminid);
		blockedDetails.setUserId(userId);
		blockedDetails.setUserName(fetchedName.get().getUserName());
		blockedDetails.setReason("bad utilizer!");
		blockedDetails.setType(Type.ADMIN);
		blockedDetails.setFileId(0L);

		BlockedData blockeData = null;
		BlockedData fetched = blockedDataService.findByUserId(userId);

		String status = null;
		if (fetched == null) {
			status = "blocked";
			blockeData = blockedDataService.save(blockedDetails);

		} else {
			status = "unblocked";
			blockedDataService.deleteById(fetched.getId());
		}

		response = server.getSuccessResponse(status, blockeData);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	  @ApiOperation(value = "blockfile ", notes = "block a file !", response = UserInfo.class, responseContainer = "List" , tags={ "admin", })
	    @ApiResponses(value = {  @ApiResponse(code = 200, message = "successful operation", response = UserInfo.class, responseContainer = "List"),
	    						 @ApiResponse(code = 400, message = "Invalid tag value") })
	  
	@RequestMapping(value = "/blockfile", method = RequestMethod.GET)
 	public ResponseEntity<Map<String, Object>> blockFile(@RequestParam Long fileid, @RequestParam Long adminid)
			throws IOException, ParseException {
		log.info("blockfile is calling");

		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();

		BlockedData blockedDetails = new BlockedData();
		BlockedData blockeData = null;

		Optional<FileInfo> fetchedFile = fileInfoService.findById(fileid);
		Optional<UserInfo> fetchedUser = userService.findById(fetchedFile.get().getUserId());
		blockedDetails.setAdminId(adminid);
		blockedDetails.setUserId(fetchedFile.get().getUserId());
		blockedDetails.setUserName(fetchedUser.get().getUserName());
		blockedDetails.setFileId(fileid);
		blockedDetails.setReason("bad content!");
		blockedDetails.setType(Type.FILE);
		BlockedData fetched = blockedDataService.findByFileId(fileid);
		String status = null;

		if (fetched == null) {
			status = "blocked";
			blockeData = blockedDataService.save(blockedDetails);

		} else {
			status = "unblocked";
			blockedDataService.deleteById(fetched.getId());
		}

		response = server.getSuccessResponse(status, blockeData);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	/*@ApiOperation(value = "unblockUser ", notes = "block a User  !", response = UserInfo.class, responseContainer = "List" , tags={ "admin", })
	    @ApiResponses(value = {  @ApiResponse(code = 200, message = "successful operation", response = UserInfo.class, responseContainer = "List"),
	    						 @ApiResponse(code = 400, message = "Invalid tag value") })
	@RequestMapping(value = "/unblock", method = RequestMethod.GET)
 	public ResponseEntity<Map<String, Object>> Unblockuserordata(@ApiParam(value = "userId that needs to be unblocked ",required=true) @RequestParam Long userid)
			throws IOException, ParseException {
		log.info("unblock is calling");

		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();

		BlockedData fetchedByUser = blockedDataService.findByUserId(userid);
		if (fetchedByUser == null) {
			response = server.getNotAceptableResponse("No Data found", null);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

		}

		blockedDataService.deleteById(fetchedByUser.getId());

		response = server.getSuccessResponse("User un-blocked", userid);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}*/
 

}
