package com.fashionapp.service;

import java.util.List;
import java.util.Optional;

import com.fashionapp.Entity.FileInfo;

public interface FileInfoService {

	Optional<FileInfo> findById(Long fileid);

	void deleteById(Long id);

	List<FileInfo> findByUserId(Long id);

	FileInfo save(FileInfo fileInfo);

}
