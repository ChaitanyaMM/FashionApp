package com.fashionapp.Repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.fashionapp.Entity.Products;
import com.fashionapp.Entity.WishList;

public interface WishListRepository extends CrudRepository<WishList, Long>{

	//List<WishList> findByProductIdAndUserId(long productId,long userId);
	List<WishList> findByUserId(Long id);
	List<WishList> findByProductsAndUserId(Products products, Long id);
	
}
