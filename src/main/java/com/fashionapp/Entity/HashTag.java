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
	private Type type;

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

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
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

	public HashTag(Long id, String hashTag, Type type, Long fileId, Date hashtage_time) {
		super();
		this.id = id;
		this.hashTag = hashTag;
		this.type = type;
		this.fileId = fileId;
		this.hashtage_time = hashtage_time;
	}

}
