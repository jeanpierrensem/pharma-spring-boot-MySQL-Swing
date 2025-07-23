package com.officine.losto.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.officine.losto.backend.entity.Seuil;


public interface SeuilRepository extends JpaRepository<Seuil, Long> {
     Seuil findBySeuilCode (String seuilCode) ;
     Optional<Seuil> findById(Long id); 
     List<Seuil> findBySeuilCodeContaining(String seuilCode); 
     
     



}
