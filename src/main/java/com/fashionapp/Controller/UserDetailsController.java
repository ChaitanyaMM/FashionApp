package com.fashionapp.Controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.fashionapp.Entity.FileInfo;
import com.fashionapp.Entity.UserDetails;
import com.fashionapp.Repository.FileInfoRepository;
import com.fashionapp.Repository.UserDetailsRepository;
import com.fashionapp.filestorage.FileStorage;
import com.fashionapp.util.PasswordEncryptDecryptor;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping(value = "/userdetails")
@Api(value="UserDetailsController")

public class UserDetailsController {
	Logger log = LoggerFactory.getLogger(this.getClass().getName());

	
	
	@Autowired
	private UserDetailsRepository userDetailsRepository;
	
	@Autowired
	private FileInfoRepository fileInfoRepository;
	
	@Autowired
	FileStorage fileStorage;
	
    @ApiOperation(value = "sampleService")
	@RequestMapping(value = "/sample", method = RequestMethod.GET)
	@ResponseBody
	public String sample() {

		return "Sample!";
	}
	
    @ApiOperation(value = "saving_userdetails",response = UserDetails.class)
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
    
    @ApiOperation(value = "list_of_users",response = UserDetails.class)
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
	
 
	@RequestMapping(value = "/upload", method = RequestMethod.POST, headers = ("content-type=multipart/*"))
	@ResponseBody
	public ResponseEntity<Map<String, Object>> upload(@RequestParam("file") MultipartFile file) throws IOException {
		FileInfo fileInof = new FileInfo();
		//UserDetails userdetails = null;
		Date date = new Date(System.currentTimeMillis());
		fileInof.setDate(date);
		fileInof.setFilename(file.getOriginalFilename());
		//userdetails.setId(id);
		try {
			fileStorage.store(file);
			log.info("File uploaded successfully! -> filename = " + file.getOriginalFilename());
		} catch (Exception e) {
			log.info("Fail! -> uploaded filename: = " + file.getOriginalFilename());
		}
		Resource path =fileStorage.loadFile(file.getOriginalFilename());
		System.out.println("PATH :="+path.toString());
		fileInof.setUrl(path.toString());
		FileInfo fileinserted =fileInfoRepository.save(fileInof);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status",HttpStatus.OK);
		map.put("data", fileinserted);
		return ResponseEntity.ok().body(map);

	}
	
	
	@RequestMapping(value = "/view-files", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> fetchfiles() throws IOException {

		List<FileInfo> files = (List<FileInfo>) fileInfoRepository.findAll();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", HttpStatus.OK);
		map.put("data", files);
		return ResponseEntity.ok().body(map);

	}
	
	
	
	
	
	
	/*@RequestMapping(value = "/view-files", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> fetchfiles() throws IOException {
		
		List<FileInfo> fileInfos = fileStorage.loadFiles().map(
				path ->	{
					String filename = path.getFileName().toString();
					String url = MvcUriComponentsBuilder.fromMethodName(UserDetailsController.class,
	                        "downloadFile", path.getFileName().toString()).build().toString();
					return new FileInfo(filename, url); 
				} 
			)
			.collect(Collectors.toList());
	
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status",HttpStatus.OK);
		map.put("data", fileInfos);
		return ResponseEntity.ok().body(map);
		
		
	}
	
     * Download Files
     
	@GetMapping("/files/{filename}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
		Resource file = fileStorage.loadFile(filename);
		return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
					.body(file);	
	}*/
	
	
	
	
	
	
  /*
    author :Divya sai
	*/
	

    @ApiOperation(value = "user-signup", response = UserDetails.class)
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> usersignup(@RequestBody String data) throws Exception {
		UserDetails userDetails = null;
		try {
			userDetails = new ObjectMapper().readValue(data, UserDetails.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		Date date = new Date(System.currentTimeMillis());
		userDetails.setCreationDate(date);
		userDetails.setPassword(PasswordEncryptDecryptor.encrypt(userDetails.getPassword()));
		if (userDetails.getChangepassword() != null)
			userDetails.setChangepassword(PasswordEncryptDecryptor.encrypt(userDetails.getChangepassword()));
		UserDetails userData = userDetailsRepository.save(userDetails);
		map.put("Data", userData);
		map.put("message", "Successfull !.");
		map.put("status", true);
		return ResponseEntity.ok().body(map);
	}

	@ApiOperation(value = "user-forgotpwd", response = UserDetails.class)
	@RequestMapping(value = "/forgotpwd", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> userforgotpwd(@RequestBody String data) throws Exception {
		UserDetails userDetails = null;
		try {
			userDetails = new ObjectMapper().readValue(data, UserDetails.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		UserDetails userDetailsObj = userDetailsRepository.findByEmail(userDetails.getEmail());
		Map<String, Object> map = new HashMap<String, Object>();
		if (userDetailsObj != null) {
			userDetailsObj.setPassword(PasswordEncryptDecryptor.encrypt(userDetails.getPassword()));
			userDetailsObj.setChangepassword(PasswordEncryptDecryptor.encrypt(userDetails.getChangepassword()));
			Date date = new Date(System.currentTimeMillis());
			userDetailsObj.setCreationDate(date);
			UserDetails userData = userDetailsRepository.save(userDetailsObj);
			map.put("Data", userData);
			map.put("message", "Successfull !.");
			map.put("status", true);
		} else {
			map.put("message", "Invalid User");
			map.put("status", false);
		}
		return ResponseEntity.ok().body(map);
	}

	@ApiOperation(value = "user-login", response = UserDetails.class)
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> userlogin(@RequestBody String data)
			throws Exception {
		
		UserDetails userDetails = null;
		try {
			userDetails = new ObjectMapper().readValue(data, UserDetails.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		UserDetails userDetailsObj = userDetailsRepository.findByEmail(userDetails.getEmail());
		
		Map<String, Object> map = new HashMap<String, Object>();
		if (userDetailsObj != null) {

			String pwd = PasswordEncryptDecryptor.encrypt(userDetails.getPassword());

			if (pwd.equalsIgnoreCase(userDetailsObj.getPassword())) {
				map.put("message", "Login Successfull !.");
				map.put("status", true);
			} else {
				map.put("message", "Invalid Password !.");
				map.put("status", false);
			}

		} else {
			map.put("message", "Invalid User");
			map.put("status", false);
		}
		return ResponseEntity.ok().body(map);
	}
	
	
	
	
	
}
