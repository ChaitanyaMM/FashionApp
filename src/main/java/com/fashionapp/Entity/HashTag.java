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

import com.fashionapp.Enum.Role;

@Entity
@Table(name = "hashtags")
public class HashTag implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "hashtag_name")
	private String hashTag;

	@Column(columnDefinition = "tinyint(1) default 3")
	@Enumerated(value = EnumType.ORDINAL)
	private Role role;

	private Long fileId;

	@Column(name = "tagged_time")
	private Date hashtage_time;

	@PrePersist
	protected void onCreate() {
		hashtage_time = new Date();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getHashTag() {
		return hashTag;
	}

	public void setHashTag(String hashTag) {
		this.hashTag = hashTag;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role type) {
		this.role = type;
	}

	public Long getFileId() {
		return fileId;
	}

	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}

	public Date getHashtage_time() {
		return hashtage_time;
	}

	public void setHashtage_time(Date hashtage_time) {
		this.hashtage_time = hashtage_time;
	}

	
	
	public HashTag() {

	}

	public HashTag(Long id, String hashTag, Role role, Long fileId, Date hashtage_time) {
		super();
		this.id = id;
		this.hashTag = hashTag;
		this.role = role;
		this.fileId = fileId;
		this.hashtage_time = hashtage_time;
	}

}
