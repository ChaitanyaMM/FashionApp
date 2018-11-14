package com.fashionapp.Repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.fashionapp.Entity.Products;

public interface ProductRepository extends CrudRepository<Products, Long>{

	Optional<Products> findByProductType(String productType);

}
