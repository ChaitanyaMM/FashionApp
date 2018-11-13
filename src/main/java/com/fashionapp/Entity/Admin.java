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

import io.swagger.annotations.ApiModelProperty;

@Entity
@Table(name = "admin")
public class Admin implements Serializable{
	
 	private static final long serialVersionUID = 5644291308701001712L;
	 
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@ApiModelProperty(example = "admin-userName", required = true, value = "")
	private String userName;
	@ApiModelProperty(example = "admin-firstName", required = true, value = "")
	private String firstName;
	private String lastName;
	@ApiModelProperty(example = "email", required = true, value = "")
	private String email;
	@ApiModelProperty(example = "password", required = true, value = "")
	private String password;
	@ApiModelProperty(example = "description", required = true, value = "")
	private String description;
	@ApiModelProperty(example = "phoneNo", required = true, value = "")
	private String phoneNo;
	
	@Enumerated(EnumType.ORDINAL)
	private Gender gender;
	
	@Column(name = "signup_time", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date signup_time;
	
	@PrePersist
	protected void onCreate() {
		signup_time = new Date();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
 
	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public Date getSignup_time() {
		return signup_time;
	}

	public void setSignup_time(Date signup_time) {
		this.signup_time = signup_time;
	}

	
	 
}
