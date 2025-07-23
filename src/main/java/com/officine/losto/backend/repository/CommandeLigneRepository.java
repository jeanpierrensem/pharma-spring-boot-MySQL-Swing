package com.officine.losto.backend.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.officine.losto.backend.entity.Commande;
import com.officine.losto.backend.entity.CommandeLigne;
import com.officine.losto.backend.entity.ReceptionLigne;


public interface CommandeLigneRepository extends JpaRepository<CommandeLigne, Long> {
   
     Optional<CommandeLigne> findById(Long id); 
     long deleteByCommandeLigneCommande(Commande commande);
     List<CommandeLigne> findByCommandeLigneCommande(Commande commande); 
       
     @Query("SELECT r FROM ReceptionLigne r WHERE r.commandeLigne.id = :id")
     List<ReceptionLigne> findByCommandeLigneId(@Param("id") Long id);
 }
