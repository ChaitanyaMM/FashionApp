package com.fashionapp.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fashionapp.Entity.Comments;
import com.fashionapp.Repository.CommentsRepository;
import com.fashionapp.service.CommentsService;

@Service
public class CommentsServiceImpl implements CommentsService{
	
	@Autowired
	private CommentsRepository commentsRepository;

	@Override
	public void deleteById(Long id) {
		commentsRepository.deleteById(id);
		
	}

	@Override
	public Comments findByUserIdAndVideoId(Long userId, Long fileId) {
 		return commentsRepository.findByUserIdAndVideoId(userId, fileId);
	}

	@Override
	public Comments save(Comments comentsObject) {
 		return commentsRepository.save(comentsObject);
	}

}
