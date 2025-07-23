package com.officine.losto.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.officine.losto.backend.entity.Packaging;


public interface PackagingRepository extends JpaRepository<Packaging, Long> {
	Packaging findByPackagingName (String packagingName) ;
     Optional<Packaging> findById(Long id ); 
     List<Packaging> findByPackagingDescriptionContainingOrPackagingNameContaining(String packagingDescription, String packagingName); 
     
     

}
