package com.fashionapp.service;

import java.util.List;

import com.fashionapp.Entity.Products;
import com.fashionapp.Entity.WishList;

public interface WishListService {

	List<WishList> findByUserId(long id);

	void deleteById(long id);

	List<WishList> findByProductsAndUserId(Products products, long id);

	void save(WishList wishList);

}
