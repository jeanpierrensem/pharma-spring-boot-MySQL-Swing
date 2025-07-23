package com.officine.losto.backend.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.officine.losto.backend.entity.Seuil;
import com.officine.losto.backend.repository.SeuilRepository;


@Service
public non-sealed class SeuilServiceImpl implements SeuilService {
	private SeuilRepository seuilRepository ;
  
    public SeuilServiceImpl(SeuilRepository seuilRepository) {
        this.seuilRepository = seuilRepository;    
    }

    @Override
    public Seuil save(Seuil Seuil) {
        return seuilRepository.save(Seuil);
    }

   
    @Override
    public List<Seuil> listSeuils() {
        return seuilRepository.findAll();
    }

	@Override
	public void remove(Seuil seuil) {
		seuilRepository.delete(seuil);

	}

	@Override
	public Seuil saveAndFlush(Seuil Seuil) {
		// TODO Auto-generated method stub
		return seuilRepository.saveAndFlush(Seuil);
	}

	@Override
	public List<Seuil> findSeuilByCriteria(String seuilCode) {
		// TODO Auto-generated method stub
		return seuilRepository.findBySeuilCodeContaining(seuilCode);
	}

	@Override
	public Seuil loadSeuilByCode(String SeuilCode) {
		// TODO Auto-generated method stub
		return seuilRepository.findBySeuilCode(SeuilCode);
	}
	
	

}
