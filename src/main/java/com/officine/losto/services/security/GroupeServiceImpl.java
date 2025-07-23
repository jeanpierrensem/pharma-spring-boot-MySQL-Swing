package com.officine.losto.services.security;

import java.util.List;

import org.springframework.stereotype.Service;

import com.officine.losto.backend.entity.AppGroupe;
import com.officine.losto.backend.repository.AppGroupeRepository;


@Service
public non-sealed  class GroupeServiceImpl  implements GroupeService{
    private AppGroupeRepository appGroupeRepository ;
  
    public GroupeServiceImpl(AppGroupeRepository appGroupeRepository) {
        this.appGroupeRepository = appGroupeRepository;
      
    }

    @Override
    public AppGroupe save(AppGroupe appGroupe) {
        return appGroupeRepository.save(appGroupe);
    }

    /*@Override
    public AppGroupe loadGroupeByName(String groupename) {
        return appGroupeRepository.findByGroupeName(groupename);
    }*/
    @Override
    public List<AppGroupe> listGroupes() {
        return appGroupeRepository.findAll();
    }
    
    @Override
    public List<AppGroupe> listAllGroupes(String groupeCode) {
        return appGroupeRepository.findByGroupeCode(groupeCode);
    }
    
    
    
	@Override
	public void remove(AppGroupe groupe) {
		appGroupeRepository.delete(groupe);

	}
	@Override
	public AppGroupe saveAndFlush(AppGroupe appGroupe) {
		
		return appGroupeRepository.saveAndFlush(appGroupe);
	}

	@Override
	public List<AppGroupe> findGroupeByCriteria(String groupeDescription, String groupeName) {
	
		return appGroupeRepository.findByGroupeDescriptionContainingOrGroupeNameContaining(groupeDescription, groupeName);
	}

	@Override
	public List<AppGroupe> saveAll(List<AppGroupe> appGroupes) {
		
		return appGroupeRepository.saveAll(appGroupes);
	}

	@Override
	public List<AppGroupe> saveAllAndFlush(List<AppGroupe> appGroupes) {
		
		return appGroupeRepository.saveAllAndFlush(appGroupes);
	}

	@Override
	public void removeByGroupeCode(String groupeCode) {
	 appGroupeRepository.deleteByGroupeCode(groupeCode);
		
	}

	@Override
	public int update(boolean isAfficher, boolean isEnregistrer, boolean isModifier, boolean isSupprimer,
			boolean isImprimer, String groupeCode, Long menueId) {
		
		return appGroupeRepository.updateGroupeRoles(isAfficher, isEnregistrer, isModifier, isSupprimer, isImprimer, groupeCode, menueId);
		
	}

	@Override
	public AppGroupe findGroupeById(long id) {
		// TODO Auto-generated method stub
		return appGroupeRepository.findById(null).get();
	}

	

	

	

	


}
