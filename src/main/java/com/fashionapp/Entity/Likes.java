package com.fashionapp.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "likes")
public class Likes {

	@Id
	@Column(unique = true)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private Long userId;

	private Long videoId;

	@Column(columnDefinition = "tinyint(1) default 0")
	private boolean liked;

	@Column(columnDefinition = "tinyint(1) default 0")
	private boolean disLiked;

	public Likes() {
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getVideoId() {
		return videoId;
	}

	public void setVideoId(Long videoId) {
		this.videoId = videoId;
	}

	public boolean isLiked() {
		return liked;
	}

	public void setLiked(boolean liked) {
		this.liked = liked;
	}

	public boolean isDisLiked() {
		return disLiked;
	}

	public void setDisLiked(boolean disLiked) {
		this.disLiked = disLiked;
	}

	/**
	 * 
	 * @param userId
	 * @param videoId
	 * @param liked
	 * @param disLiked
	 */
	public Likes(Long userId, Long videoId, boolean liked, boolean disLiked) {
		this.userId = userId;
		this.videoId = videoId;
		this.liked = liked;
		this.disLiked = disLiked;
	}
}
