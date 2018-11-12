package com.fashionapp.service;

import com.fashionapp.Entity.BlockedData;

public interface BlockedDataService {

	void deleteById(Long id);

	BlockedData findByUserId(Long userid);

	BlockedData save(BlockedData blockedDetails);

	BlockedData findByFileId(Long fileid);

}
