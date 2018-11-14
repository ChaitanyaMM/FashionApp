package com.fashionapp.initializer;

import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.fashionapp.Entity.Category;
import com.fashionapp.Entity.Products;
import com.fashionapp.Entity.SubCategory;
import com.fashionapp.Repository.CategoryRepository;
import com.fashionapp.Repository.ProductRepository;
import com.fashionapp.Repository.SubCategoryRepository;

 
@Component
public class ContextInitEvent implements ApplicationListener<ContextRefreshedEvent>{
	
	private static final Log logger = LogFactory.getLog(ContextInitEvent.class);
	
	private CategoryRepository categoryRepository;
	private SubCategoryRepository subCategoryRepository;
	private ProductRepository productRepository;


	@Autowired
	public ContextInitEvent(CategoryRepository categoryRepository, SubCategoryRepository subCategoryRepository,
			ProductRepository productRepository) {
		super();
		this.categoryRepository = categoryRepository;
		this.subCategoryRepository = subCategoryRepository;
		this.productRepository = productRepository;
	}


	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		logger.info("FashionApp context Initialized!");
	    initDefaultProducts();
	}


	private void initDefaultProducts() {
		Optional<Category> category=null;
		Optional<SubCategory> subcategory=null;
		Optional<Products> producttype=null;
		
		String[] categoryList= {"Men’s","Women’s","Kids","Home"};
		String[] subcategoryList= {"Clothing","Footwear","Accessories","Home"};
		String[] mensClothingproducts={"T-Shirts & Polos", "Jeans", "Shirts", "Trousers & Chinos",
		"Trackpants & Tracksuits", "Traditional wear: Kurta, Pyjama & Sherwanis", 
		"Suitings & Shirtings", "Suits & Blazers", "Sports/Fitness Wear", "Jackets"};
		String[] womensClothingProducts= { "Salwar Suits", "Kurtas & Kurtis","Lehengas",
				"Shawls", "Saree",  "Tops & Tunics", "Dresses","T-Shirts", "Jeans & Jeggings", 
				"Shrugs & Waistcoats", "Sweatshirts, Cardigans & Pullovers", "Jackets"
 		
		};

		
		
		
		
		
		
		
		
		
		
		
		
		
		
		Category categeroy = new Category();
		categeroy.setCategoryType("Mens");
		category = categoryRepository.findByCategoryType(categeroy.getCategoryType());
		if (!category.isPresent()) {
			categoryRepository.save(categeroy);
		}

		SubCategory subCategory = new SubCategory();
		subCategory.setCategoryId(categeroy.getId());
		subCategory.setSubCategoryType("Clothing");
		subcategory = subCategoryRepository.findBySubCategoryType(subCategory.getSubCategoryType());
		
		if (!subcategory.isPresent()) {
			subCategoryRepository.save(subCategory);
		}
		
		Products product = new Products();
		
		product.setProductType("T-Shirts & Polos");
		product.setSubCategoryId(subCategory.getId());
		producttype = productRepository.findByProductType(product.getProductType());
		if (!producttype.isPresent()) {
			productRepository.save(product);
		}
		
		
		
		
		
		
		
		
		
		
	}
	
	 
	
/*	@PersistenceContext
	private EntityManager em;
	
	private void initDefaultProducts() {
		  StringBuilder builder = new StringBuilder();
			builder.append("INSERT INTO Category(id,categoryType) VALUES ( 4,'afafa' ) ");
			 
			
			
			System.out.println("Query :== "+builder.toString());

			
			Query result = em.createQuery(builder.toString());
  
			System.out.println("into entitiy inserting ");
			System.out.println("result " + result);

	 	
		log.info("**Default Products **" );

	}*/
	

}
