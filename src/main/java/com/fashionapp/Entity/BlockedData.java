package com.fashionapp.Entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

 
  /*This object is to block the user/data by the ADMIN */
  
@Entity
@Table(name = "blocked_data")
public class BlockedData implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO) 
	private Long id;
	private Long userId;
	private String userName;
	private Long fileId;
	private Long adminId;
 	private String reason;
 	
	
	@Column( columnDefinition = "tinyint(1) default 3")
	@Enumerated(value = EnumType.ORDINAL)
	private Role role;
	 
	
	@Column(name = "blocked_time", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date blocked_time;
	
	@PrePersist
	protected void onCreate() {
		blocked_time = new Date();
	}
 
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getFileId() {
		return fileId;
	}

	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}

	public Long getAdminId() {
		return adminId;
	}

	public void setAdminId(Long adminId) {
		this.adminId = adminId;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role type) {
		this.role = type;
	}
 
	public Date getBlocked_time() {
		return blocked_time;
	}

	public void setBlocked_time(Date blocked_time) {
		this.blocked_time = blocked_time;
	}

 
	 
}
