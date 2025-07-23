package com.officine.losto.services.security;

import java.util.List;

import com.officine.losto.backend.entity.AppRole;


public sealed interface  RoleService permits RoleServiceImpl {

	   AppRole  addNewRole(AppRole appRole) ;
	   void  deleteRole(AppRole appRole) ;
	   AppRole loadRoleByName (String rolename);
	   List<AppRole> listRoles();

}
