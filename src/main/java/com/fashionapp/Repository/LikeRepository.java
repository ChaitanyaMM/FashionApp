package com.fashionapp.Repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.fashionapp.Entity.Likes;

public interface LikeRepository extends CrudRepository<Likes, Long>{
	
	 Likes findByUserIdAndVideoId(Long userId,Long fileId);
	 
	 List<Likes> findByVideoId(Long fileId);

 
}
