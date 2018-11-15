package com.fashionapp.Repository;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import com.fashionapp.Entity.ProductCategory;
 
public interface ProductCategoryRepository extends CrudRepository<ProductCategory, Long> {
	
	Optional<ProductCategory> findByProductType(String productType);

}
