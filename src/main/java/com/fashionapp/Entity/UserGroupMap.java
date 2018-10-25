package com.fashionapp.Entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="usergroup_map")
public class UserGroupMap implements Serializable{
	
	
	
	/*TO-DO 
	 follow/unfollow is mappedto group and save to group;
	 if unmapped unammpe from group and delte from group;*/

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	private long userId;
	private long groupId;
	/*private String username;
	private String useremail;
	private String phoneno;*/
	private boolean isMapped;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public long getGroupId() {
		return groupId;
	}
	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}
	/*public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUseremail() {
		return useremail;
	}
	public void setUseremail(String useremail) {
		this.useremail = useremail;
	}
	public String getPhoneno() {
		return phoneno;
	}
	public void setPhoneno(String phoneno) {
		this.phoneno = phoneno;
	}*/
	public boolean isMapped() {
		return isMapped;
	}
	public void setMapped(boolean isMapped) {
		this.isMapped = isMapped;
	}
	

}
