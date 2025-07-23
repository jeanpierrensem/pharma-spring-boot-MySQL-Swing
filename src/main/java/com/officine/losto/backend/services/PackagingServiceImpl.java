package com.officine.losto.backend.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.officine.losto.backend.entity.Packaging;
import com.officine.losto.backend.repository.PackagingRepository;


@Service
public non-sealed class PackagingServiceImpl implements  PackagingService {
	private PackagingRepository packagingRepository ;
  
    public PackagingServiceImpl(PackagingRepository packagingRepository) {
        this.packagingRepository = packagingRepository;    
    }

    @Override
    public Packaging save(Packaging packaging) {
        return packagingRepository.save(packaging);
    }

    @Override
    public Packaging loadPackagingByName(String packagingName) {
        return packagingRepository.findByPackagingName(packagingName);
    }

    @Override
    public List<Packaging> listPackagings() {
        return packagingRepository.findAll();
    }

	@Override
	public void remove(Packaging packaging) {
		packagingRepository.delete(packaging);

	}

	@Override
	public Packaging saveAndFlush(Packaging packaging) {
		// TODO Auto-generated method stub
		return packagingRepository.saveAndFlush(packaging);
	}

	@Override
	public List<Packaging> findPackagingByCriteria(String packagingDescription, String packagingName) {
		// TODO Auto-generated method stub
		return packagingRepository.findByPackagingDescriptionContainingOrPackagingNameContaining(packagingDescription, packagingName);
	}

	@Override
	public Packaging loadPackagingById(Long id) {
		// TODO Auto-generated method stub
		return packagingRepository.findById(id).get();
	}
}
