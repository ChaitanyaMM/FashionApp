//package com.fashionapp.Controller;
//
//import javax.validation.Valid;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import io.swagger.annotations.ApiParam;
//import io.swagger.annotations.ApiResponse;
//import io.swagger.annotations.ApiResponses;
//import io.swagger.annotations.Authorization;
//import io.swagger.annotations.AuthorizationScope;
//
//@Api(value = "Admin", description = "the Admin API")
//
//public interface AdminApi {
//	
//	ApiOperation(value = "Add a new admin", nickname = "addAdmin", notes = "", authorizations = {
//	        @Authorization(value = "petstore_auth", scopes = {
//	            @AuthorizationScope(scope = "write:pets", description = "modify pets in your account"),
//	            @AuthorizationScope(scope = "read:pets", description = "read your pets")
//	            })
//	    }, tags={ "pet", })
//	    @ApiResponses(value = { 
//	        @ApiResponse(code = 405, message = "Invalid input") })
//	    @RequestMapping(value = "/pet",
//	        produces = { "application/xml", "application/json" }, 
//	        consumes = { "application/json", "application/xml" },
//	        method = RequestMethod.POST)
//	    ResponseEntity<Void> addPet(@ApiParam(value = "Pet object that needs to be added to the store" ,required=true )  @Valid @RequestBody Pet body);
//
//
//}
