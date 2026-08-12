package com.officine.losto.s1.organisation.service;

import com.officine.losto.entity.MagasinCentral;
import com.officine.losto.entity.PointDeVente;
import com.officine.losto.entity.Site;
import com.officine.losto.model.UserRepo;
import com.officine.losto.s1.organisation.dto.SiteRequestDto;
import com.officine.losto.s1.organisation.dto.SiteResponseDto;
import com.officine.losto.s1.organisation.exception.ResourceNotFoundException;
import com.officine.losto.s1.organisation.mapper.OrganisationMapper;
import com.officine.losto.s1.organisation.repository.MagasinCentralRepository;
import com.officine.losto.s1.organisation.repository.PointDeVenteRepository;
import com.officine.losto.s1.organisation.repository.SiteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SiteServiceImpl implements SiteService {

	private final SiteRepository siteRepository;
	private final MagasinCentralRepository magasinCentralRepository;
	private final PointDeVenteRepository pointDeVenteRepository;
	private final OrganisationMapper organisationMapper;
	private final UserRepo userRepo;

	@Override
	@Transactional(readOnly = true)
	public List<Site> findAll() {
		return siteRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Site getById(long id) {
		return siteRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Site", id));
	}

	@Override
	@Transactional(readOnly = true)
	public SiteResponseDto buildResponse(Site site) {
		Long magId = magasinCentralRepository.findBySite_Id(site.getId())
				.map(MagasinCentral::getId)
				.orElse(null);
		return organisationMapper.toSiteResponse(site, magId);
	}

	@Override
	@Transactional
	public Site create(SiteRequestDto dto) {
		if (siteRepository.existsByCode(dto.getCode())) {
			throw new IllegalArgumentException("Code site déjà utilisé : " + dto.getCode());
		}
		validateResponsable(dto.getResponsableUserId());
		Site site = organisationMapper.toNewSite(dto);
		Site saved = siteRepository.save(site);
		log.info("Site créé id={} code={}", saved.getId(), saved.getCode());
		return saved;
	}

	@Override
	@Transactional
	public Site update(SiteRequestDto dto) {
		Site entity = siteRepository.findById(dto.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Site", dto.getId()));
		if (dto.getCode() != null && siteRepository.existsByCodeAndIdNot(dto.getCode(), dto.getId())) {
			throw new IllegalArgumentException("Code site déjà utilisé : " + dto.getCode());
		}
		validateResponsable(dto.getResponsableUserId());
		organisationMapper.updateSiteFromDto(dto, entity);
		Site saved = siteRepository.save(entity);
		log.info("Site mis à jour id={}", saved.getId());
		return saved;
	}

	@Override
	@Transactional
	public void delete(long id) {
		Site site = siteRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Site", id));
		if (magasinCentralRepository.existsBySite_Id(id)) {
			throw new IllegalArgumentException("Suppression impossible : un magasin central est rattaché au site.");
		}
		if (!pointDeVenteRepository.findBySite_Id(id).isEmpty()) {
			throw new IllegalArgumentException("Suppression impossible : des points de vente sont rattachés au site.");
		}
		siteRepository.delete(site);
		log.info("Site supprimé id={}", id);
	}

	@Override
	@Transactional
	public void setActif(long id, boolean actif) {
		Site site = siteRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Site", id));
		site.setActif(actif);
		siteRepository.save(site);
		log.info("Site id={} actif={}", id, actif);
	}

	@Override
	@Transactional(readOnly = true)
	public List<PointDeVente> findPointsDeVenteBySite(long siteId) {
		if (!siteRepository.existsById(siteId)) {
			throw new ResourceNotFoundException("Site", siteId);
		}
		return pointDeVenteRepository.findBySite_Id(siteId);
	}

	private void validateResponsable(Long responsableUserId) {
		if (responsableUserId != null && !userRepo.existsById(responsableUserId)) {
			throw new IllegalArgumentException("Utilisateur responsable inexistant : " + responsableUserId);
		}
	}
}
