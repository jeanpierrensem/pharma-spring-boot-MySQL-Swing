package com.officine.losto.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.officine.losto.backend.entity.Commande;


public interface CommandeRepository extends JpaRepository<Commande, Long> {
     Optional<Commande> findById(Long id); 
     List<Commande> findByCommandeNumeroContainingOrCommandeDateContaining(String commandeNumero,  String commandeDate); 
     Commande findByCommandeNumero(String commandeNumero); 
     //List<Commande> findTypeByCriteria(String commandeNumero, String commandePar, String commandeFournisseur); 
}
