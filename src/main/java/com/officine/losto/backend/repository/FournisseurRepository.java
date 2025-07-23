package com.officine.losto.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.officine.losto.backend.entity.Fournisseur;


public interface FournisseurRepository extends JpaRepository<Fournisseur, Long> {
	Fournisseur findByFournisseurName (String fournisseurName) ;
	Optional<Fournisseur> findById (Long id  ) ;
     List<Fournisseur> findByFournisseurAdresseContainingOrFournisseurNameContaining(String fournisseurAdresse, String fournisseurName); 
     
     

}
