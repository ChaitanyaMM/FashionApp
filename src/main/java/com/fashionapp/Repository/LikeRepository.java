package com.fashionapp.Repository;

import org.springframework.data.repository.CrudRepository;

import com.fashionapp.Entity.Likes;

public interface LikeRepository extends CrudRepository<Likes, Long>{

}
