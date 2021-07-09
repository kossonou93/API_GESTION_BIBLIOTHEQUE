package com.gsee.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="livres")
public class Livre extends AuditModel{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String libelle;
	
	private String auteur;
	
	private String ouvrage;
	
	private Boolean etat;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public String getAuteur() {
		return auteur;
	}

	public void setAuteur(String auteur) {
		this.auteur = auteur;
	}

	public String getOuvrage() {
		return ouvrage;
	}

	public void setOuvrage(String ouvrage) {
		this.ouvrage = ouvrage;
	}

	public Boolean getEtat() {
		return etat;
	}

	public void setEtat(Boolean etat) {
		this.etat = etat;
	}

	public Livre() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	
}
