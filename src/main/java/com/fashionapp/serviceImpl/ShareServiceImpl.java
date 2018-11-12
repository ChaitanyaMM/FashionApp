package com.fashionapp.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fashionapp.Entity.Share;
import com.fashionapp.Repository.ShareRepository;
import com.fashionapp.service.ShareService;

@Service
public class ShareServiceImpl implements ShareService{
	@Autowired
	private ShareRepository shareRepository;

	@Override
	public Share save(Share shareObject) {
		
 		return shareRepository.save(shareObject);
	}

	@Override
	public List<Share> findByUserId(Long userId) {
 		return shareRepository.findByUserId(userId);
	}

	
	
	
	
}
