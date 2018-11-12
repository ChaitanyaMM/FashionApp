package com.fashionapp.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fashionapp.Entity.Products;
import com.fashionapp.Entity.WishList;
import com.fashionapp.Repository.WishListRepository;
import com.fashionapp.service.WishListService;

@Service
public class WishListServiceImpl implements WishListService  {

	@Autowired
	private WishListRepository wishListRepository;

	@Override
	public List<WishList> findByUserId(long id) {
 		return wishListRepository.findByUserId(id);
	}

	@Override
	public void deleteById(long id) {
		wishListRepository.deleteById(id);
		
	}

	@Override
	public List<WishList> findByProductsAndUserId(Products products, long id) {
 		return wishListRepository.findByProductsAndUserId(products, id);
	}

	@Override
	public void save(WishList wishList) {
		wishListRepository.save(wishList);
		
	}

}
