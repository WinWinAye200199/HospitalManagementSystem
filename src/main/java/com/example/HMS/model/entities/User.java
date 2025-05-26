package com.example.HMS.model.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="name", columnDefinition= "VARCHAR(255) COLLATE utf8_general_ci")
	private String name;
	
	@Column(name = "email", columnDefinition = "VARCHAR(255) COLLATE utf8_general_ci", unique = true)
	private String email;
	
	@Column(name = "phone")
	private String phone;
	
	@Column(name = "address")
	private String address;
	
	@Column(name = "password")
	private String password;
	
	@Column(name = "active", nullable = false)
	private boolean active = true;
	
	@Enumerated(EnumType.STRING)
    private Role role; // ADMIN or STAFF	


}
