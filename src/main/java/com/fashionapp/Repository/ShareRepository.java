package com.fashionapp.Repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.fashionapp.Entity.Share;

public interface ShareRepository extends CrudRepository<Share, Long>{

	List<Share> findByUserId(Long userId);

}
