package com.fashionapp.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fashionapp.Entity.FileInfo;
import com.fashionapp.Repository.FileInfoRepository;
import com.fashionapp.service.FileInfoService;

@Service
public class FileInfoServiceImpl implements FileInfoService{

	@Autowired
	private FileInfoRepository fileInfoRepository;
	
	@Override
	public Optional<FileInfo> findById(Long fileid) {
 		return fileInfoRepository.findById(fileid);
	}

	@Override
	public void deleteById(Long id) {
		fileInfoRepository.deleteById(id);
		return;
	}

	@Override
	public List<FileInfo> findByUserId(Long id) {
 		return fileInfoRepository.findByUserId(id);
	}

	@Override
	public FileInfo save(FileInfo fileInfo) {
 		return fileInfoRepository.save(fileInfo);
	}

	 

}
