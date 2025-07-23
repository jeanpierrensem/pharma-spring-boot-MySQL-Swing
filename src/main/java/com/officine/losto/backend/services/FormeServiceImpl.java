package com.officine.losto.backend.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.officine.losto.backend.entity.Forme;
import com.officine.losto.backend.repository.FormeRepository;


@Service
public non-sealed class FormeServiceImpl implements FormeService {
	private FormeRepository formeRepository ;
  
    public FormeServiceImpl(FormeRepository formeRepository) {
        this.formeRepository = formeRepository;    
    }

    @Override
    public Forme save(Forme forme) {
        return formeRepository.save(forme);
    }

    @Override
    public Forme loadFormeByName(String FormeName) {
        return formeRepository.findByFormeName(FormeName);
    }

    @Override
    public List<Forme> listFormes() {
        return formeRepository.findAll();
    }

	@Override
	public void remove(Forme forme) {
		formeRepository.delete(forme);

	}

	@Override
	public Forme saveAndFlush(Forme forme) {
		// TODO Auto-generated method stub
		return formeRepository.saveAndFlush(forme);
	}

	@Override
	public List<Forme> findFormeByCriteria(String FormeDescription, String FormeName) {
		// TODO Auto-generated method stub
		return formeRepository.findByFormeDescriptionContainingOrFormeNameContaining(FormeDescription, FormeName);
	}

	@Override
	public Forme loadFormeById(Long id) {
		return formeRepository.findById(id).get();
	}
}
