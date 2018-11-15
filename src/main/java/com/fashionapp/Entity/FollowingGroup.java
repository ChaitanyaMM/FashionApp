package com.fashionapp.Entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
	
	/*creates the default following group for individual user*/
@Entity
@Table(name = "following_group")
public class FollowingGroup {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private Long userId;
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
//
//	public long getFollowinguserId() {
//		return followinguserId;
//	}
//
//	public void setFollowinguserId(long followinguserId) {
//		this.followinguserId = followinguserId;
//	}

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

//	public String getFollowiguseremail() {
//		return followiguseremail;
//	}
//
//	public void setFollowiguseremail(String followiguseremail) {
//		this.followiguseremail = followiguseremail;
//	}

	 

}
