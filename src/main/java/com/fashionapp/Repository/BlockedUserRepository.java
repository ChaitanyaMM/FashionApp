package com.fashionapp.Repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.fashionapp.Entity.BlockedUsers;

public interface BlockedUserRepository extends CrudRepository<BlockedUsers, Long>{
	
List<BlockedUsers>	findByuserId(long userId);

BlockedUsers findByUserIdAndBlockedUserId(long userId,long blockedUserId);

}
