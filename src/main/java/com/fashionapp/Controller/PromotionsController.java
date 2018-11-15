package com.fashionapp.Controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.fashionapp.Entity.Promotions;
import com.fashionapp.filestorage.FileStorage;
import com.fashionapp.service.PromotionsService;
import com.fashionapp.util.ServerResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/promotion")
public class PromotionsController {
	
	
	private static final Logger log = LoggerFactory.getLogger(PromotionsController.class);

	ServerResponse<Object> server = new ServerResponse<Object>();
	Map<String, Object> response = new HashMap<String, Object>();
	
	private final PromotionsService promotionsService;
	private final FileStorage fileStorage;

	
	
	@Autowired
	public PromotionsController(PromotionsService promotionsService, FileStorage fileStorage) {
 		this.promotionsService = promotionsService;
		this.fileStorage = fileStorage;
	}



	@ApiOperation(value = "pushed promotion videos or images in the App", nickname = "promotions", notes = "", response = String.class, tags = { "promotions", })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "successful operation", response = String.class),
							@ApiResponse(code = 400, message = "Invalid file") })
	 
	@RequestMapping(value = "/post", method = RequestMethod.POST, headers = ("content-type=multipart/*"))
 	public ResponseEntity<Map<String, Object>> upload(@RequestParam("file") MultipartFile file) throws IOException {

		Promotions promotionData = new Promotions();
 		promotionData.setImageName(file.getOriginalFilename());
  		try {
			fileStorage.store(file);
			log.info("File uploaded successfully! -> filename = " + file.getOriginalFilename());
		} catch (Exception e) {
			log.info("Fail! -> uploaded filename: = " + file.getOriginalFilename());
		}
		Resource path = fileStorage.loadFile(file.getOriginalFilename());
		System.out.println("PATH :=" + path.toString());
		promotionData.setImageUrl(path.toString());
		Promotions fileinserted = promotionsService.save(promotionData);
		response = server.getSuccessResponse("Uploded Successfully", fileinserted);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

	}
	
	
	
	
	
	
	
	
	
	

}
