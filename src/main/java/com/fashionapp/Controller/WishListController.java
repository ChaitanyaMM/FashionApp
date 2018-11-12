package com.fashionapp.Controller;

import java.sql.Date;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.fashionapp.Entity.Products;
import com.fashionapp.Entity.UserInfo;
import com.fashionapp.Entity.WishList;
import com.fashionapp.filestorage.FileStorage;
import com.fashionapp.service.ProductService;
import com.fashionapp.service.WishListService;
import com.fashionapp.util.ServerResponse;
import io.swagger.annotations.ApiOperation;

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

 	@RequestMapping(value = "/addToWishList", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> addWishList(@RequestParam("id") Long id,
			@RequestParam("productImage") MultipartFile productImage, @RequestParam("productId") Long productId)
			throws Exception {

		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		WishList wishList = new WishList();
		
		Optional<Products> products =  productService.findById(productId);
		
		wishList.setUserId(id);
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
		response = server.getSuccessResponse(products.get().getProductType()+" added to wishList", products.get().getProductType());

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

 	//To delete a product from wish list
	@ApiOperation(value = "remove_from_WishList", response = UserInfo.class)
	@RequestMapping(value = "/removeWishList", method = RequestMethod.DELETE)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> removeFromWishList(@RequestParam("id") Long id,
			@RequestParam("productId") Long productId) throws Exception {

		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		
		Optional<Products> products =  productService.findById(productId);
		
		List<WishList> wishLists = wishListService.findByProductsAndUserId(products.get() , id);
		WishList wishList = wishLists.get(0);
		
		if (wishLists.size() > 0) {
			wishListService.deleteById(wishLists.get(0).getId());
			response = server.getSuccessResponse( wishList.getProducts().getProductType()+" removed Successfully", wishList.getProducts().getProductType());
		}
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@ApiOperation(value = "clear_WishList", response = UserInfo.class)
	@RequestMapping(value = "/clearWishList", method = RequestMethod.DELETE)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> clearWishList(@RequestParam("id") Long id) throws Exception {

		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();

		List<WishList> wishLists = wishListService.findByUserId(id);
		for (WishList w : wishLists) {
			wishListService.deleteById(w.getId());
		}
		response = server.getSuccessResponse("Cleared Wishlist", null);
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@ApiOperation(value = "get_WishList", response = UserInfo.class)
	@RequestMapping(value = "/getWishList", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getWishList(@RequestParam("id") Long id) throws Exception {

		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		
		List<WishList> wishLists = wishListService.findByUserId(id);
		if(wishLists.size() > 0) {
		response = server.getSuccessResponse(" Wishlist ", wishLists);
		}else {
			response = server.getSuccessResponse(" Wishlist is empty", wishLists);	
		}
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	 

}
