package com.fashionapp.Entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

@Entity
@Table(name="promotions")
public class Promotions implements Serializable{

	private static final long serialVersionUID = -6066627935775546690L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String imageName;
	private String imageUrl;
	private Date promotion_time;
	
	@PrePersist
	protected void onCreate() {
		promotion_time = new Date();
	}

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public Date getPromotion_time() {
		return promotion_time;
	}

	public void setPromotion_time(Date promotion_time) {
		this.promotion_time = promotion_time;
	}
	
}
