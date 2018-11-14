package com.fashionapp.Controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.fashionapp.Entity.BlockedUsers;
import com.fashionapp.Entity.Comments;
import com.fashionapp.Entity.FileInfo;
import com.fashionapp.Entity.FollowersGroup;
import com.fashionapp.Entity.FollowingGroup;
import com.fashionapp.Entity.HashTag;
import com.fashionapp.Entity.HashtagVideoMap;
import com.fashionapp.Entity.Likes;
import com.fashionapp.Entity.Role;
import com.fashionapp.Entity.UserGroupMap;
import com.fashionapp.Entity.UserInfo;
import com.fashionapp.Entity.VideoStatus;
import com.fashionapp.filestorage.FileStorage;
import com.fashionapp.securityconfiguration.JwtTokenGenerator;
import com.fashionapp.service.BlockedDataService;
import com.fashionapp.service.BlockedUsersService;
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
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/user")
@Api(value="user")
public class UserDetailsController {
	private static final Logger log = LoggerFactory.getLogger(UserDetailsController.class);

	ServerResponse<Object> server = new ServerResponse<Object>();
	Map<String, Object> response = new HashMap<String, Object>();
	
	private final UserService userService;
	private final FileInfoService fileInfoService;
	private final BlockedDataService blockedDataService;
	private final BlockedUsersService blockedUsersService;
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
			FirebaseService firebaseService,BlockedUsersService blockedUsersService) {

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
		this.blockedUsersService=blockedUsersService;
	}
      
	@Autowired
	private EmailSender emailSender;

	private final static String default_url = "home/chaitanya/chaitanya-workspace/workspace/java-webapi/profileimages/male.png";
	private final static String default_image = "male.png";
 
	
	

	@ApiOperation(value = "signup", nickname = "RegisterUser",response = UserInfo.class, notes = "user whom wanted to register the App", tags={ "user", })
    @ApiResponses(value = {  @ApiResponse(code = 200, message = "successful operation") })
	
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

		//emailSender.sendOnRegistration(userData.getUserName(), userData.getEmail());

		System.out.println("creating default group");
		DefaultfollowingGroup(userDetails.getId(), userDetails.getEmail());
		DefaultfollowersGroup(userDetails.getId(), userDetails.getEmail());
		response = server.getSuccessResponse("SignUp Successful", userData);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

 
	@ApiOperation(value = "Logs user into the system", nickname = "loginUser", notes = "", response = String.class, tags = { "user", })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "successful operation", response = String.class),
							@ApiResponse(code = 400, message = "Invalid username/password supplied") })
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getAuthtoken(@NotNull @ApiParam(value = "The email for login", required = true) @Valid @RequestParam(value = "email", required = true) String email,@NotNull @ApiParam(value = "The password for login in clear text", required = true) @Valid @RequestParam(value = "password", required = true) String password) throws Exception {
		log.info("login calling!!..");
		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();

		 
		final UserInfo user = userService.findByEmail(email);
		if (user == null) {
			response = server.getNotFoundResponse("invalid email", null);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		String pwd = PasswordEncryptDecryptor.encrypt(password);

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
	 
	  @ApiOperation(value = "Updated user", nickname = "updateUser",response = UserInfo.class, notes = "This can only be done by the logged in user.", tags={ "user", })
	    @ApiResponses(value = {  @ApiResponse(code = 400, message = "Invalid user supplied"),
	    						 @ApiResponse(code = 404, message = "User not found") })
	  
    @RequestMapping(value = "/user/{userId}",  produces = { "application/xml", "application/json" },  method = RequestMethod.PUT)
 	public ResponseEntity<Map<String, Object>> update(@ApiParam(value = "user needs to be updated",required=true) @PathVariable("userId") Long id,@ApiParam(value = "Updated user object" ,required=true ) @RequestBody String data)
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
		UserInfo fetched =new UserInfo();
		fetched.setId(id);
		fetched.setDescription(userdetails.getDescription());
		fetched.setEmail(userdetails.getEmail());
		fetched.setFirstName(userdetails.getFirstName());
		fetched.setLastName(userdetails.getLastName());
 		fetched.setPhoneNo(userdetails.getPhoneNo());
		fetched.setUserName(userdetails.getUserName());
		fetched.setGender(userdetails.getGender());
		if (data == null) {
			log.info("data is null");
			response = server.getNotAceptableResponse("please enter the data", data);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_ACCEPTABLE);

		} else {
			fetched = userService.save(userdetails);
			log.info("updated successfully");
			response = server.getSuccessResponse("Successful", fetched);
		}
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Find Userby ID", nickname = "getUserById", notes = "Returns a single User", response = UserInfo.class, tags={ "admin", })
	    @ApiResponses(value = {  @ApiResponse(code = 200, message = "successful operation", response = UserInfo.class),
	    						 @ApiResponse(code = 400, message = "Invalid ID supplied"),
	    						 @ApiResponse(code = 404, message = "user not found") })
	
 	@RequestMapping(value = "/findUser/{userId}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> findUser(@PathVariable("userId") Long userId) throws IOException, ParseException {
		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		Optional<UserInfo> fecthed = userService.findById(userId);
		log.info("**Called!**" );

 		response = server.getSuccessResponse("Uploded Successfully", fecthed);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	 
	
	 @ApiOperation(value = "Delete user", nickname = "deleteUser", notes = "This can only be done by the logged in user.", tags={ "user", })
     @ApiResponses(value = {   @ApiResponse(code = 400, message = "Invalid userId supplied"),
    						   @ApiResponse(code = 404, message = "User not found") })  
	 
    @RequestMapping(value = "/delete/{userId}", method = RequestMethod.DELETE)
	public ResponseEntity<Map<String, Object>> delete(@ApiParam(value = "user needs to be deleted",required=true) @PathVariable("userId") Long userId) throws IOException, ParseException {
		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		userService.deleteById(userId);
		followersGroupService.deleteByUserId(userId);
		followingGroupService.deleteByUserId(userId);
		response = server.getSuccessResponse("deleted successfully", userId);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	@ApiOperation(value = "uploadvideo", response = UserInfo.class)
	@RequestMapping(value = "/postVideo", method = RequestMethod.POST, headers = ("content-type=multipart/*"))
	@ResponseBody
	public ResponseEntity<Map<String, Object>> upload(@RequestParam("id") Long id,
			@RequestParam("file") MultipartFile file, @RequestParam("hashTag") String hashTag) throws IOException {
 
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

		HashTag hashtag = new HashTag();
		 
		if (hashTag != null) {
 			hashtag.setFileId(fileinserted.getId());
 			hashtag.setRole(Role.USER);
 			hashtag.setHashTag(hashTag);
			 
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
	@RequestMapping(value = "/postVideos", method = RequestMethod.POST, headers = ("content-type=multipart/*"))
	@ResponseBody
	public ResponseEntity<Map<String, Object>> uplodVideos(@RequestParam("id") Long id,
			@RequestParam("file") List<MultipartFile> files, @RequestParam("hashTag") List<String> hashTag) throws IOException {
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

		for (String values : hashTag) {

		 System.out.println("tag :==" + hashTag);
			try {
				hashtag = new ObjectMapper().readValue(values, HashTag.class);
				System.out.println("Values :=" + values);
			} catch (Exception e) {
				e.printStackTrace();
			} 
			if (hashTag != null) {
	 			hashtag.setRole(Role.USER);
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
	@ApiOperation(value = "Find Videos by UserId", nickname = "getUserById", notes = "Returns a single User", response = UserInfo.class, tags={ "user", })
    @ApiResponses(value = {  @ApiResponse(code = 200, message = "successful operation", response = UserInfo.class),
    						 @ApiResponse(code = 400, message = "Invalid ID supplied"),
    						 @ApiResponse(code = 404, message = "user not found") })
	@RequestMapping(value = "/videos/{userId}", method = RequestMethod.GET)
 	public ResponseEntity<Map<String, Object>> fetchfilesuplodedbyUser(@PathVariable("userId") Long userId) throws IOException {
		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		List<FileInfo> files = (List<FileInfo>) fileInfoService.findByUserId(userId);
		response = server.getSuccessResponse("fetched", files);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

	}
	@ApiOperation(value = "Find Video by UserId", nickname = "getUserById", notes = "Returns a single video", response = FileInfo.class, tags={ "user", })
    @ApiResponses(value = {  @ApiResponse(code = 200, message = "successful operation", response = UserInfo.class),
    						 @ApiResponse(code = 400, message = "Invalid ID supplied"),
    						 @ApiResponse(code = 404, message = "user not found") })
	@RequestMapping(value = "/video/{fileId}", method = RequestMethod.GET)
 	public ResponseEntity<Map<String, Object>> fetchVideo(@PathVariable("fileId") Long fileId) throws IOException {
		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		Optional<FileInfo>  file = fileInfoService.findById(fileId);
		response = server.getSuccessResponse("fetched", file);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

	}
	
	@GetMapping("/download/{filename}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
		Resource file = fileStorage.loadFile(filename);
		return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
					.body(file);	
	}
	
	 
 
	@ApiOperation(value = "like Videos", nickname = "likeVideo", notes = "Likes a video", response = UserInfo.class, tags={ "user", })
    @ApiResponses(value = {  @ApiResponse(code = 200, message = "successful operation", response = UserInfo.class),
    						 @ApiResponse(code = 400, message = "Invalid ID supplied"),
    						 @ApiResponse(code = 404, message = "user not found") })
	
 	@RequestMapping(value = "/like", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> fileLike(@RequestParam("userId") Long userId,
			@RequestParam("fileId") Long fileId, @RequestParam("status") int status) throws IOException, ParseException {
		Likes likesObject = new Likes();
		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		Likes likesData = null;
		 
		likesObject.setUserId(userId);
		likesObject.setVideoId(fileId);
		likesObject.setStatus(VideoStatus.LIKE);
		Likes likesObj = likeService.findByUserIdAndVideoId(userId, fileId);
		String filestatus = null;
		if (likesObject.getStatus() == VideoStatus.LIKE) {
			filestatus = "Liked";
		} else {
			filestatus = "DisLiked";
		}
		if (likesObj == null) {
			likesData = likeService.save(likesObject);

		} else {
			likesObj.setStatus(likesObject.getStatus());
			likesData = likeService.save(likesObj);

		}
		response = server.getSuccessResponse(filestatus, likesData);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
 
	@SuppressWarnings("unused")
	@ApiOperation(value = "Comment on a Videos", nickname = "commentOnVideo", notes = "logged in user can comment on video", response = UserInfo.class, tags={ "user", })
    @ApiResponses(value = {  @ApiResponse(code = 200, message = "successful operation", response = UserInfo.class),
    						 @ApiResponse(code = 400, message = "Invalid ID supplied"),
    						 @ApiResponse(code = 404, message = "user not found") })
	
 	@RequestMapping(value = "/comment", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> fileComments(@RequestParam("userId") Long userId,
			@RequestParam("fileId") Long fileId, @RequestParam("comment") String comment) throws IOException, ParseException {
		Comments comentsObject = new Comments();
		System.out.println("comment:= "+comment);
		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();

		 
		comentsObject.setUserId(userId);
		comentsObject.setVideoId(fileId);
	    comentsObject.setComment(comment);

		Comments commentsData = null;
		Comments fetchedComments = commentsService.findByUserIdAndVideoId(userId, fileId);
		if (comentsObject.getComment() == null || comentsObject.getComment().isEmpty()) {

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
	
	
	 @ApiOperation(value = "Delete comment", nickname = "deleteComment", notes = "This can only be done by the logged in user.", tags={ "user", })
     @ApiResponses(value = {   @ApiResponse(code = 400, message = "Invalid Id supplied"),
    						   @ApiResponse(code = 404, message = "comment not found") })  
	 
 	 @RequestMapping(value = "/comment/{id}", method = RequestMethod.DELETE)
 	public ResponseEntity<Map<String, Object>> deleteComment(@PathVariable Long id) throws IOException, ParseException {
		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		commentsService.deleteById(id);
		response = server.getSuccessResponse("deleted successfully", id);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

  
	 @ApiOperation(value = "follow a User", nickname = "follow a User", notes = "logged in user can follow another user", response = UserInfo.class, tags={ "user", })
	    @ApiResponses(value = {  @ApiResponse(code = 200, message = "successful operation", response = UserInfo.class),
	    						 @ApiResponse(code = 400, message = "Invalid ID supplied"),
	    						 @ApiResponse(code = 404, message = "user not found") })
	 
	@RequestMapping(value = "/follow", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> followUser(@RequestParam("userId") Long userId,
			@RequestParam("followingUserId") Long followingUserId) {
		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		Optional<FollowingGroup> userData = followingGroupService.findByUserId(userId);

		UserGroupMap fetchedMapData = userGroupMapService.findByUserIdAndFollowinguserId(userId, followingUserId);
		if (fetchedMapData == null) {
			mapUsertoUsergroup(userId, followingUserId, userData.get().getId(), userData.get().getUseremail());
			response = server.getSuccessResponse("following-user", followingUserId);

		} else {
			unmapuserfromUsergroup(userId, followingUserId, userData.get().getId(), userData.get().getUseremail());
			response = server.getSuccessResponse("un-followed user", followingUserId);

		}

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
 
	@ApiOperation(value = "getall-in-group", response = UserInfo.class)
	@RequestMapping(value = "/getall-in-group/{userId}", method = RequestMethod.GET)
 	public ResponseEntity<Map<String, Object>> findUsersInGroup(@PathVariable("userId") Long userId) {
		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();

		List<UserGroupMap> fetchedUsers = userGroupMapService.findByUserId(userId);

		response = server.getSuccessResponse("fecthed users from group", fetchedUsers);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

	}
	
	@ApiOperation(value = "likedlist", response = UserInfo.class)
	@RequestMapping(value = "/likedList/{videoId}", method = RequestMethod.GET)
 	public ResponseEntity<Map<String, Object>> listoflikedfiles(@PathVariable("videoId") Long videoId) {
		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		List<Likes> likesData = null;
		if (VideoStatus.LIKE != null) {
			likesData = likeService.findByVideoId(videoId);
		}

		response = server.getSuccessResponse("liked", likesData);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	

	@ApiOperation(value = "block-user", response = UserInfo.class)
	@RequestMapping(value = "/block", method = RequestMethod.GET)
 	public ResponseEntity<Map<String, Object>> blockuser(@RequestParam("userId") Long userId,
 			@RequestParam("blockedUserId") Long blockedUserId) throws Exception {

		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();

		/*
		 * TO:DO search from list of user either from the main page or individial user
		 * followers group and follwing group and block user
		 * 
		 * Take BlockedUsers group and
		 */
		BlockedUsers blockedUsers = new BlockedUsers();

		Optional<UserInfo> blockedUserInfo = userService.findById(blockedUserId);
		Optional<UserInfo> userInfo = userService.findById(userId);

		 

		BlockedUsers isBlockedUser = blockedUsersService.findByUserIdAndBlockedUserId(userId, blockedUserId);
		if (isBlockedUser == null) {
 			blockedUsers.setBlockedUserId(blockedUserId);
			blockedUsers.setUserId(userId);
			blockedUsers.setUserName(userInfo.get().getUserName());
			blockedUsers.setBlockedUserName(blockedUserInfo.get().getUserName());
			BlockedUsers blockedUserData = blockedUsersService.save(blockedUsers);
			response = server.getSuccessResponse("Blocked " + blockedUserInfo.get().getUserName() + " Successfully",
					blockedUserData.getUserName());
		} else {
			 blockedUsersService.deleteById(isBlockedUser.getId());
			 response = server.getSuccessResponse("UnBlocked User Successfully",isBlockedUser.getUserName());
		}
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

	}

	@ApiOperation(value = "blocked-user-list", response = UserInfo.class)
	@RequestMapping(value = "/blockeduserslist/{userId}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getBlockUsersList(@PathVariable("userId") Long userId) throws Exception {
		
		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		
		List<BlockedUsers> blockedUsersList =  blockedUsersService.findByuserId(userId);
		if(blockedUsersList.size() != 0) {
		response = server.getSuccessResponse("Blocked Users List ", blockedUsersList);
		}else {
			response = server.getNotFoundResponse("no blocked users!", null);	
		}
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
		log.info("default followingGroup created for the userId := " + usergroup.getUserId());
	}

	public void DefaultfollowersGroup(Long userId, String email) {
		FollowersGroup groupData = new FollowersGroup();
		groupData.setUserId(userId);
		groupData.setUserEmail(email);
		groupData.setGroupName("followers");
 		FollowersGroup usergroup = followersGroupService.save(groupData);
		log.info("default followersGroup created for the userId := " + usergroup.getUserId());
	}
	
	public void DefaultBlockedUsersGroup(Long userId,String userName) {
		BlockedUsers blockedGroup =new BlockedUsers();
		blockedGroup.setUserId(userId);
		blockedGroup.setUserName(userName);
		blockedGroup.setGroupName("blockedUsers");
		BlockedUsers defaultGroup =blockedUsersService.save(blockedGroup);
		log.info("default blockGroup created for the userId := " + defaultGroup.getUserId());
	 }

	/*
	@ApiOperation(value = "unblock-user", response = UserInfo.class)
	@RequestMapping(value = "/unblock", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> unBlockuser(@RequestParam("email") String email,@RequestParam("id") Long id) throws Exception {
		
		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		
		UserInfo blockedUserInfo = userService.findByEmail(email);
		BlockedUsers blockedUserData = blockedUsersService.findByUserIdAndBlockedUserId(id, blockedUserInfo.getId());
		if(blockedUserData != null) {
			blockedUsersService.deleteById(blockedUserData.getId());
		 response = server.getSuccessResponse("UnBlocked User Successfully",blockedUserData.getUsername());
		}else {
			response = server.getNotFoundResponse(" ", null);
		}
		
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

	}
	*/
	

 	
/*
 	@RequestMapping(value = "/notification", method = RequestMethod.GET, produces = "application/json")
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
 
*//**
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
*//*
 
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
	}*/
}