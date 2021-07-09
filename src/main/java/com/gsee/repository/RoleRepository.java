package com.gsee.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gsee.model.ERole;
import com.gsee.model.Role;

public interface RoleRepository extends JpaRepository<Role, Integer>{

	Role findByName(ERole name);
}
