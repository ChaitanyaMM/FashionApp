package com.fashionapp.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fashionapp.Entity.BlockedData;
import com.fashionapp.Repository.BlockedDataRepository;
import com.fashionapp.service.BlockedDataService;

@Service
public class BlockedDataServiceImpl implements BlockedDataService {

	@Autowired
	private BlockedDataRepository blockedDataRepository;

	@Override
	public void deleteById(Long id) {
		blockedDataRepository.deleteById(id);		
	}

	@Override
	public BlockedData findByUserId(Long userid) {
 		return blockedDataRepository.findByUserId(userid);
	}

	@Override
	public BlockedData save(BlockedData blockedDetails) {
 		return blockedDataRepository.save(blockedDetails);
	}

	@Override
	public BlockedData findByFileId(Long fileid) {
 		return blockedDataRepository.findByFileId(fileid);
	}
	
}
