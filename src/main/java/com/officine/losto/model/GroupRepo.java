package com.officine.losto.model;

import com.officine.losto.entity.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.*;

import java.util.*;

public interface GroupRepo extends JpaRepository<AppGroup, Long> {

    @Query("SELECT DISTINCT g FROM AppGroup g LEFT JOIN FETCH g.menus WHERE g.id = :id")
    Optional<AppGroup> findByIdWithMenus(@Param("id") Long id);

    @Query("SELECT DISTINCT g FROM AppGroup g LEFT JOIN FETCH g.menus")
    List<AppGroup> findAllWithMenus();

    List<AppGroup> findByDescriptionContainingOrNameContaining(String description, String name);

    //void deleteById(Long id);

    @Query("SELECT u  FROM AppGroup u where u.name = ?1")
    AppGroup findGroupByName(String name);

	/*@Modifying
	@Transactional
	@Query(value = "UPDATE AppGroupe u1  set " + "u1.isAfficher = :isAfficher, " + "u1.isEnregistrer = :isEnregistrer, "
			+ "u1.isModifier = :isModifier,  " + "u1.isSupprimer = :isSupprimer,  " + "u1.isImprimer = :isImprimer "
			+ "where u1.groupeCode = :groupeCode " + " And menueId = :menueId ")
	int updateGroupeRoles(@Param("isAfficher") boolean isAfficher, @Param("isEnregistrer") boolean isEnregistrer,
			@Param("isModifier") boolean isModifier, @Param("isSupprimer") boolean isSupprimer,
			@Param("isImprimer") boolean isImprimer, @Param("groupeCode") String groupeCode,
			@Param("menueId") Long menueId);*/
}
