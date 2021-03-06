package com.fashionapp.service;

import java.util.Optional;

import com.fashionapp.Entity.Products;

public interface ProductService {

	Optional<Products> findById(long productId);

	Products save(Products product);

	void deleteById(Long productId);

}
