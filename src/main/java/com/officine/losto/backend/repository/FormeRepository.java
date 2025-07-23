package com.officine.losto.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.officine.losto.backend.entity.Forme;


public interface FormeRepository extends JpaRepository<Forme, Long> {
     Forme findByFormeName (String formeName) ;
     Optional<Forme> findById (Long id) ;
     List<Forme> findByFormeDescriptionContainingOrFormeNameContaining(String formeDescription, String formeName); 
     
     

}
