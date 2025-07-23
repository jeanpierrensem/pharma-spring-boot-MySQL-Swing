package com.officine.losto.backend.services;

import java.util.List;
import org.springframework.stereotype.Service;

import com.officine.losto.backend.entity.Vente;
import com.officine.losto.backend.repository.VenteRepository;

@Service
public non-sealed class VenteServiceImpl implements VenteService {
	private VenteRepository repository;

	public VenteServiceImpl(VenteRepository repository) {
		this.repository = repository;
	}

	@Override
	public Vente save(Vente Vente) {
		return repository.save(Vente); 
	}

	@Override
	public List<Vente> listVentes() {
		return repository.findAll();
	}

	@Override
	public void remove(Vente Vente) {
		repository.delete(Vente);

	}

	@Override
	public Vente saveAndFlush(Vente Vente) {
		// TODO Auto-generated method stub
		return repository.saveAndFlush(Vente);
	}


	@Override
	public Vente loadVenteById(Long id) {
		return repository.findById(id).get();
	}

	@Override
	public long getVenteCount() {
		return repository.count();
	}

	@Override
	public List<Vente> findTypeByCriteria(String VenteNumero, String VenteDate) {
		return repository.findByNumeroContainingOrVentedateContaining(VenteNumero,
				VenteDate);
	}

	@Override
	public Vente loadVenteByNumero(String VenteNumero) {
		return repository.findByNumero(VenteNumero);
	}

}
