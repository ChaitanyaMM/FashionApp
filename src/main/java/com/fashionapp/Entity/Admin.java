package com.fashionapp.Entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "admin")
public class Admin {
	
	
	private Long id;
	private String name;
	private String email;
	private String password;
	private String description;
	

}
