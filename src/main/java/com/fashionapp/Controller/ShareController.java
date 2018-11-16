package com.fashionapp.Controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.websocket.server.PathParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fashionapp.Entity.Products;
import com.fashionapp.Entity.Share;
import com.fashionapp.service.ShareService;
import com.fashionapp.util.ServerResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
 

@RestController
@RequestMapping(value = "/api/share")
public class ShareController {
	
	private static final Logger log = LoggerFactory.getLogger(ShareController.class);

	ServerResponse<Object> server = new ServerResponse<Object>();
	Map<String, Object> response = new HashMap<String, Object>();

	@Autowired
	private ShareService shareService;
	
	@ApiOperation(value = "ShareFIle", nickname = "ShareFIle",response = Share.class, notes = "This can only be done by the logged in user.")
    @ApiResponses(value = {  @ApiResponse(code = 200, message = "successful operation"),
    		 				 @ApiResponse(code = 400, message = "Invalid  Data") })
 	@RequestMapping(value = "/", method = RequestMethod.POST)
	 
	public ResponseEntity<Map<String, Object>> sharefile(@RequestParam("userId") Long userId,
			@RequestParam("fileId") Long fileId, @RequestBody String data) throws IOException, ParseException {
		Share shareObject = new Share();
		
		System.out.println("sample");

		try {
			shareObject = new ObjectMapper().readValue(data, Share.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
 		shareObject.setUserId(userId);
		shareObject.setVideoId(fileId);
		Share sharedData = shareService.save(shareObject);
		response = server.getSuccessResponse("Successfully share file", sharedData);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
 	
	@ApiOperation(value = "ShareFIle", nickname = "ShareFIle",response = Share.class, notes = "This can only be done by the logged in user.")
    @ApiResponses(value = {  @ApiResponse(code = 200, message = "successful operation"),
    		 				 @ApiResponse(code = 400, message = "Invalid  Data") })
 	@RequestMapping(value = "/sharedList/{userId}", method = RequestMethod.GET)
	 
	public List<Share> getAllFiles(@PathParam(value="userId") Long userId) {
	    return shareService.findByUserId(userId);
	}
 	
 	/*@ApiOperation(value = "share_file", response = Comments.class)
	@RequestMapping(value = "/share", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> sharefile(@RequestParam("userId") long userId,
			@RequestParam("fileId") long fileId,@RequestParam("sharedId") long sharedId) 
					throws IOException, ParseException {
		
		Share shareObject = new Share();
		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		System.out.println("sample");

		try {
			shareObject = new ObjectMapper().readValue(data, Share.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		
		Optional<UserDetails> userData=userDetailsRepository.findById(userId);
		Optional<FileInfo> fileInfo=fileInfoRepository.findById(fileId);
		shareObject.setEmail(userData.get().getEmail());
		shareObject.setUserId(userId);
		shareObject.setVideoId(fileId);
		shareObject.setSharedId(sharedId);
		shareObject.setFilename(fileInfo.get().getFilename());
		Share sharedData = shareRepository.save(shareObject);

		response = server.getSuccessResponse("Successfully share file", sharedData);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}*/
	
	
}
