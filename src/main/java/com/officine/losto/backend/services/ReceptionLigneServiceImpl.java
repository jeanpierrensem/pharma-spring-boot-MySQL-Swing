package com.officine.losto.backend.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.officine.losto.backend.entity.CommandeLigne;
import com.officine.losto.backend.entity.ReceptionLigne;
import com.officine.losto.backend.repository.ReceptionLigneRepository;


@Service
public non-sealed class ReceptionLigneServiceImpl implements ReceptionLigneService {
	private ReceptionLigneRepository repository ;
  
    public ReceptionLigneServiceImpl(ReceptionLigneRepository repository) {
        this.repository = repository; }

    @Override
    public ReceptionLigne save(ReceptionLigne receptionLigne) {
        return repository.save(receptionLigne);
    }

    @Override
    public List<ReceptionLigne> listReceptionLignes() {
        return repository.findAll();
    }

	@Override
	public void remove(ReceptionLigne  receptionLigne) {
		repository.delete(receptionLigne);

	}

	@Override
	public ReceptionLigne saveAndFlush(ReceptionLigne receptionLigne ) {
		return repository.saveAndFlush(receptionLigne);
	}
	
	@Override
	public ReceptionLigne loadReceptionLigneById(Long id) {
		return repository.findById(id).get();
	}

	@Override
	public void saveAll(List<ReceptionLigne> receptionLignes) {
		repository.saveAll(receptionLignes); 
		
	}

	@Override
	public List<ReceptionLigne> saveAllAndFlush(List<ReceptionLigne> receptionLignes) {
		return repository.saveAllAndFlush(receptionLignes); }

	@Override
	public List<ReceptionLigne> findReceptionsByCommandeLigne(CommandeLigne commandeLigne) {
		// TODO Auto-generated method stub
		return  repository.findByCommandeLigne(commandeLigne);
	}

	

	/*@Override
	public List<ReceptionLigne> findByCommande(Commande commande) {
		// TODO Auto-generated method stub
		return null;
	}*/

	/*@Override
	public List<ReceptionLigne> loadByReceptionLigneId(long id) {
		// TODO Auto-generated method stub
		return repository.findById(id);
	}*/

}
