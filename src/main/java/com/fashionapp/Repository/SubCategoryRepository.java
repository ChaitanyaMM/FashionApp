package com.fashionapp.Repository;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import com.fashionapp.Entity.SubCategory;

public interface SubCategoryRepository extends CrudRepository<SubCategory, Long>{

	Optional<SubCategory> findBySubCategoryType(String subCategoryType);

	//List<SubCategory> findBycategory(Category category);
}
