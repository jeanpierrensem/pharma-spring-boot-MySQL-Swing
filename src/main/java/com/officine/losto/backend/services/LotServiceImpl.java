package com.officine.losto.backend.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.officine.losto.backend.entity.Lot;
import com.officine.losto.backend.repository.LotRepository;


@Service
public non-sealed class LotServiceImpl implements LotService {
	private LotRepository lotRepository ;
  
    public LotServiceImpl(LotRepository lotRepository) {
        this.lotRepository = lotRepository;    
    }

    @Override
    public Lot save(Lot lot) {
        return lotRepository.save(lot);
    }

    @Override
    public Lot loadLotByName(String numeroLot) {
        return lotRepository.findByNumeroLot(numeroLot);
    }

    @Override
    public List<Lot> listLots() {
        return lotRepository.findAll();
    }

	@Override
	public void remove(Lot lot) {
		lotRepository.delete(lot);

	}

	@Override
	public Lot saveAndFlush(Lot lot) {
		// TODO Auto-generated method stub
		return lotRepository.saveAndFlush(lot);
	}

	@Override
	public List<Lot> findLotByCriteria(String numeroLot) {
		// TODO Auto-generated method stub
		return lotRepository.findByNumeroLotContaining(numeroLot);
	}

	@Override
	public Lot loadLotById(Long id) {
		// TODO Auto-generated method stub
		return lotRepository.findById(id).get();
	}
}
