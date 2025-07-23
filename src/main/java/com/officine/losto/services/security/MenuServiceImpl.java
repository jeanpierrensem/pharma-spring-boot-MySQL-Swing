package com.officine.losto.services.security;

import java.util.List;

import org.springframework.stereotype.Service;

import com.officine.losto.backend.entity.AppMenu;
import com.officine.losto.backend.repository.AppMenuRepository;


@Service
public non-sealed class MenuServiceImpl implements MenuService {
	private AppMenuRepository appMenuRepository ;
  
    public MenuServiceImpl(AppMenuRepository appMenuRepository) {
        this.appMenuRepository = appMenuRepository;    
    }

    @Override
    public AppMenu save(AppMenu appMenu) {
        return appMenuRepository.save(appMenu);
    }

    @Override
    public AppMenu loadMenuByName(String menuName) {
        return appMenuRepository.findByMenuName(menuName);
    }

    @Override
    public List<AppMenu> listMenus() {
        return appMenuRepository.findAll();
    }

	@Override
	public void remove(AppMenu groupe) {
		appMenuRepository.delete(groupe);

	}

	@Override
	public AppMenu saveAndFlush(AppMenu appMenu) {
		// TODO Auto-generated method stub
		return appMenuRepository.saveAndFlush(appMenu);
	}

	@Override
	public List<AppMenu> findMenuByCriteria(String menuDescription, String menuName) {
		// TODO Auto-generated method stub
		return appMenuRepository.findByMenuDescriptionContainingOrMenuNameContaining(menuDescription, menuName);
	}
}
