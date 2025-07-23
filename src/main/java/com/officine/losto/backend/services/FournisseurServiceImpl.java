package com.officine.losto.backend.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.officine.losto.backend.entity.Fournisseur;
import com.officine.losto.backend.repository.FournisseurRepository;


@Service
public non-sealed class FournisseurServiceImpl implements  FournisseurService {
	private FournisseurRepository fournisseurRepository ;
  
    public FournisseurServiceImpl(FournisseurRepository fournisseurRepository) {
        this.fournisseurRepository = fournisseurRepository;    
    }

    @Override
    public Fournisseur save(Fournisseur Fournisseur) {
        return fournisseurRepository.save(Fournisseur);
    }

    @Override
    public Fournisseur loadFournisseurByName(String FournisseurName) {
        return fournisseurRepository.findByFournisseurName(FournisseurName);
    }

    @Override
    public List<Fournisseur> listFournisseurs() {
        return fournisseurRepository.findAll();
    }

	@Override
	public void remove(Fournisseur groupe) {
		fournisseurRepository.delete(groupe);

	}

	@Override
	public Fournisseur saveAndFlush(Fournisseur Fournisseur) {
		// TODO Auto-generated method stub
		return fournisseurRepository.saveAndFlush(Fournisseur);
	}

	@Override
	public List<Fournisseur> findFournisseurByCriteria(String fournisseurAdresse, String fournisseurName) {
		// TODO Auto-generated method stub
		return fournisseurRepository.findByFournisseurAdresseContainingOrFournisseurNameContaining(fournisseurAdresse, fournisseurName);
	}

	@Override
	public Fournisseur loadFournisseurById(long id) {
		// TODO Auto-generated method stub
		return fournisseurRepository.findById(id).get();
	}
}
