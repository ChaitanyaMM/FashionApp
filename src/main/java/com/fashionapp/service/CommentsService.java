package com.fashionapp.service;

import com.fashionapp.Entity.Comments;

public interface CommentsService {

	void deleteById(Long id);

	Comments findByUserIdAndVideoId(Long userId, Long fileId);

	Comments save(Comments comentsObject);

}
