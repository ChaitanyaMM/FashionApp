package com.fashionapp.Repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.fashionapp.Entity.Category;

public interface CategoryRepository extends CrudRepository<Category, Long>{

	Optional<Category> findByCategoryType(String type);

}
