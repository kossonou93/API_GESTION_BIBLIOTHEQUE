package com.gsee.model;

import java.util.Set;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;


@Entity
@DiscriminatorValue("Etudiant")
public class Etudiant extends User{

	public Etudiant() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Etudiant(String nom, String prenom, String username, String email, String password, String contact) {
		super(nom, prenom, username, email, password, contact);
		// TODO Auto-generated constructor stub
	}
	
	
}
