package com.officine.losto.backend.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.officine.losto.backend.entity.Commande;
import com.officine.losto.backend.entity.CommandeLigne;
import com.officine.losto.backend.entity.ReceptionLigne;
import com.officine.losto.backend.repository.CommandeLigneRepository;


@Service
public non-sealed class CommandeLigneServiceImpl implements CommandeLigneService {
	private CommandeLigneRepository repository ;
  
    public CommandeLigneServiceImpl(CommandeLigneRepository repository) {
        this.repository = repository;    
    }

    @Override
    public CommandeLigne save(CommandeLigne commandeLigne) {
        return repository.save(commandeLigne);
    }

   

    @Override
    public List<CommandeLigne> listCommandeLignes() {
        return repository.findAll();
    }

	@Override
	public void remove(CommandeLigne  commandeLigne) {
		repository.delete(commandeLigne);

	}

	@Override
	public CommandeLigne saveAndFlush(CommandeLigne commandeLigne ) {
		// TODO Auto-generated method stub
		return repository.saveAndFlush(commandeLigne);
	}

	
	@Override
	public CommandeLigne loadCommandeLigneById(Long id) {
		// TODO Auto-generated method stub
		return repository.findById(id).get();
	}

	@Override
	public void saveAll(List<CommandeLigne> commandeLignes) {
		repository.saveAll(commandeLignes); 
		
	}

	@Override
	public List<CommandeLigne> saveAllAndFlush(List<CommandeLigne> commandeLignes) {
		return repository.saveAllAndFlush(commandeLignes); 
		
	}

	@Override
	public void deleteByCommandeLigneCommande(Commande commande) {
		repository.deleteByCommandeLigneCommande(commande); 
	}

	@Override
	public List<CommandeLigne> findByCommande(Commande commande) {
		return repository.findByCommandeLigneCommande(commande);
	}

	@Override
	public List<ReceptionLigne> loadByCommandeLigneId(long id) {
		// TODO Auto-generated method stub
		return  repository.findByCommandeLigneId(id);
	}
	
	
	
}
