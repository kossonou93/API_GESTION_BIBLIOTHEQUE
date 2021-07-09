package com.gsee.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(	name = "reservations")
public class Reservation extends AuditModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Date debut;

	private Date fin;
	
	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "user_id")
	private Etudiant etudiant;
	
	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "livre_id")
	private Livre livre;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Reservation(Date debut, Date fin, Etudiant etudiant) {
		super();
		this.debut = debut;
		this.fin = fin;
		this.etudiant = etudiant;
	}
	public Date getDebut() {
		return debut;
	}
	public void setDebut(Date debut) {
		this.debut = debut;
	}
	public Date getFin() {
		return fin;
	}
	public void setFin(Date fin) {
		this.fin = fin;
	}
	public Etudiant getEtudiant() {
		return etudiant;
	}
	public void setEtudiant(Etudiant etudiant) {
		this.etudiant = etudiant;
	}
	
	public Reservation() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Livre getLivre() {
		return livre;
	}
	public void setLivre(Livre livre) {
		this.livre = livre;
	}
	public Reservation(Date debut, Date fin, Etudiant etudiant, Livre livre) {
		super();
		this.debut = debut;
		this.fin = fin;
		this.etudiant = etudiant;
		this.livre = livre;
	}
	
	
}
