package com.fashionapp.serviceImpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fashionapp.Entity.Products;
import com.fashionapp.Repository.ProductRepository;
import com.fashionapp.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService{

	
	@Autowired
	private ProductRepository productRepository;
	
	@Override
	public Optional<Products> findById(long productId) {
 		return productRepository.findById(productId);
	}

}
