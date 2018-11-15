package com.fashionapp.initializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.fashionapp.Entity.Category;
import com.fashionapp.Entity.ProductCategory;
import com.fashionapp.Entity.Products;
import com.fashionapp.Entity.SubCategory;
import com.fashionapp.Repository.CategoryRepository;
import com.fashionapp.Repository.ProductCategoryRepository;
import com.fashionapp.Repository.ProductRepository;
import com.fashionapp.Repository.SubCategoryRepository;

 		/*creates the default when the App boot up*/

@Component
public class ContextInitEvent implements ApplicationListener<ContextRefreshedEvent>{
	
	private static final Log log = LogFactory.getLog(ContextInitEvent.class);
	
	private CategoryRepository categoryRepository;
	private SubCategoryRepository subCategoryRepository;
	private ProductCategoryRepository productCategoryRepository;


	@Autowired
	public ContextInitEvent(CategoryRepository categoryRepository, SubCategoryRepository subCategoryRepository,
			ProductCategoryRepository productCategoryRepository) {
		super();
		this.categoryRepository = categoryRepository;
		this.subCategoryRepository = subCategoryRepository;
		this.productCategoryRepository = productCategoryRepository;
	}


	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		log.info("default context Initialized!");
	     initDefaultProducts();
	}


	 

    private void initDefaultProducts() {
	 Optional<Category> fetchMens=null;
	 Optional<Category> fetchWomens=null;
	 Optional<SubCategory> fetchsubcategory=null;
	 Optional<ProductCategory> producttype=null;
	 
	 /*default creation list*/
	
	 String[] categoryList= {"Mens","Womens"};
	
	 String[] subcategoryList= {"Clothing","Footwear","Accessories"};
	
	 String[] mensClothing={"T-Shirts & Polos", "Jeans", "Shirts", "Trousers & Chinos",
			 				"Trackpants & Tracksuits", "Traditional wear: Kurta, Pyjama & Sherwanis", 
			 				"Suitings & Shirtings", "Suits & Blazers", "Sports/Fitness Wear", "Jackets","Sweatshirts, Sweaters & Thermals"};
	
	 String[] mensFootWear = {"Sports Shoes & Running Shoes","Casual Shoes", "Sandals & Floaters", 
			 				  "Formal Shoes", "Loafers", "Slippers", "Sneakers", "Boots", "Ethnic Footwear", "Socks" };
	 
	 String[] mensAccessories = {"Wallets" , "Sunglasses", "Watches","Belts","Jewellery & Cufflinks", 
								 "Hats & Caps", "Neckties & Cravats", "Suspenders" };
	
	 	
	 String[] womensClothing= {"Salwar Suits", "Kurtas & Kurtis","Lehengas",
			 				   "Shawls", "Saree",  "Tops & Tunics", "Dresses","T-Shirts", "Jeans & Jeggings", 
			 				   "Shrugs & Waistcoats", "Sweatshirts, Cardigans & Pullovers", "Jackets" };
	
	 String[] womensFootWear = {"Heels", "Boots", "Formal shoes", "Casual Shoes", 
			 					"Sneakers","Flats & Sandals","Sports and running shoes","Ballerinas","Slippers & Flip Flops", 
			 					"Ethnic Footwear", "socks" };
	 
	 String[] womensAccessories = {"Handbags", "Wallets", "Clutches",  "Utility bags", "Watches", 
			 					   "Sunglasses", "Necklaces, pendants and sets", "Earrings",  "Bangles & Bracelets",  "Rings"};

	
	
	    /*categeories*/
		Category inserted = null;
		List<Category> list = new ArrayList<>();
		for (String value : categoryList) {
			Category category = new Category();

			category.setCategoryType(value);
			fetchMens = categoryRepository.findByCategoryType(category.getCategoryType());
			if (!fetchMens.isPresent()) {
				inserted = categoryRepository.save(category);
				list.add(inserted);
			}

		}
		
	
	
	    /*Sub-categeories*/

 		for (String value : subcategoryList) {
			SubCategory subCategory = new SubCategory();
			fetchMens = categoryRepository.findByCategoryType("Mens");
 			subCategory.setCategoryId(fetchMens.get().getId());
			subCategory.setSubCategoryType(value);
			fetchsubcategory = subCategoryRepository.findBySubCategoryTypeAndCategoryId(subCategory.getSubCategoryType(),fetchMens.get().getId());

			if (!fetchsubcategory.isPresent()) {
				  subCategoryRepository.save(subCategory);
			}
		}
		
 		for (String value : subcategoryList) {
			SubCategory subCategory = new SubCategory();
			fetchWomens = categoryRepository.findByCategoryType("Womens");
			subCategory.setCategoryId(fetchWomens.get().getId());
			subCategory.setSubCategoryType(value);
			fetchsubcategory = subCategoryRepository.findBySubCategoryTypeAndCategoryId(subCategory.getSubCategoryType(),fetchWomens.get().getId());

			if (!fetchsubcategory.isPresent()) {
				 subCategoryRepository.save(subCategory);
			}
		}


		/* Men related products !!*/

 		for (String values : mensClothing) {

			Optional<SubCategory> fetchId = subCategoryRepository.findBySubCategoryTypeAndCategoryId("Clothing", fetchMens.get().getId());

			ProductCategory product = new ProductCategory();
			product.setSubCategoryId(fetchId.get().getId());
			product.setProductType(values);

			producttype = productCategoryRepository.findByProductType(product.getProductType());

			if (!producttype.isPresent()) {
				productCategoryRepository.save(product);
			}

		}

		for (String values : mensFootWear) {

			Optional<SubCategory> fetchId = subCategoryRepository.findBySubCategoryTypeAndCategoryId("Footwear", fetchMens.get().getId());

			ProductCategory product = new ProductCategory();
			product.setSubCategoryId(fetchId.get().getId());
			product.setProductType(values);

			producttype = productCategoryRepository.findByProductType(product.getProductType());

			if (!producttype.isPresent()) {
				productCategoryRepository.save(product);
			}

		}

		for (String values : mensAccessories) {

			Optional<SubCategory> fetchId = subCategoryRepository.findBySubCategoryTypeAndCategoryId("Accessories",fetchMens.get().getId());

			ProductCategory product = new ProductCategory();
			product.setSubCategoryId(fetchId.get().getId());
			product.setProductType(values);

			producttype = productCategoryRepository.findByProductType(product.getProductType());

			if (!producttype.isPresent()) {
				productCategoryRepository.save(product);
			}

		}
		
		/* Women related products !!*/
		
		for (String values : womensClothing) {

			Optional<SubCategory> fetchId = subCategoryRepository.findBySubCategoryTypeAndCategoryId("Clothing", fetchWomens.get().getId());

			ProductCategory product = new ProductCategory();
			product.setSubCategoryId(fetchId.get().getId());
			product.setProductType(values);

			producttype = productCategoryRepository.findByProductType(product.getProductType());

			if (!producttype.isPresent()) {
				productCategoryRepository.save(product);
			}

		}

		for (String values : womensFootWear) {

			Optional<SubCategory> fetchId = subCategoryRepository.findBySubCategoryTypeAndCategoryId("Footwear", fetchWomens.get().getId());

			ProductCategory product = new ProductCategory();
			product.setSubCategoryId(fetchId.get().getId());
			product.setProductType(values);

			producttype = productCategoryRepository.findByProductType(product.getProductType());

			if (!producttype.isPresent()) {
				productCategoryRepository.save(product);
			}

		}

		for (String values : womensAccessories) {

			Optional<SubCategory> fetchId = subCategoryRepository.findBySubCategoryTypeAndCategoryId("Accessories",fetchWomens.get().getId());

			ProductCategory product = new ProductCategory();
			product.setSubCategoryId(fetchId.get().getId());
			product.setProductType(values);

			producttype = productCategoryRepository.findByProductType(product.getProductType());

			if (!producttype.isPresent()) {
				productCategoryRepository.save(product);
			}

		}
		 
		
		 log.info("default products have been created!!");
		
		
	 
		
    }



}
