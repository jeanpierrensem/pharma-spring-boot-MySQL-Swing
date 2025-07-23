package com.officine.losto.backend.services;

import java.util.List;
import org.springframework.stereotype.Service;

import com.officine.losto.backend.entity.Commande;
import com.officine.losto.backend.repository.CommandeRepository;

@Service
public non-sealed class CommandeServiceImpl implements CommandeService {
	private CommandeRepository repository;

	public CommandeServiceImpl(CommandeRepository repository) {
		this.repository = repository;
	}

	@Override
	public Commande save(Commande commande) {
		return repository.save(commande);
	}

	@Override
	public List<Commande> listCommandes() {
		return repository.findAll();
	}

	@Override
	public void remove(Commande commande) {
		repository.delete(commande);

	}

	@Override
	public Commande saveAndFlush(Commande commande) {
		// TODO Auto-generated method stub
		return repository.saveAndFlush(commande);
	}


	@Override
	public Commande loadCommandeById(Long id) {
		return repository.findById(id).get();
	}

	@Override
	public long getCommandCount() {
		return repository.count();
	}

	@Override
	public List<Commande> findTypeByCriteria(String commandeNumero, String commandeDate) {
		return repository.findByCommandeNumeroContainingOrCommandeDateContaining(commandeNumero,
				commandeDate);
	}

	@Override
	public Commande loadCommandeByNumeroCommande(String commandeNumero) {
		return repository.findByCommandeNumero(commandeNumero);
	}

}
