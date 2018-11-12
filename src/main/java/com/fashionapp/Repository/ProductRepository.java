package com.fashionapp.Repository;

import org.springframework.data.repository.CrudRepository;

import com.fashionapp.Entity.Products;

public interface ProductRepository extends CrudRepository<Products, Long>{

}
