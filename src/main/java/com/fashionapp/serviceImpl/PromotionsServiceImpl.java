package com.fashionapp.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fashionapp.Entity.Promotions;
import com.fashionapp.Repository.PromotionsRepository;
import com.fashionapp.service.PromotionsService;

@Service
public class PromotionsServiceImpl implements PromotionsService {
	
	@Autowired
	private PromotionsRepository promotionsRepository;

	@Override
	public Promotions save(Promotions promotionData) {
 		return promotionsRepository.save(promotionData);
	}

}
