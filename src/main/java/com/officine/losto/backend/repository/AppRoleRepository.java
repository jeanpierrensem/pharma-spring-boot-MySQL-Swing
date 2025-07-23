package com.officine.losto.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.officine.losto.backend.entity.AppRole;




public interface AppRoleRepository extends JpaRepository<AppRole, Long> {
	 AppRole findByRolename (String rolename) ;

}
