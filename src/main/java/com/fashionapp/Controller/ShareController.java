package com.fashionapp.Controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.fashionapp.Entity.Share;
import com.fashionapp.service.ShareService;
import com.fashionapp.util.ServerResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
 

@Controller
@RequestMapping(value = "/api/share")
 public class ShareController {

	@Autowired
	private ShareService shareService;
	
	
 	@RequestMapping(value = "/", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> sharefile(@RequestParam("userId") Long userId,
			@RequestParam("fileId") Long fileId, @RequestBody String data) throws IOException, ParseException {
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
		shareObject.setUserId(userId);
		shareObject.setVideoId(fileId);
		Share sharedData = shareService.save(shareObject);
		response = server.getSuccessResponse("Successfully share file", sharedData);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
 	
 	
 	@RequestMapping(value = "/listFiles/{id}", method = RequestMethod.GET)
	@ResponseBody
	public List<Share> getAllFiles(@RequestParam(value="userId") Long userId) {
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
