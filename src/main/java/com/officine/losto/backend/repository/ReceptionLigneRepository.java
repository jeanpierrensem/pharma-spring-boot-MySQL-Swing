package com.officine.losto.backend.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.officine.losto.backend.entity.CommandeLigne;
import com.officine.losto.backend.entity.ReceptionLigne;


public interface ReceptionLigneRepository extends JpaRepository<ReceptionLigne, Long> {  
     Optional<ReceptionLigne> findById(Long id);  
     long deleteByCommandeLigne(CommandeLigne ligneCommande);
     List<ReceptionLigne> findByCommandeLigne(CommandeLigne ligneCommande);    
    
     
    /* @Query("SELECT r FROM ReceptionLigne r WHERE r.commandeLigne.id = :id")
     List<ReceptionLigne> findByCommandeLigneId(@Param("id") Long id);*/
  }
