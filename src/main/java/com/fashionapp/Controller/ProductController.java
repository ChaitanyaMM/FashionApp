package com.fashionapp.Controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.fashionapp.Enum.ProductTypes;
import com.fashionapp.Entity.Products;
import com.fashionapp.filestorage.FileStorage;
import com.fashionapp.service.ProductService;
import com.fashionapp.util.ServerResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/product")
public class ProductController {
    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

	 ServerResponse<Object> server = new ServerResponse<Object>();
	 Map<String, Object> response = new HashMap<String, Object>();
	 
	 private final static String default_url = "home/chaitanya/chaitanya-workspace/workspace/java-webapi/profileimages/defaultProduct.png";
	 private final static String default_image = "defaultProduct.png";
	 

 	 private final ProductService productService;
 	 private final FileStorage fileStorage;

 
	
	@Autowired
	public ProductController(ProductService productService,FileStorage fileStorage) {
		super();
 		this.productService = productService;
		this.fileStorage=fileStorage;

	}

	@ApiOperation(value = "addProduct", nickname = "addProduct", notes = "", tags={ "product", })
	@ApiResponses(value = {   @ApiResponse(code = 200, message = "successful operation"),
			                  @ApiResponse(code = 400, message = "Invalid productId supplied"),
							  @ApiResponse(code = 404, message = "productId not found") })  
 	@RequestMapping(value = "/{type}", method = RequestMethod.POST)
 	public ResponseEntity<Map<String, Object>> CreateProduct(@RequestParam("data") String data,@PathVariable(value = "type") int type,
			 @RequestParam(value = "file", required = false) MultipartFile productImage) {

		Products inserted = new Products();
		Products product = null;

		try {
			product = new ObjectMapper().readValue(data, Products.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (type == 1) {

			product.setProductType(ProductTypes.TSHIRTSANDPOLOS);
		}

		if (productImage == null || productImage.isEmpty()) {
			product.setProductImage(default_image);
			product.setProductImageUrl(default_url);
			inserted = productService.save(product);

		} else {
			try {
				fileStorage.storeUserProfileImage(productImage);
				log.info("File uploaded successfully! -> filename = " + productImage.getOriginalFilename());
			} catch (Exception e) {
				log.info("Fail! -> uploaded filename: = " + productImage.getOriginalFilename());
			}

			Resource path = fileStorage.loadprofileImage(productImage.getOriginalFilename());
			System.out.println("PATH :=" + path.toString());
			product.setProductImage(productImage.getOriginalFilename());
			product.setProductImageUrl(path.toString());
			inserted = productService.save(product);
		}

		response = server.getSuccessResponse("product has been Uploded!", inserted);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);

	}
    
        @ApiOperation(value = "Find productby ID", nickname = "getProductById", notes = "Returns a single product", response = Products.class, tags={ "product", })
        @ApiResponses(value = { @ApiResponse(code = 200, message = "successful operation", response = Products.class),
    						    @ApiResponse(code = 400, message = "Invalid ID supplied"),
    						    @ApiResponse(code = 404, message = "product not found") })

		@RequestMapping(value = "/{productId}",method = RequestMethod.GET)
		public ResponseEntity<Map<String, Object>> findUser(@PathVariable("productId") Long productId) throws IOException, ParseException {
		log.info("findProduct is calling");
		 
		Optional<Products> fecthed = productService.findById(productId);
		response = server.getSuccessResponse("Uploded Successfully", fecthed);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
		}
		
		@ApiOperation(value = "Delete product", nickname = "deleteProduct", notes = "", tags={ "product", })
		@ApiResponses(value = {   @ApiResponse(code = 400, message = "Invalid productId supplied"),
								  @ApiResponse(code = 404, message = "productId not found") })  
		
		@RequestMapping(value = "/{productId}", method = RequestMethod.DELETE)
		public ResponseEntity<Map<String, Object>> delete(@ApiParam(value = "The product that needs to be deleted",required=true) @PathVariable("productId") Long productId)
			throws IOException, ParseException {
		log.info("deleteProduct is calling");
		Optional<Products> fecthed = productService.findById(productId);

		productService.deleteById(productId);
		response = server.getSuccessResponse("deleted successfully", fecthed.get().getId());
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
		}
	
	
	
	
	
	
	
	

}
