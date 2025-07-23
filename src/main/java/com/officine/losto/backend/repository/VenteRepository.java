package com.officine.losto.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.officine.losto.backend.entity.Vente;


public interface VenteRepository extends JpaRepository<Vente, Long> {
     Optional<Vente> findById(Long id); 
     List<Vente> findByNumeroContainingOrVentedateContaining(String numero,  String date); 
     Vente findByNumero(String numero); 
  
}
