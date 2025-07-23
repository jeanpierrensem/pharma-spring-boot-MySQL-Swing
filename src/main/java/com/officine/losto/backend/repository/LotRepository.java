package com.officine.losto.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.officine.losto.backend.entity.Lot;


public interface LotRepository extends JpaRepository<Lot, Long> {
    Optional<Lot> findById (Long id) ;
     Lot findByNumeroLot (String numeroLot) ;
     List<Lot> findByNumeroLotContaining(String numeroLot); 
     
     

}
