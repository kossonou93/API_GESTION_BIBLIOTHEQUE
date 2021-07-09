package com.gsee.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Admin")
public class Admin extends User{

	public Admin() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Admin(String nom, String prenom, String username, String email, String password, String contact) {
		super(nom, prenom, username, email, password, contact);
		// TODO Auto-generated constructor stub
	}
	
	
}
