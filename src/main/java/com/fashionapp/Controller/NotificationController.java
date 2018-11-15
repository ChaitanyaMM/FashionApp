package com.fashionapp.Controller;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fashionapp.Entity.Admin;
import com.fashionapp.service.FirebaseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/notification")
//@Api(value="notification")
public class NotificationController {
	
	private final FirebaseService firebaseService;
	
	@Autowired
	public NotificationController(FirebaseService firebaseService) {
 		this.firebaseService = firebaseService;
	}

   /*Sends Push notication using firebase Key 
	   to android and ios respectively*/
	@ApiOperation(value = "Send Notification", nickname = "Notification",response = String.class, notes = "when evenet triggered it sends notification", tags={ "notification", })
    @ApiResponses(value = {  @ApiResponse(code = 200, message = "successful operation") })

	@RequestMapping(value = "/send", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> send() throws JSONException {
 
		JSONObject body = new JSONObject();
		body.put("to", "/topics/" + "ProjectY");
		body.put("priority", "high");
 
		JSONObject notification = new JSONObject();
		notification.put("title", " Notification");
		notification.put("body", "Push Notificataion!");
		
		JSONObject data = new JSONObject();
		data.put("Key-1", "Data 1");
		data.put("Key-2", "Data 2");
 
		body.put("notification", notification);
		body.put("data", data);
 
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
