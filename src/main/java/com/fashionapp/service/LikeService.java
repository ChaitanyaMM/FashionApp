package com.fashionapp.service;

import java.util.List;

import com.fashionapp.Entity.Likes;

public interface LikeService {

	Likes findByUserIdAndVideoId(Long userId, Long fileId);

	Likes save(Likes likesObject);

	List<Likes> findByVideoId(Long videoId);

}
