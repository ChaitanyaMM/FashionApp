package com.fashionapp.Controller;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fashionapp.Entity.Admin;
import com.fashionapp.Entity.Products;
import com.fashionapp.Entity.UserInfo;
import com.fashionapp.Entity.WishList;
import com.fashionapp.filestorage.FileStorage;
import com.fashionapp.service.ProductService;
import com.fashionapp.service.WishListService;
import com.fashionapp.util.ServerResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/wishlist")
 public class WishListController {

	private static final Logger log = LoggerFactory.getLogger(WishListController.class);

	@Autowired
	private WishListService wishListService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	FileStorage fileStorage;

 	@ApiOperation(value = "addProducttoWishList", nickname = "addProducttoWishList",response = Admin.class, notes = "This can only be done by the logged in user.")
    @ApiResponses(value = {  @ApiResponse(code = 200, message = "successful operation"),
    		 					@ApiResponse(code = 400, message = "Invalid  Data") })

 	@RequestMapping(value = "/add/{userId}", method = RequestMethod.POST)
 	public ResponseEntity<Map<String, Object>> addWishList(@PathVariable("userId") Long userId,
			@RequestParam("productImage") MultipartFile productImage, @RequestParam("productId") Long productId)
			throws Exception {

		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		WishList wishList = new WishList();
		
		Optional<Products> products =  productService.findById(productId);
		
		wishList.setUserId(userId);
		wishList.setProducts(products.get());
		wishList.setFileName(productImage.getOriginalFilename());
		Date date = new Date(System.currentTimeMillis());
		wishList.setDate(date);
		try {
			fileStorage.store(productImage);
			log.info("Uploaded product sucessfully ! -> filename = " + productImage.getOriginalFilename());
		} catch (Exception e) {
			log.info("Fail! -> uploaded product name: = " + productImage.getOriginalFilename());
		}
		Resource path = fileStorage.loadFile(productImage.getOriginalFilename());
		System.out.println("PATH :=" + path.toString());
		wishList.setUrl(path.toString());

		wishListService.save(wishList);
		response=server.getSuccessResponse("added to wishList", products);
		//response = server.getSuccessResponse(products.get().getProductType()+" added to wishList", products.get().getProductType());

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
 
 	@ApiOperation(value = "removeFromWishList", nickname = "addProducttoWishList",response = Products.class, notes = "This can only be done by the logged in user.")
    @ApiResponses(value = {  @ApiResponse(code = 200, message = "successful operation"),
    		 					@ApiResponse(code = 400, message = "Invalid  Data") })
 	
	@RequestMapping(value = "/Product", method = RequestMethod.DELETE)
	public ResponseEntity<Map<String, Object>> removeFromWishList(@RequestParam("userId") Long userId,
			@RequestParam("productId") Long productId) throws Exception {

		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		
		Optional<Products> products =  productService.findById(productId);
		
		List<WishList> wishLists = wishListService.findByProductsAndUserId(products.get(),userId);
		WishList wishList = wishLists.get(0);
		
		if (wishLists.size() > 0) {
			wishListService.deleteById(wishLists.get(0).getId());
			response=server.getSuccessResponse("added to wishList", products);

			//response = server.getSuccessResponse( wishList.getProducts().getProductType()+" removed Successfully", wishList.getProducts().getProductType());
		}
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

 	@ApiOperation(value = "Delete", nickname = "Delete",response = Products.class, notes = "This can only be done by the logged in user.")
    @ApiResponses(value = {  @ApiResponse(code = 200, message = "successful operation"),
    		 					@ApiResponse(code = 400, message = "Invalid  Data") })
 	
 	@RequestMapping(value = "/Delete/{userId}", method = RequestMethod.DELETE)
	public ResponseEntity<Map<String, Object>> clearWishList(@PathVariable("userId") Long userId) throws Exception {

		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();

		List<WishList> wishLists = wishListService.findByUserId(userId);
		for (WishList w : wishLists) {
			wishListService.deleteById(w.getId());
		}
		response = server.getSuccessResponse("Cleared Wishlist", null);
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
 	@ApiOperation(value = "GetWishListData", nickname = "GetWishListData",response = Products.class, notes = "This can only be done by the logged in user.")
    @ApiResponses(value = {  @ApiResponse(code = 200, message = "successful operation"),
    		 				 @ApiResponse(code = 400, message = "Invalid  Data") })
	@RequestMapping(value = "/{userId}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getWishList(@PathVariable("userId") Long userId) throws Exception {

		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		
		List<WishList> wishLists = wishListService.findByUserId(userId);
		if(wishLists.size() > 0) {
		response = server.getSuccessResponse(" Wishlist ", wishLists);
		}else {
			response = server.getSuccessResponse(" Wishlist is empty", wishLists);	
		}
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	 

}
