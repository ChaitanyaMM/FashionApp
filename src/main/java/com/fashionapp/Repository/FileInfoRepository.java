package com.fashionapp.Repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.fashionapp.Entity.FileInfo;
import com.fashionapp.Entity.UserInfo;

public interface FileInfoRepository extends CrudRepository<FileInfo, Long> {

	List<FileInfo> findByUserid(Long id);

}
