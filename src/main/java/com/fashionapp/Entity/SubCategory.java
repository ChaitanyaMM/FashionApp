package com.fashionapp.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "subcategory")
public class SubCategory {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "subcategory_type")
	private String subCategoryType;
	
	private Long categoryId; //f.k
	/*
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "categoryId", nullable = false)
	private Category category;*/

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSubCategoryType() {
		return subCategoryType;
	}

	public void setSubCategoryType(String subCategoryType) {
		this.subCategoryType = subCategoryType;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	/*public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}*/
	
	 

	 
	

}
