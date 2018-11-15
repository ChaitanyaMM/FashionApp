package com.fashionapp.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "productcategory")
public class ProductCategory {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "product_type")
	private String productType;
	
	@Column(name  = "subCategoryId") //f.k 
	private Long subCategoryId;
	
	
/*	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "subCategoryId", nullable = false)
	private SubCategory subCategory;*/
	
	 

	public Long getProductId() {
		return id;
	}


	public void setProductId(Long productId) {
		this.id = productId;
	}


	public String getProductType() {
		return productType;
	}


	public void setProductType(String productType) {
		this.productType = productType;
	}


	public Long getSubCategoryId() {
		return subCategoryId;
	}


	public void setSubCategoryId(Long subCategoryId) {
		this.subCategoryId = subCategoryId;
	}


	/*public SubCategory getSubCategory() {
		return subCategory;
	}


	public void setSubCategory(SubCategory subCategory) {
		this.subCategory = subCategory;
	}
*/
 
	
	

}
