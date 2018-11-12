package com.fashionapp.Entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "followers_group")
public class FollowersGroup {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private Long userId;
//	private long followeruserId;
	private String groupName;
	private String userEmail;
	
	
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
	/*public long getFolloweruserId() {
		return followeruserId;
	}
	public void setFolloweruserId(long followeruserId) {
		this.followeruserId = followeruserId;
	}*/
	public String getGroupname() {
		return groupName;
	}
	public void setGroupname(String groupname) {
		this.groupName = groupname;
	}
	public String getUseremail() {
		return userEmail;
	}
	public void setUseremail(String useremail) {
		this.userEmail = useremail;
	}
	 


}
