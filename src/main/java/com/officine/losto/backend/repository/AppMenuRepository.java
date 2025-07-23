package com.officine.losto.backend.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.officine.losto.backend.entity.AppMenu;


public interface AppMenuRepository extends JpaRepository<AppMenu, Long> {
     AppMenu findByMenuName (String menuName) ;
     List<AppMenu> findByMenuDescriptionContainingOrMenuNameContaining(String menuDescription, String menuName); 
     
     

}
