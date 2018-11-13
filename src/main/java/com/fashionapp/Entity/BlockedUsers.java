package com.fashionapp.Entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "blocked_users")
public class BlockedUsers {
	
  	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private Long userId;
	private Long blockedUserId;
	private String userName;
	private String blockedUserName;
	private String groupName;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getBlockedUserId() {
		return blockedUserId;
	}
	public void setBlockedUserId(Long blockedUserId) {
		this.blockedUserId = blockedUserId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getBlockedUserName() {
		return blockedUserName;
	}
	public void setBlockedUserName(String blockedUserName) {
		this.blockedUserName = blockedUserName;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
 
 
}
