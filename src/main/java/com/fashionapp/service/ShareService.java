package com.fashionapp.service;

 import java.util.List;

import com.fashionapp.Entity.Share;

public interface ShareService {

	Share save(Share shareObject);

	List<Share> findByUserId(Long userId);

}
