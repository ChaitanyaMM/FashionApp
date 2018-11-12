package com.fashionapp.Entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "products")
public class Products {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long productId;
	
	@Column(name = "product_type")
	private String productType;
	
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "subCategoryId", nullable = false)
	private SubCategory subCategory;
	
	/*@OneToMany(fetch = FetchType.EAGER,mappedBy = "id",cascade = CascadeType.ALL)
	private Set<WishList> wishLists;*/

	public Long getProductId() {
		return productId;
	}


	public void setProductId(Long productId) {
		this.productId = productId;
	}


	public String getProductType() {
		return productType;
	}


	public void setProductType(String productType) {
		this.productType = productType;
	}


	public SubCategory getSubCategory() {
		return subCategory;
	}


	public void setSubCategory(SubCategory subCategory) {
		this.subCategory = subCategory;
	}


//	public Set<WishList> getWishLists() {
//		return wishLists;
//	}
//
//
//	public void setWishLists(Set<WishList> wishLists) {
//		this.wishLists = wishLists;
//	}
	
	
	

}
