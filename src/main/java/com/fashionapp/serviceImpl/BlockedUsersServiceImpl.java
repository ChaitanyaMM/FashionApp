package com.fashionapp.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fashionapp.Entity.BlockedUsers;
import com.fashionapp.Repository.BlockedUserRepository;
import com.fashionapp.service.BlockedUsersService;

@Service
public class BlockedUsersServiceImpl implements BlockedUsersService{
	
	@Autowired
	private BlockedUserRepository blockedUserRepository;

	@Override
	public List<BlockedUsers> findByuserId(long userId) {
 		return blockedUserRepository.findByuserId(userId);
	}

	@Override
	public BlockedUsers findByUserIdAndBlockedUserId(long userId, long blockedUserId) {
 		return blockedUserRepository.findByUserIdAndBlockedUserId(userId, blockedUserId);
	}

	@Override
	public void deleteById(long id) {
		blockedUserRepository.deleteById(id);
	}

	@Override
	public BlockedUsers save(BlockedUsers blockedUsers) {
 		return blockedUserRepository.save(blockedUsers);
	}

}
