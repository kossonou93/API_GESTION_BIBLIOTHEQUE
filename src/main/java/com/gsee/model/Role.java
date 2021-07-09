package com.gsee.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import com.gsee.model.AuditModel;
import com.gsee.model.ERole;


@Entity
@Table(name = "roles")
public class Role extends AuditModel{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@Enumerated(EnumType.STRING)
	@Column(length = 255)
	private ERole name;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public ERole getName() {
		return name;
	}

	public void setName(ERole name) {
		this.name = name;
	}

	public Role(ERole name) {
		this.name = name;
	}

	public Role() {
		// TODO Auto-generated constructor stub
	}
}
