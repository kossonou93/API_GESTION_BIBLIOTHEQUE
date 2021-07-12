package com.gsee.request;

public class ReservationRequest {

	private String etudiant;
	private Long livre;
	private String debut;
	private String fin;
	private Boolean etat;
	public String getDebut() {
		return debut;
	}
	public void setDebut(String debut) {
		this.debut = debut;
	}
	public String getFin() {
		return fin;
	}
	public void setFin(String fin) {
		this.fin = fin;
	}
	public String getEtudiant() {
		return etudiant;
	}
	public void setEtudiant(String etudiant) {
		this.etudiant = etudiant;
	}

	public Long getLivre() {
		return livre;
	}
	public void setLivre(Long livre) {
		this.livre = livre;
	}
	public Boolean getEtat() {
		return etat;
	}
	public void setEtat(Boolean etat) {
		this.etat = etat;
	}
	
	
}
