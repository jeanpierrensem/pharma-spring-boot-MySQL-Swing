package com.officine.losto.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.officine.losto.backend.entity.ReceptionLigne;
import com.officine.losto.backend.entity.Vente;
import com.officine.losto.backend.entity.VenteLigne;



public interface VenteLigneRepository extends JpaRepository<VenteLigne, Long> {
   
     Optional<VenteLigne> findById(Long id); 
     //long deleteByLigneVente(LigneVente ligneVente );
     List<VenteLigne> findByVente(Vente  vente); 
       
     @Query("SELECT r FROM VenteLigne r WHERE r.vente.id = :id")
     List<VenteLigne> findByVenteLigneId(@Param("id") Long id);
     
     
     
 }

