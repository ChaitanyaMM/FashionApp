package com.fashionapp.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fashionapp.Entity.HashtagVideoMap;
import com.fashionapp.Repository.HashtagVideoMapRepository;
import com.fashionapp.service.HashtagVideoMapService;

@Service
public class HashtagVideoMapServiceImpl implements HashtagVideoMapService {
	
	@Autowired
	private HashtagVideoMapRepository hashtagVideoMapRepository;

	@Override
	public HashtagVideoMap save(HashtagVideoMap mappingtag) {
 		return hashtagVideoMapRepository.save(mappingtag);
	}

}
