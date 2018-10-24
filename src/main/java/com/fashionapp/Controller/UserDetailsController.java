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

import com.fashionapp.Entity.Comments;
import com.fashionapp.Entity.FileInfo;
import com.fashionapp.Entity.Likes;
import com.fashionapp.Entity.Share;
import com.fashionapp.Entity.UserDetails;
import com.fashionapp.Repository.CommentsRepository;
import com.fashionapp.Repository.FileInfoRepository;
import com.fashionapp.Repository.LikeRepository;
import com.fashionapp.Repository.ShareRepository;
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
	private LikeRepository likeRepository;
	
	@Autowired
	private CommentsRepository commentsRepository;
	
	@Autowired
	private ShareRepository shareRepository;
	
	@Autowired
	FileStorage fileStorage;
	
   

	@ApiOperation(value = "saving_userdetails", response = UserDetails.class)
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

	@ApiOperation(value = "updating userdetails", response = UserDetails.class)
	@RequestMapping(value = "/update-user", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> update(@RequestParam long id, @RequestBody String data)
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

	@ApiOperation(value = "retreieving by userid", response = UserDetails.class)
	@RequestMapping(value = "/find-user-by-id", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> findUser(@RequestParam long id) throws IOException, ParseException {
		Map<String, Object> map = new HashMap<String, Object>();
		Optional<UserDetails> fecthed = userDetailsRepository.findById(id);
		map.put("Data", fecthed);
		map.put("message", "fetched User Details!.");
		map.put("status", true);
		return ResponseEntity.ok().body(map);
	}

	@ApiOperation(value = "delete-user", response = UserDetails.class)
	@RequestMapping(value = "/delete-user", method = RequestMethod.DELETE)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> delete(@RequestParam long id) throws IOException, ParseException {
		Map<String, Object> map = new HashMap<String, Object>();
		userDetailsRepository.deleteById(id);
		map.put("message", "user: " + id + "deleted");
		map.put("status", true);
		return ResponseEntity.ok().body(map);
	}

	@RequestMapping(value = "/uploadvideo", method = RequestMethod.POST, headers = ("content-type=multipart/*"))
	@ResponseBody
	public ResponseEntity<Map<String, Object>> upload(@RequestParam("id") long id,
			@RequestParam("file") MultipartFile file) throws IOException {
		
		FileInfo fileInfo = new FileInfo();
		UserDetails userdetails = new UserDetails();
		Date date = new Date(System.currentTimeMillis());
		fileInfo.setDate(date);
		fileInfo.setFilename(file.getOriginalFilename());
		fileInfo.setUser_id(id);
		userdetails.setId(id);
		try {
			fileStorage.store(file);
			log.info("File uploaded successfully! -> filename = " + file.getOriginalFilename());
		} catch (Exception e) {
			log.info("Fail! -> uploaded filename: = " + file.getOriginalFilename());
		}
		Resource path = fileStorage.loadFile(file.getOriginalFilename());
		System.out.println("PATH :=" + path.toString());
		fileInfo.setUrl(path.toString());
		FileInfo fileinserted = fileInfoRepository.save(fileInfo);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", HttpStatus.OK);
		map.put("data", fileinserted);
		return ResponseEntity.ok().body(map);

	}

	@RequestMapping(value = "/uploadmultiplevideos", method = RequestMethod.POST, headers = ("content-type=multipart/*"))
	@ResponseBody
	public ResponseEntity<Map<String, Object>> uplodVideos(@RequestParam("id") long id,
			@RequestParam("file") MultipartFile[] file) throws IOException {

		FileInfo fileInfo = new FileInfo();
		UserDetails userdetails = new UserDetails();
		Date date = new Date(System.currentTimeMillis());
		fileInfo.setDate(date);

//		MultipartFile data;
		for (MultipartFile files : file) {
			//data = files;
			System.out.println("files<><" + files.getOriginalFilename());
			System.out.println("file!!><" + files);

			fileInfo.setFilename(files.getOriginalFilename());

			fileInfo.setUser_id(id);
			userdetails.setId(id);
			try {
				fileStorage.storemultiple(file);
				log.info("File uploaded successfully! -> filename = " + files.getOriginalFilename());
			} catch (Exception e) {
				log.info("Fail! -> uploaded filename: = " + files.getOriginalFilename());
			}
			Resource path = fileStorage.loadFile(files.getOriginalFilename());
			System.out.println("PATH :=" + path.toString());
			fileInfo.setUrl(path.toString());
		}
		FileInfo fileinserted = fileInfoRepository.save(fileInfo);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", HttpStatus.OK);
		map.put("data", fileinserted);
		return ResponseEntity.ok().body(map);

	}

	@RequestMapping(value = "/view-videos", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> fetchfiles() throws IOException {

		List<FileInfo> files = (List<FileInfo>) fileInfoRepository.findAll();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", HttpStatus.OK);
		map.put("data", files);
		return ResponseEntity.ok().body(map);

	}

	@RequestMapping(value = "/view-videos-by-userId", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> fetchfilesuplodedbyUser(@RequestParam("id") long id) throws IOException {

		List<FileInfo> files = (List<FileInfo>) fileInfoRepository.findByUserid(id);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", HttpStatus.OK);
		map.put("data", files);
		return ResponseEntity.ok().body(map);

	}

	
	@ApiOperation(value = "like_file", response = Likes.class)
	@RequestMapping(value = "/likes", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> fileLike(@RequestParam("userId") long userId,@RequestParam("fileId") long fileId,
			@RequestBody String data) throws IOException, ParseException {
		Likes likesObject = new Likes();

		try {
			likesObject = new ObjectMapper().readValue(data, Likes.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		likesObject.setUserId(userId);
		likesObject.setVideoId(fileId);
		Likes likesData = likeRepository.save(likesObject);
		map.put("Data", likesData);
		map.put("message", "Successfull !.");
		map.put("status", true);
		return ResponseEntity.ok().body(map);
	}

	@ApiOperation(value = "comments_on_file", response = Comments.class)
	@RequestMapping(value = "/comment", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> fileComments(@RequestParam("userId") long userId,@RequestParam("fileId") long fileId,
			@RequestBody String data) throws IOException, ParseException {
		Comments comentsObject = new Comments();
		
		System.out.println("sample");

		try {
			comentsObject = new ObjectMapper().readValue(data, Comments.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
 		Map<String, Object> map = new HashMap<String, Object>();
		comentsObject.setUserId(userId);
		comentsObject.setVideoId(fileId);
		Comments likesData = commentsRepository.save(comentsObject);
		map.put("Data", likesData);
		map.put("message", "Successfull !.");
		map.put("status", true);
		return ResponseEntity.ok().body(map);
	}
	
	
	@ApiOperation(value = "share_file", response = Comments.class)
	@RequestMapping(value = "/share", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> sharefile(@RequestParam("userId") long userId,@RequestParam("fileId") long fileId,
			@RequestBody String data) throws IOException, ParseException {
		Share shareObject = new Share();
		
		System.out.println("sample");

		try {
			shareObject = new ObjectMapper().readValue(data, Share.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
 		Map<String, Object> map = new HashMap<String, Object>();
 		shareObject.setUserId(userId);
 		shareObject.setVideoId(fileId);
		Share likesData = shareRepository.save(shareObject);
		map.put("Data", likesData);
		map.put("message", "Successfull !.");
		map.put("status", true);
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
