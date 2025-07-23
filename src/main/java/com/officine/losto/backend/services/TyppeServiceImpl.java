package com.officine.losto.backend.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.officine.losto.backend.entity.Typpe;
import com.officine.losto.backend.repository.TyppeRepository;


@Service
public non-sealed class TyppeServiceImpl implements TyppeService {
	private TyppeRepository typeRepository ;
  
    public TyppeServiceImpl(TyppeRepository typeRepository) {
        this.typeRepository = typeRepository;    
    }

    @Override
    public Typpe save(Typpe Type) { 
        return typeRepository.save(Type);
    }

    @Override
    public Typpe loadTypeByName(String TypeName) {
        return typeRepository.findByTyppeName(TypeName);
    }

    @Override
    public List<Typpe> listTypes() {
        return typeRepository.findAll();
    }

	@Override
	public void remove(Typpe groupe) {
		typeRepository.delete(groupe);

	}

	@Override
	public Typpe saveAndFlush(Typpe Type) {
		// TODO Auto-generated method stub
		return typeRepository.saveAndFlush(Type);
	}

	@Override
	public List<Typpe> findTypeByCriteria(String TypeDescription, String TypeName) {
		// TODO Auto-generated method stub
		return typeRepository.findByTyppeDescriptionContainingOrTyppeNameContaining(TypeDescription, TypeName);
	}

	@Override
	public Typpe loadTypeById(Long id) {
		// TODO Auto-generated method stub
		return typeRepository.findById(id).get();
	}
}
