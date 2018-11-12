package com.fashionapp.Repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.fashionapp.Entity.Category;
import com.fashionapp.Entity.SubCategory;

public interface SubCategoryRepository extends CrudRepository<SubCategory, Long>{

	List<SubCategory> findBycategory(Category category);
}
