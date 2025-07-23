package com.officine.losto.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.officine.losto.backend.entity.AppGroupe;

import jakarta.transaction.Transactional;

public interface AppGroupeRepository extends JpaRepository<AppGroupe, Long> {

	List<AppGroupe> findByGroupeDescriptionContainingOrGroupeNameContaining(String groupeDescription,
			String groupeName);

	void deleteByGroupeCode(String groupeCode);
	
	@Query("SELECT u  FROM AppGroupe u where u.groupeCode = ?1")
	List<AppGroupe> findByGroupeCode(String groupeCode);

	@Modifying
	@Transactional
	@Query(value = "UPDATE AppGroupe u1  set " + "u1.isAfficher = :isAfficher, " + "u1.isEnregistrer = :isEnregistrer, "
			+ "u1.isModifier = :isModifier,  " + "u1.isSupprimer = :isSupprimer,  " + "u1.isImprimer = :isImprimer "
			+ "where u1.groupeCode = :groupeCode " + " And menueId = :menueId ")
	int updateGroupeRoles(@Param("isAfficher") boolean isAfficher, @Param("isEnregistrer") boolean isEnregistrer,
			@Param("isModifier") boolean isModifier, @Param("isSupprimer") boolean isSupprimer,
			@Param("isImprimer") boolean isImprimer, @Param("groupeCode") String groupeCode,
			@Param("menueId") Long menueId);
}
