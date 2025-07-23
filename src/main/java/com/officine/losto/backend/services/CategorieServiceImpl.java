package com.officine.losto.backend.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.officine.losto.backend.entity.Categorie;
import com.officine.losto.backend.repository.CategorieRepository;


@Service
public non-sealed class CategorieServiceImpl implements CategorieService {
	private CategorieRepository categorieRepository ;
  
    public CategorieServiceImpl(CategorieRepository categorieRepository) {
        this.categorieRepository = categorieRepository;    
    }

    @Override
    public Categorie save(Categorie categorie) {
        return categorieRepository.save(categorie);
    }

    @Override
    public Categorie loadCategorieByName(String categorieName) {
        return categorieRepository.findByCategorieName(categorieName);
    }

    @Override
    public List<Categorie> listCategories() {
        return categorieRepository.findAll();
    }

	@Override
	public void remove(Categorie categorie) {
		categorieRepository.delete(categorie);

	}

	@Override
	public Categorie saveAndFlush(Categorie categorie) {
		// TODO Auto-generated method stub
		return categorieRepository.saveAndFlush(categorie);
	}

	@Override
	public List<Categorie> findCategorieByCriteria(String categorieDescription, String categorieName) {
		// TODO Auto-generated method stub
		return categorieRepository.findByCategorieDescriptionContainingOrCategorieNameContaining(categorieDescription, categorieName);
	}

	@Override
	public Categorie loadCategorieById(Long id) {
		// TODO Auto-generated method stub
		return categorieRepository.findById(id).get();
	}
}
