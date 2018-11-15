package com.fashionapp.service;

import com.fashionapp.Entity.HashTag;

public interface HashTagService {

	HashTag save(HashTag hashtag);

	HashTag findByfileId(Long fileId);

}
