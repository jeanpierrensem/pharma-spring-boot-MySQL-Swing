package com.officine.losto.services.security;

import java.util.List;

import com.officine.losto.backend.entity.AppMenu;
import com.officine.losto.util.annotation.TransactionalReadOnly;
import com.officine.losto.util.annotation.TransactionalWrite;

public sealed interface  MenuService permits MenuServiceImpl {
	   @TransactionalWrite
	   AppMenu  save(AppMenu appMenu) ;
	   @TransactionalWrite
	   AppMenu  saveAndFlush(AppMenu appMenu) ;
	   @TransactionalWrite
	   void remove(AppMenu appMenu);
	   @TransactionalReadOnly
	   AppMenu loadMenuByName (String menuName);
	   @TransactionalReadOnly
	   List<AppMenu> listMenus();
	   @TransactionalReadOnly
	   List<AppMenu> findMenuByCriteria(String menuDescription, String menuName); 
}
