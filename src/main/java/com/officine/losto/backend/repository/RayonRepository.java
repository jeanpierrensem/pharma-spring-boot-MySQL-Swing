package com.officine.losto.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.officine.losto.backend.entity.Rayon;


public interface RayonRepository extends JpaRepository<Rayon, Long> {
     Rayon findByRayonName (String rayonName) ;
     Optional<Rayon> findById(Long id ); 
     List<Rayon> findByRayonDescriptionContainingOrRayonNameContaining(String rayonDescription, String rayonName); 
     
     

}
