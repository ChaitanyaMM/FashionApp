package com.fashionapp.service;

import java.util.List;

import com.fashionapp.Entity.BlockedUsers;

public interface BlockedUsersService {

	List<BlockedUsers> findByuserId(long userId);

	BlockedUsers findByUserIdAndBlockedUserId(long id, long id2);

	void deleteById(long id);

	BlockedUsers save(BlockedUsers blockedUsers);

}
