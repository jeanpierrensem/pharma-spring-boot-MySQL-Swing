package com.officine.losto.backend.services;

import java.util.List;
import org.springframework.stereotype.Service;

import com.officine.losto.backend.entity.Vente;
import com.officine.losto.backend.entity.VenteLigne;
import com.officine.losto.backend.repository.VenteLigneRepository;

@Service
public non-sealed class VenteLigneServiceImpl implements VenteLigneService {
	private VenteLigneRepository repository;

	public VenteLigneServiceImpl(VenteLigneRepository repository) {
		this.repository = repository;
	}

	@Override
	public VenteLigne save(VenteLigne venteLigne) {

		return repository.save(venteLigne);
	}

	@Override
	public void saveAll(List<VenteLigne> venteLignes) {
		// TODO Auto-generated method stub
		repository.saveAll(venteLignes);
	}

	@Override
	public List<VenteLigne> saveAllAndFlush(List<VenteLigne> venteLignes) {

		return repository.saveAllAndFlush(venteLignes);
	}

	@Override
	public VenteLigne saveAndFlush(VenteLigne venteLigne) {
		// TODO Auto-generated method stub
		return repository.saveAndFlush(venteLigne);
	}

	@Override
	public void remove(VenteLigne venteLigne) {
		// TODO Auto-generated method stub
		repository.delete(venteLigne);

	}
	/*
	 * @Override public void deleteByLigneVente(LigneVente ligneVente) { // TODO
	 * Auto-generated method stub repository.deleteByLigneVente(ligneVente);
	 * 
	 * }
	 */

	@Override
	public List<VenteLigne> listVenteLignes() {
		// TODO Auto-generated method stub
		return repository.findAll();
	}

	@Override
	public List<VenteLigne> loadByVente(Vente vente) {
		// TODO Auto-generated method stub
		return repository.findByVente(vente);
	}

	@Override
	public VenteLigne loadVenteLigneById(Long id) {
		// TODO Auto-generated method stub
		return repository.findById(id).get();
	}

	@Override
	public List<VenteLigne> loadByVenteLigneId(long id) {
		// TODO Auto-generated method stub
		return repository.findByVenteLigneId(id);
	}

}
