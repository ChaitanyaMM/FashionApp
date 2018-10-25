package com.fashionapp.Controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.fashionapp.Entity.Comments;
import com.fashionapp.Entity.FileInfo;
import com.fashionapp.Entity.FollowersGroup;
import com.fashionapp.Entity.FollowingGroup;
import com.fashionapp.Entity.Likes;
import com.fashionapp.Entity.Share;
import com.fashionapp.Entity.UserDetails;
import com.fashionapp.Entity.UserGroupMap;
import com.fashionapp.Repository.CommentsRepository;
import com.fashionapp.Repository.FileInfoRepository;
import com.fashionapp.Repository.FollowersGroupRepository;
import com.fashionapp.Repository.FollowingGroupRepository;
import com.fashionapp.Repository.LikeRepository;
import com.fashionapp.Repository.ShareRepository;
import com.fashionapp.Repository.UserDetailsRepository;
import com.fashionapp.Repository.UserGroupMapRepository;
import com.fashionapp.filestorage.FileStorage;
import com.fashionapp.util.PasswordEncryptDecryptor;
import com.fashionapp.util.ServerResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping(value = "/userdetails")
@Api(value = "UserDetailsController")

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

	@Autowired
	private FollowingGroupRepository followingGroupRepository;

	@Autowired
	private FollowersGroupRepository followersGroupRepository;

	@Autowired
	private UserGroupMapRepository userGroupMapRepository;

	@ApiOperation(value = "user-signup", response = UserDetails.class)
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> usersignup(@RequestParam("data") String data,
			@RequestParam("file") MultipartFile profileImage) throws Exception {

		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		UserDetails userDetails = null;
		try {
			userDetails = new ObjectMapper().readValue(data, UserDetails.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*
		 * try { fileStorage.store(profileImage);
		 * log.info("File uploaded successfully! -> filename = " +
		 * profileImage.getOriginalFilename()); } catch (Exception e) {
		 * log.info("Fail! -> uploaded filename: = " +
		 * profileImage.getOriginalFilename()); } Resource path =
		 * fileStorage.loadFile(profileImage.getOriginalFilename());
		 * System.out.println("PATH :=" + path.toString());
		 * userDetails.setImagepath(path.toString());
		 */

		UserDetails isemailExists = userDetailsRepository.findByEmail(userDetails.getEmail());
		if (isemailExists != null) {
			log.info("Email Id already exists, please choose another email id");
			response = server.getDuplicateResponse("Email Id already exists, please choose another email id", null);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CONFLICT);
		}

		byte[] image = profileImage.getBytes();
		userDetails.setImage(image);
		UserDetails userData = userDetailsRepository.save(userDetails);
		System.out.println("creating default group");
		DefaultfollowingGroup(userDetails.getId(), userDetails.getEmail());
		DefaultfollowersGroup(userDetails.getId(), userDetails.getEmail());
		response = server.getSuccessResponse("SignUp Successful", userData);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@ApiOperation(value = "user-login", response = UserDetails.class)
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> userlogin(@RequestBody String data) throws Exception {

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
		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			userdetails = new ObjectMapper().readValue(data, UserDetails.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		userdetails.setId(id);
		UserDetails fecthed = userDetailsRepository.save(userdetails);
		response = server.getSuccessResponse("SignUp Successful", fecthed);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "retreieving by userid", response = UserDetails.class)
	@RequestMapping(value = "/find-user-by-id", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> findUser(@RequestParam long id) throws IOException, ParseException {
		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		Optional<UserDetails> fecthed = userDetailsRepository.findById(id);
		response = server.getSuccessResponse("Uploded Successfully", fecthed);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "delete-user", response = UserDetails.class)
	@RequestMapping(value = "/delete-user", method = RequestMethod.DELETE)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> delete(@RequestParam long id) throws IOException, ParseException {
		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		userDetailsRepository.deleteById(id);
		response = server.getSuccessResponse("deleted successfully", null);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/uploadvideo", method = RequestMethod.POST, headers = ("content-type=multipart/*"))
	@ResponseBody
	public ResponseEntity<Map<String, Object>> upload(@RequestParam("id") long id,
			@RequestParam("file") MultipartFile file) throws IOException {
		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();
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
		response = server.getSuccessResponse("Uploded Successfully", fileinserted);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

	}

	@RequestMapping(value = "/uploadmultiplevideos", method = RequestMethod.POST, headers = ("content-type=multipart/*"))
	@ResponseBody
	public ResponseEntity<Map<String, Object>> uplodVideos(@RequestParam("id") long id,
			@RequestParam("file") MultipartFile[] file) throws IOException {

		FileInfo fileInfo = new FileInfo();
		UserDetails userdetails = new UserDetails();
		Date date = new Date(System.currentTimeMillis());
		fileInfo.setDate(date);

		// MultipartFile data;
		for (MultipartFile files : file) {
			// data = files;
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
		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		List<FileInfo> files = (List<FileInfo>) fileInfoRepository.findAll();
		response = server.getSuccessResponse("fetched", files);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

	}

	@RequestMapping(value = "/view-videos-by-userId", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> fetchfilesuplodedbyUser(@RequestParam("id") long id) throws IOException {
		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		List<FileInfo> files = (List<FileInfo>) fileInfoRepository.findByUserid(id);
		response = server.getSuccessResponse("fetched", files);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

	}

	@ApiOperation(value = "like_file", response = Likes.class)
	@RequestMapping(value = "/likes", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> fileLike(@RequestParam("userId") long userId,
			@RequestParam("fileId") long fileId, @RequestBody String data) throws IOException, ParseException {
		Likes likesObject = new Likes();
		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			likesObject = new ObjectMapper().readValue(data, Likes.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		likesObject.setUserId(userId);
		likesObject.setVideoId(fileId);
		Likes likesData = likeRepository.save(likesObject);
		response = server.getSuccessResponse("liked", likesData);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "comment", response = Comments.class)
	@RequestMapping(value = "/comment", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> fileComments(@RequestParam("userId") long userId,
			@RequestParam("fileId") long fileId, @RequestBody String data) throws IOException, ParseException {
		Comments comentsObject = new Comments();
		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();

		try {
			comentsObject = new ObjectMapper().readValue(data, Comments.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		comentsObject.setUserId(userId);
		comentsObject.setVideoId(fileId);
		Comments commentsData = commentsRepository.save(comentsObject);
		response = server.getSuccessResponse("commented", commentsData);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "share_file", response = Comments.class)
	@RequestMapping(value = "/share", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> sharefile(@RequestParam("userId") long userId,
			@RequestParam("fileId") long fileId, @RequestBody String data) throws IOException, ParseException {
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
		Share sharedData = shareRepository.save(shareObject);
		response = server.getSuccessResponse("Successfully share file", sharedData);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/follow", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> followUser(@RequestParam("id") long id) {
		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		Optional<FollowingGroup> userData = followingGroupRepository.findById(id);

		System.out.println("userGroup..." + userData.get().getUserId());
		System.out.println("userEmail..." + userData.get().getUseremail());

		addUsertoUsergroup(id, userData.get().getUserId(), userData.get().getUseremail());
		log.info("following a user");
		response = server.getSuccessResponse("following-user", null);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/unfollow", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> unfollowUser(@RequestParam("userId") long userId) {
		Optional<UserDetails> userData = userDetailsRepository.findById(userId);
		Map<String, Object> response = new HashMap<String, Object>();
		ServerResponse<Object> server = new ServerResponse<Object>();
		response = server.getSuccessResponse("unfollowed-Successfull", userData.get().getNickName());
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	public ResponseEntity<Map<String, Object>> addUsertoUsergroup(long userId, long groupId, String email) {
		log.info("to follow user");
		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();

		UserGroupMap userGroupMap = new UserGroupMap();
		userGroupMap.setGroupId(groupId);
		userGroupMap.setUserId(userId);
		// userGroupMap.setUseremail(useremail);
		// userGroupMap.setPhoneno(phoneno);
		// userGroupMap.setUsername(username);
		userGroupMap.setMapped(true);

		UserGroupMap usermappedData = userGroupMapRepository.save(userGroupMap);
		log.info("following a user");
		response = server.getSuccessResponse("user-added-to-group", usermappedData);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);

	}

	public ResponseEntity<Map<String, Object>> unmapuserfromUsergroup(long userId) {

		/*
		 * TO:DO
		 * 
		 * step:1 find out user from any group by id step:2 get the groupmap id from
		 * userid and delete it from group table
		 */

		UserGroupMap unmapuserData = userGroupMapRepository.findByUserId(userId);

		userGroupMapRepository.deleteById(unmapuserData.getId());

		log.info("following a user");
		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		response = server.getSuccessResponse("un-mapped", null);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

	}

	public ResponseEntity<Map<String, Object>> DefaultfollowingGroup(long userId, String email) {
		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		FollowingGroup groupData = new FollowingGroup();
		groupData.setUserId(userId);
		groupData.setUseremail(email);
		groupData.setGroupname("following");
		groupData.setDefault(true);
		FollowingGroup usergroup = followingGroupRepository.save(groupData);
		log.info(" default followingGroup created");
		response = server.getSuccessResponse("default-group-created", usergroup);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	public ResponseEntity<Map<String, Object>> DefaultfollowersGroup(long userId, String email) {
		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		FollowersGroup groupData = new FollowersGroup();
		groupData.setUserId(userId);
		groupData.setUseremail(email);
		groupData.setGroupname("followers");
		groupData.setDefault(true);
		FollowersGroup usergroup = followersGroupRepository.save(groupData);
		log.info(" default followersGroup created");
		response = server.getSuccessResponse("default-group-created", usergroup);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@ApiOperation(value = "block-user", response = UserDetails.class)
	@RequestMapping(value = "/block", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> blockuser(@RequestParam("data") String username) throws Exception {

		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();

		/*
		 * TO:DO search from list of user either from the main page or individial user
		 * followers group and follwing group and block user
		 * 
		 * Take BlockedUsers group and
		 */

		return null;

	}

}
