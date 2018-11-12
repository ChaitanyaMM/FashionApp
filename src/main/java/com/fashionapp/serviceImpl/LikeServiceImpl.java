package com.fashionapp.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fashionapp.Entity.Likes;
import com.fashionapp.Repository.LikeRepository;
import com.fashionapp.service.LikeService;

@Service
public class LikeServiceImpl implements LikeService{
	
	@Autowired
	private LikeRepository likeRepository;

	@Override
	public Likes findByUserIdAndVideoId(Long userId, Long fileId) {
 		return likeRepository.findByUserIdAndVideoId(userId, fileId);
	}

	@Override
	public Likes save(Likes likesObject) {
 		return likeRepository.save(likesObject);
	}

	@Override
	public List<Likes> findByVideoId(Long videoId) {
 		return likeRepository.findByVideoId(videoId);
	}

}
