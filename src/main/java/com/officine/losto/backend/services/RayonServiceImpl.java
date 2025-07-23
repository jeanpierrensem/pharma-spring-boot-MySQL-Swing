package com.officine.losto.backend.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.officine.losto.backend.entity.Rayon;
import com.officine.losto.backend.repository.RayonRepository;


@Service
public non-sealed class RayonServiceImpl implements RayonService {
	private RayonRepository rayonRepository ;
  
    public RayonServiceImpl(RayonRepository rayonRepository) {
        this.rayonRepository = rayonRepository;    
    }

    @Override
    public Rayon save(Rayon Rayon) {
        return rayonRepository.save(Rayon);
    }

    @Override
    public Rayon loadRayonByName(String RayonName) {
        return rayonRepository.findByRayonName(RayonName);
    }

    @Override
    public List<Rayon> listRayons() {
        return rayonRepository.findAll();
    }

	@Override
	public void remove(Rayon groupe) {
		rayonRepository.delete(groupe);

	}

	@Override
	public Rayon saveAndFlush(Rayon Rayon) {
		// TODO Auto-generated method stub
		return rayonRepository.saveAndFlush(Rayon);
	}

	@Override
	public List<Rayon> findRayonByCriteria(String rayonDescription, String rayonName) {
		// TODO Auto-generated method stub
		return rayonRepository.findByRayonDescriptionContainingOrRayonNameContaining(rayonDescription, rayonName);
	}

	@Override
	public Rayon loadRayonById(Long id) {
		// TODO Auto-generated method stub
		return rayonRepository.findById(id).get();
	}
}
