package com.officine.losto.services.security;

import java.util.List;
import org.springframework.stereotype.Service;

import com.officine.losto.backend.entity.AppRole;
import com.officine.losto.backend.repository.AppRoleRepository;


@Service
public non-sealed  class RoleServiceImpl  implements RoleService{

	private AppRoleRepository appRoleRepository;

	public RoleServiceImpl(AppRoleRepository appRoleRepository) {
		this.appRoleRepository = appRoleRepository;
	}

	@Override
	public AppRole addNewRole(AppRole appRole) {
		return appRoleRepository.save(appRole);
	}

	@Override
	public AppRole loadRoleByName(String rolename) {

		return appRoleRepository.findByRolename(rolename);
	}

	@Override
	public List<AppRole> listRoles() {
		return appRoleRepository.findAll();
	}

	@Override
	public void deleteRole(AppRole appRole) {
	appRoleRepository.delete(appRole);
	}
}
