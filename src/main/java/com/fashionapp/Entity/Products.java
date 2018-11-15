package com.fashionapp.Entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fashionapp.Enum.CategoryType;
import com.fashionapp.Enum.ProductTypes;

@Entity
@Table(name ="products")
public class Products implements Serializable{
 
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String name;
	private String productImage;
	private String productImageUrl;
	private String partnerName;
	private String partnerUrl;
	private String price;
	private String brand;
	private String size;
	
	@Column( columnDefinition = "tinyint(1) default 0")
	@Enumerated(value = EnumType.ORDINAL)
	private ProductTypes ProductType;
	
	@Column( columnDefinition = "tinyint(1) default 0")
	@Enumerated(value = EnumType.ORDINAL)
	private CategoryType categoryType;
	
	
	/*@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id", nullable = false)
	private ProductCategory productCategory;*/

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProductImage() {
		return productImage;
	}

	public void setProductImage(String productImage) {
		this.productImage = productImage;
	}

	public String getProductImageUrl() {
		return productImageUrl;
	}

	public void setProductImageUrl(String productImageUrl) {
		this.productImageUrl = productImageUrl;
	}

	public String getPartnerName() {
		return partnerName;
	}

	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
	}

	public String getPartnerUrl() {
		return partnerUrl;
	}

	public void setPartnerUrl(String partnerUrl) {
		this.partnerUrl = partnerUrl;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public ProductTypes getProductType() {
		return ProductType;
	}

	public void setProductType(ProductTypes productType) {
		ProductType = productType;
	}

	public CategoryType getCategoryType() {
		return categoryType;
	}

	public void setCategoryType(CategoryType categoryType) {
		this.categoryType = categoryType;
	}

 


	/*public ProductCategory getProductCategory() {
		return productCategory;
	}



	public void setProductCategory(ProductCategory productCategory) {
		this.productCategory = productCategory;
	}*/
	

}
