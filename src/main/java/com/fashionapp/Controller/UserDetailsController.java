package com.fashionapp.Controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fashionapp.Entity.Comments;
import com.fashionapp.Entity.FileInfo;
import com.fashionapp.Entity.FollowersGroup;
import com.fashionapp.Entity.FollowingGroup;
import com.fashionapp.Entity.HashTag;
import com.fashionapp.Entity.HashtagVideoMap;
import com.fashionapp.Entity.Likes;
import com.fashionapp.Entity.Status;
import com.fashionapp.Entity.UserGroupMap;
import com.fashionapp.Entity.UserInfo;
import com.fashionapp.filestorage.FileStorage;
import com.fashionapp.securityconfiguration.JwtTokenGenerator;
import com.fashionapp.service.AdminService;
import com.fashionapp.service.BlockedDataService;
import com.fashionapp.service.CommentsService;
import com.fashionapp.service.FileInfoService;
import com.fashionapp.service.FirebaseService;
import com.fashionapp.service.FollowersGroupService;
import com.fashionapp.service.FollowingGroupService;
import com.fashionapp.service.HashTagService;
import com.fashionapp.service.HashtagVideoMapService;
import com.fashionapp.service.LikeService;
import com.fashionapp.service.UserGroupMapService;
import com.fashionapp.service.UserService;
import com.fashionapp.util.EmailSender;
import com.fashionapp.util.PasswordEncryptDecryptor;
import com.fashionapp.util.ServerResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
 

 
@RestController
@RequestMapping(value = "/api/userdetails")
@Api(value="UserDetailsController")
public class UserDetailsController {
	private static final Logger log = LoggerFactory.getLogger(UserDetailsController.class);

	ServerResponse<Object> server = new ServerResponse<Object>();
	Map<String, Object> response = new HashMap<String, Object>();
	
	private final UserService userService;
	private final FileInfoService fileInfoService;
	private final BlockedDataService blockedDataService;
	private final LikeService likeService;
	private final CommentsService commentsService;
	private final FileStorage fileStorage;
	private final FirebaseService firebaseService;
	private final FollowingGroupService followingGroupService;
	private final FollowersGroupService followersGroupService;
	private final UserGroupMapService userGroupMapService;
	private final HashTagService hashTagService;
	private final HashtagVideoMapService hashtagVideoMapService;
	private final JwtTokenGenerator jwtTokenGenerator;

	@Autowired
	public UserDetailsController(UserService userService, FileInfoService fileInfoService,
			BlockedDataService blockedDataService, LikeService likeService, CommentsService commentsService,
			FollowingGroupService followingGroupService, FollowersGroupService followersGroupService,
			UserGroupMapService userGroupMapService, HashTagService hashTagService,
			HashtagVideoMapService hashtagVideoMapService, JwtTokenGenerator jwtTokenGenerator, FileStorage fileStorage,
			FirebaseService firebaseService) {

		this.userService = userService;
		this.fileInfoService = fileInfoService;
		this.blockedDataService = blockedDataService;
		this.likeService = likeService;
		this.commentsService = commentsService;
		this.followingGroupService = followingGroupService;
		this.followersGroupService = followersGroupService;
		this.userGroupMapService = userGroupMapService;
		this.hashTagService = hashTagService;
		this.hashtagVideoMapService = hashtagVideoMapService;
		this.jwtTokenGenerator = jwtTokenGenerator;
		this.fileStorage = fileStorage;
		this.firebaseService = firebaseService;
	}
    
    
    
    

	@Autowired
	private EmailSender emailSender;

	private final static String default_url = "home/chaitanya/chaitanya-workspace/workspace/java-webapi/profileimages/male.png";
	private final static String default_image = "male.png";
 

	@ApiOperation(value = "signup", response = UserInfo.class)
 	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> usersignup(@RequestParam("data") String data,
			@RequestParam(value = "file", required = false) MultipartFile profileImage) throws Exception {

		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		UserInfo userDetails = null;
		try {
			userDetails = new ObjectMapper().readValue(data, UserInfo.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		UserInfo isemailExists = userService.findByEmail(userDetails.getEmail());
		if (isemailExists != null) {
			log.info("Email Id already exists, please choose another email id");
			response = server.getDuplicateResponse("Email Id already exists, please choose another email id", null);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CONFLICT);
		}
		userDetails.setPassword(PasswordEncryptDecryptor.encrypt(userDetails.getPassword()));

		UserInfo userData = null;
		if (profileImage == null || profileImage.isEmpty()) {
			userDetails.setProfileImageName(default_image);
			userDetails.setProfileImageUrl(default_url);
 			userData = userService.save(userDetails);

		} else {
			try {
				fileStorage.storeUserProfileImage(profileImage);
				log.info("File uploaded successfully! -> filename = " + profileImage.getOriginalFilename());
			} catch (Exception e) {
				log.info("Fail! -> uploaded filename: = " + profileImage.getOriginalFilename());
			}

			Resource path = fileStorage.loadprofileImage(profileImage.getOriginalFilename());
			System.out.println("PATH :=" + path.toString());
			userDetails.setProfileImageName(profileImage.getOriginalFilename());
 			userDetails.setProfileImageUrl(path.toString());
			userData = userService.save(userDetails);
		}

		emailSender.sendOnRegistration(userData.getUserName(), userData.getEmail());

		System.out.println("creating default group");
		DefaultfollowingGroup(userDetails.getId(), userDetails.getEmail());
		// DefaultfollowersGroup(userDetails.getId(), userDetails.getEmail());
		response = server.getSuccessResponse("SignUp Successful", userData);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	/*
	 * TO:DO
	 * 
	 * 1.Separate logins for admin and users
	 * 
	 * 2. Separate login response based on logged in category if male, male related
	 * content should occur if female ,female-content,kids ...
	 */
	@ApiOperation(value = "login", response = UserInfo.class)
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getAuthtoken(@RequestBody String data) throws Exception {
		log.info("login calling!!..");
		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();

		UserInfo userDetails = null;

		try {
			userDetails = new ObjectMapper().readValue(data, UserInfo.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		final UserInfo user = userService.findByEmail(userDetails.getEmail());
		if (user == null) {
			response = server.getNotFoundResponse("invalid email", null);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		String pwd = PasswordEncryptDecryptor.encrypt(userDetails.getPassword());

		if (!pwd.equalsIgnoreCase(user.getPassword())) {
			log.info("Invalid password");
			response = server.getNotFoundResponse("invalid password", null);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		} else {
			log.info("password is valid");
		}

		final String token = jwtTokenGenerator.generateToken(user);

		response = server.getSuccessResponse("login succesful", token);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

	}
	
	@ApiOperation(value = "forgotpwd", response = UserInfo.class)
 	@RequestMapping(value = "/forgotpwd", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> userforgotpwd(@RequestBody String data) throws Exception {
		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		UserInfo userInfo = null;
		try {
			userInfo = new ObjectMapper().readValue(data, UserInfo.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		UserInfo userInfoObj = userService.findByEmail(userInfo.getEmail());
		if (userInfoObj != null) {
			userInfoObj.setPassword(PasswordEncryptDecryptor.encrypt(userInfo.getPassword()));
			Date date = new Date(System.currentTimeMillis());
			userInfoObj.setCreationDate(date);
			UserInfo userData = userService.save(userInfoObj);
			response = server.getSuccessResponse("Password changed Successfully..", userInfoObj.getEmail());
			// sender.sendmail(userInfoObj.getEmail(), "Changed Password Successfully");
		} else {
			response = server.getNotFoundResponse("User is not registered", null);
		}
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	/*@ApiOperation(value = "listusers", response = UserInfo.class)
 	@RequestMapping(value = "/listusers", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getAll(Pageable pageable) throws IOException, ParseException {
		Map<String, Object> response = new HashMap<String, Object>();
		ServerResponse<Object> server = new ServerResponse<Object>();

		Page<UserInfo> fecthed = userService.findAll(pageable);
		response = server.getSuccessResponse("fetched", fecthed);
		log.info("fetched all users");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}*/
	
	@ApiOperation(value = "update", response = UserInfo.class)
 	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> update(@RequestParam Long id, @RequestBody String data)
			throws IOException, ParseException {
		UserInfo userdetails = null;
		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			userdetails = new ObjectMapper().readValue(data, UserInfo.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		userdetails.setId(id);
		if (data == null) {
			log.info("data is null");
			response = server.getNotAceptableResponse("please enter the data", data);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_ACCEPTABLE);

		} else {
			UserInfo fecthed = userService.save(userdetails);
			log.info("updated successfully");
			response = server.getSuccessResponse("Successful", fecthed);
		}
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	@ApiOperation(value = "find", response = UserInfo.class)
 	@RequestMapping(value = "/find", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> findUser(@RequestParam Long id) throws IOException, ParseException {
		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		Optional<UserInfo> fecthed = userService.findById(id);
		response = server.getSuccessResponse("Uploded Successfully", fecthed);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	@ApiOperation(value = "delete", response = UserInfo.class)
 	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) throws IOException, ParseException {
		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		userService.deleteById(id);
		response = server.getSuccessResponse("deleted successfully", id);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	@ApiOperation(value = "uploadvideo", response = UserInfo.class)
	@RequestMapping(value = "/uploadvideo", method = RequestMethod.POST, headers = ("content-type=multipart/*"))
	@ResponseBody
	public ResponseEntity<Map<String, Object>> upload(@RequestParam("id") Long id,
			@RequestParam("file") MultipartFile file, @RequestParam("tag") String tag) throws IOException {
 
		FileInfo fileInfo = new FileInfo();
		UserInfo userdetails = new UserInfo();
		Date date = new Date(System.currentTimeMillis());
		fileInfo.setDate(date);
		fileInfo.setFileName(file.getOriginalFilename());
		fileInfo.setUserId(id);
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
		FileInfo fileinserted = fileInfoService.save(fileInfo);

		HashTag hashtag = null;
		try {
			hashtag = new ObjectMapper().readValue(tag, HashTag.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (tag != null) {
 			hashtag.setFileId(fileinserted.getId());
			 
			HashTag tagData = hashTagService.save(hashtag);

			HashtagVideoMap mappingtag = new HashtagVideoMap();
			log.info("ineinserted.getId() :+=" + fileinserted.getId());
			mappingtag.setFileId(fileinserted.getId());

			log.info("tagData.getId() :+=" + tagData.getId());

			mappingtag.setTagId(tagData.getId());

			HashtagVideoMap mappedData = hashtagVideoMapService.save(mappingtag);
			log.info("mapped Successfully");

		}

		response = server.getSuccessResponse("Uploded Successfully", fileinserted);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

	}
	
	@ApiOperation(value = "uploadmultiple", response = UserInfo.class)
	@RequestMapping(value = "/uploadmultiple", method = RequestMethod.POST, headers = ("content-type=multipart/*"))
	@ResponseBody
	public ResponseEntity<Map<String, Object>> uplodVideos(@RequestParam("id") Long id,
			@RequestParam("file") List<MultipartFile> files, @RequestParam("tag") List<String> tag) throws IOException {
		List<FileInfo> fileinsertedlist = new ArrayList<>();
		FileInfo fileinserted = null;
		HashTag hashtag = new HashTag();
		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		int i =0;
		for (MultipartFile file : files) {

			try {
				fileStorage.storemultiple(files);
				log.info("File uploaded successfully! -> filename = " + file.getOriginalFilename());
			} catch (Exception e) {
				log.info("Fail! -> uploaded filename: = " + file.getOriginalFilename());
			}

			FileInfo fileInfo = new FileInfo();
			UserInfo userdetails = new UserInfo();
			fileInfo.setFileName(file.getOriginalFilename());
			fileInfo.setUserId(id);
			userdetails.setId(id);
			Date date = new Date(System.currentTimeMillis());
			fileInfo.setDate(date);
			Resource path = fileStorage.loadFile(file.getOriginalFilename());
			System.out.println("PATH :=" + path.toString());
			fileInfo.setUrl(path.toString());
			fileinserted = fileInfoService.save(fileInfo);
			fileinsertedlist.add(fileinserted);
			System.out.println("fileinserted : =" + fileinserted.getId());

		}

		System.out.println("FileID :=" + fileinserted.getId());

		for (String values : tag) {

			System.out.println("tag :==" + tag);
			try {
				hashtag = new ObjectMapper().readValue(values, HashTag.class);
				System.out.println("Values :=" + values);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (tag != null) {
			    
				hashtag.setFileId(id);
				HashTag tagData = hashTagService.save(hashtag);
				log.info("hastage userID := " + tagData.getFileId());
				log.info("tagData := " + tagData.getHashTag());

				HashtagVideoMap mappingtag = new HashtagVideoMap();

				log.info("ineinserted.getId() :+=" + fileinserted.getId());
				mappingtag.setFileId(fileinsertedlist.get(i).getId());

				log.info("tagData.getId() :+=" + tagData.getId());

				mappingtag.setTagId(tagData.getId());

				HashtagVideoMap mappedData = hashtagVideoMapService.save(mappingtag);
				log.info("mapped Successfully");
				i++;
			}
		}

		response = server.getSuccessResponse("Uploded Successfully", fileinsertedlist);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

	}

	/***
	 * to get the uploaded data by individual user
	 */
	@ApiOperation(value = "signup", response = UserInfo.class)
	@RequestMapping(value = "/view-videos-by-userId", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> fetchfilesuplodedbyUser(@RequestParam("id") Long id) throws IOException {
		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		List<FileInfo> files = (List<FileInfo>) fileInfoService.findByUserId(id);
		response = server.getSuccessResponse("fetched", files);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

	}

	/*
	 * 
	 * works for both like and dislike
	 * 
	 */
	@ApiOperation(value = "like", response = UserInfo.class)
 	@RequestMapping(value = "/like", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> fileLike(@RequestParam("userId") Long userId,
			@RequestParam("fileId") Long fileId, @RequestBody String data) throws IOException, ParseException {
		Likes likesObject = new Likes();
		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		Likes likesData = null;
		try {
			likesObject = new ObjectMapper().readValue(data, Likes.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		likesObject.setUserId(userId);
		likesObject.setVideoId(fileId);
		Likes likesObj = likeService.findByUserIdAndVideoId(userId, fileId);
		String status = null;
		if (likesObject.getStatus() == Status.LIKED) {
			status = "Liked";
		} else {
			status = "DisLiked";
		}
		if (likesObj == null) {
			likesData = likeService.save(likesObject);

		} else {
			likesObj.setStatus(likesObject.getStatus());
			likesData = likeService.save(likesObj);

		}
		response = server.getSuccessResponse(status, likesData);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	/*
	 * works for both comment and uncomment
	 * 
	 */
	@ApiOperation(value = "comment", response = UserInfo.class)
 	@RequestMapping(value = "/comment", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> fileComments(@RequestParam("userId") Long userId,
			@RequestParam("fileId") Long fileId, @RequestBody String data) throws IOException, ParseException {
		Comments comentsObject = null;
		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();

		try {
			comentsObject = new ObjectMapper().readValue(data, Comments.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		comentsObject.setUserId(userId);
		comentsObject.setVideoId(fileId);

		Comments commentsData = new Comments();
		Comments fetchedComments = commentsService.findByUserIdAndVideoId(userId, fileId);
		if (fetchedComments.getComment() == null || fetchedComments.getComment().isEmpty()) {

			log.info("cant write empty comment!");
			response = server.getSuccessResponse("cant write empty comment ", commentsData);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_ACCEPTABLE);

		} else {

			if (fetchedComments == null) {
				commentsData = commentsService.save(comentsObject);

			} else {

				fetchedComments.setComment(comentsObject.getComment());
				commentsData = commentsService.save(fetchedComments);
			}
		}
		response = server.getSuccessResponse("commented", commentsData);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	
	@ApiOperation(value = "comment", response = UserInfo.class)
 	@RequestMapping(value = "/comment/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> deleteComment(@PathVariable Long id) throws IOException, ParseException {
		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		commentsService.deleteById(id);
		response = server.getSuccessResponse("deleted successfully", id);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

 
	/*
	 * works for both follow/unfollow 
	 */
	@ApiOperation(value = "follow", response = UserInfo.class)
	@RequestMapping(value = "/follow", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> followUser(@RequestParam("id") Long id,
			@RequestParam("followingId") Long followingId) {
		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		Optional<FollowingGroup> userData = followingGroupService.findByUserId(id);

		UserGroupMap fetchedMapData = userGroupMapService.findByUserIdAndFollowinguserId(id, followingId);
		if (fetchedMapData == null) {
			mapUsertoUsergroup(id, followingId, userData.get().getId(), userData.get().getUseremail());
			response = server.getSuccessResponse("following-user", followingId);

		} else {
			unmapuserfromUsergroup(id, followingId, userData.get().getId(), userData.get().getUseremail());
			response = server.getSuccessResponse("un-followed user", followingId);

		}

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	/*
	 * @RequestMapping(value = "/unfollow", method = RequestMethod.GET)
	 * 
	 * @ResponseBody public ResponseEntity<Map<String, Object>>
	 * unfollowUser(@RequestParam("id") Long id,
	 * 
	 * @RequestParam("followingId") Long followingId) { Map<String, Object> response
	 * = new HashMap<String, Object>(); ServerResponse<Object> server = new
	 * ServerResponse<Object>();
	 * 
	 * Optional<FollowingGroup> userData =
	 * followingGroupService.findByUserId(id);
	 * 
	 * unmapuserfromUsergroup(id, followingId, userData.get().getId(),
	 * userData.get().getUseremail()); response =
	 * server.getSuccessResponse("unfollowed-Successfull", followingId); return new
	 * ResponseEntity<Map<String, Object>>(response, HttpStatus.OK); }
	 */
	
	@ApiOperation(value = "getall-in-group", response = UserInfo.class)
	@RequestMapping(value = "/getall-in-group", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> findUsersInGroup(@RequestParam("id") Long id) {
		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();

		List<UserGroupMap> fetchedUsers = userGroupMapService.findByUserId(id);

		response = server.getSuccessResponse("fecthed users from group", fetchedUsers);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

	}
	
	@ApiOperation(value = "likedlist", response = UserInfo.class)
	@RequestMapping(value = "/likedlist", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> listoflikedfiles(@RequestParam("videoId") Long videoId) {
		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		List<Likes> likesData = null;
		if (Status.LIKED != null) {
			likesData = likeService.findByVideoId(videoId);
		}

		response = server.getSuccessResponse("liked", likesData);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	public void mapUsertoUsergroup(Long userId, Long followinguserId, Long groupId, String email) {
		log.info("to follow user");

		UserGroupMap userGroupMap = new UserGroupMap();
		userGroupMap.setGroupId(groupId);
		userGroupMap.setUserId(userId);
		userGroupMap.setUserEmail(email);
		userGroupMap.setFollowinguserId(followinguserId);
 
		UserGroupMap usermappedData = userGroupMapService.save(userGroupMap);
		log.info("mapped := " + followinguserId + "to the := " + userId + "group");
	}

	public void unmapuserfromUsergroup(Long userId, Long followinguserId, Long groupId, String email) {
		UserGroupMap fetchedMapData = userGroupMapService.findByUserIdAndFollowinguserId(userId, followinguserId);
		userGroupMapService.deleteById(fetchedMapData.getId());
		log.info("unmapped := " + followinguserId + "from the" + userId + "group");

	}

	public void DefaultfollowingGroup(Long userId, String email) {
		FollowingGroup groupData = new FollowingGroup();
		groupData.setUserId(userId);
		groupData.setUseremail(email);
		groupData.setGroupname("following");
 		FollowingGroup usergroup = followingGroupService.save(groupData);
		log.info("default followingGroup created for the userId := " + usergroup.getId());
	}

	public void DefaultfollowersGroup(Long userId, String email) {
		FollowersGroup groupData = new FollowersGroup();
		groupData.setUserId(userId);
		groupData.setUseremail(email);
		groupData.setGroupname("followers");
 		FollowersGroup usergroup = followersGroupService.save(groupData);
		log.info("default followersGroup created for the userId := " + usergroup.getId());
	}

 	@RequestMapping(value = "/block", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> blockuser(@RequestParam("data") String username) throws Exception {

		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();

		 

		return null;

	}

 	

 	@RequestMapping(value = "/send", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> send() throws JSONException {
 
		JSONObject body = new JSONObject();
		body.put("to", "/topics/" + "JavaSampleApproach");
		body.put("priority", "high");
 
		JSONObject notification = new JSONObject();
		notification.put("title", "JSA Notification");
		notification.put("body", "Happy Message!");
		
		JSONObject data = new JSONObject();
		data.put("Key-1", "JSA Data 1");
		data.put("Key-2", "JSA Data 2");
 
		body.put("notification", notification);
		body.put("data", data);
 
/**
		{
		   "notification": {
		      "title": "JSA Notification",
		      "body": "Happy Message!"
		   },
		   "data": {
		      "Key-1": "JSA Data 1",
		      "Key-2": "JSA Data 2"
		   },
		   "to": "/topics/JavaSampleApproach",
		   "priority": "high"
		}
*/
 
		HttpEntity<String> request = new HttpEntity<>(body.toString());
 
		CompletableFuture<String> pushNotification = firebaseService.send(request);
		CompletableFuture.allOf(pushNotification).join();
 
		try {
			String firebaseResponse = pushNotification.get();
			
			return new ResponseEntity<>(firebaseResponse, HttpStatus.OK);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
 
		return new ResponseEntity<>("Push Notification ERROR!", HttpStatus.BAD_REQUEST);
	}
}