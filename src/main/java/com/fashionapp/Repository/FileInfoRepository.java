package com.fashionapp.Repository;

import org.springframework.data.repository.CrudRepository;

import com.fashionapp.Entity.FileInfo;

public interface FileInfoRepository extends CrudRepository<FileInfo, Long> {

}
