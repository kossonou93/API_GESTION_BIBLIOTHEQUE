package com.gsee.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Bibliotecaire")
public class Bibliotecaire extends User{

	public Bibliotecaire() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Bibliotecaire(String nom, String prenom, String username, String email, String password, String contact) {
		super(nom, prenom, username, email, password, contact);
		// TODO Auto-generated constructor stub
	}

	
	
}
