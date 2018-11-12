package com.fashionapp.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fashionapp.Entity.HashTag;
import com.fashionapp.Repository.HashTagRepository;
import com.fashionapp.service.HashTagService;

@Service
public class HashTagServiceImpl implements HashTagService {
	
	@Autowired
	private HashTagRepository hashTagRepository;

	@Override
	public HashTag save(HashTag hashtag) {
 		return hashTagRepository.save(hashtag);
	}

}
