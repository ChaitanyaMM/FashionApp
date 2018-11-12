package com.fashionapp.Repository;

import org.springframework.data.repository.CrudRepository;

import com.fashionapp.Entity.BlockedData;

public interface BlockedDataRepository extends CrudRepository<BlockedData, Long>{

	BlockedData findByUserId(Long userid);

	BlockedData findByFileId(Long fileid);

}
