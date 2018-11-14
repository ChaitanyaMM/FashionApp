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

import com.fasterxml.jackson.annotation.JsonFormat;

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
 	private String firstName;
	private String lastName;
 	private String email;
 	private String password;
 	private String description;
 	private String phoneNo;
	
	@Column(name = "dob")
	@Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
	private Date dob; 
	
	@Enumerated(EnumType.ORDINAL)
	private Gender gender;
	
	private String profileImageName;

	private String profileImageUrl;
	
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
	
	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
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

	public String getProfileImageName() {
		return profileImageName;
	}

	public void setProfileImageName(String profileImageName) {
		this.profileImageName = profileImageName;
	}

	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}

	public Date getSignup_time() {
		return signup_time;
	}

	public void setSignup_time(Date signup_time) {
		this.signup_time = signup_time;
	}

	
	 
}
