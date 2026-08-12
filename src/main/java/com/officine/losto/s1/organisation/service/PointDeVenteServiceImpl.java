package com.officine.losto.s1.organisation.service;

import com.officine.losto.entity.PointDeVente;
import com.officine.losto.entity.Site;
import com.officine.losto.s1.organisation.dto.PointDeVenteRequestDto;
import com.officine.losto.s1.organisation.exception.ResourceNotFoundException;
import com.officine.losto.s1.organisation.mapper.OrganisationMapper;
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
public class PointDeVenteServiceImpl implements PointDeVenteService {

	private final PointDeVenteRepository pointDeVenteRepository;
	private final SiteRepository siteRepository;
	private final OrganisationMapper organisationMapper;

	@Override
	@Transactional(readOnly = true)
	public List<PointDeVente> findAll() {
		return pointDeVenteRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public PointDeVente getById(long id) {
		return pointDeVenteRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("PointDeVente", id));
	}

	@Override
	@Transactional(readOnly = true)
	public List<PointDeVente> findBySiteId(long siteId) {
		if (!siteRepository.existsById(siteId)) {
			throw new ResourceNotFoundException("Site", siteId);
		}
		return pointDeVenteRepository.findBySite_Id(siteId);
	}

	@Override
	@Transactional
	public PointDeVente create(PointDeVenteRequestDto dto) {
		Site site = siteRepository.findById(dto.getSiteId())
				.orElseThrow(() -> new ResourceNotFoundException("Site", dto.getSiteId()));
		if (pointDeVenteRepository.existsByCode(dto.getCode())) {
			throw new IllegalArgumentException("Code point de vente déjà utilisé : " + dto.getCode());
		}
		boolean actif = dto.getActif() == null || dto.getActif();
		PointDeVente p = PointDeVente.builder()
				.site(site)
				.code(dto.getCode())
				.libelle(dto.getLibelle())
				.adresse(dto.getAdresse())
				.phone(dto.getPhone())
				.actif(actif)
				.build();
		PointDeVente saved = pointDeVenteRepository.save(p);
		log.info("Point de vente créé id={} siteId={}", saved.getId(), site.getId());
		return saved;
	}

	@Override
	@Transactional
	public PointDeVente update(PointDeVenteRequestDto dto) {
		PointDeVente entity = pointDeVenteRepository.findById(dto.getId())
				.orElseThrow(() -> new ResourceNotFoundException("PointDeVente", dto.getId()));
		if (dto.getCode() != null && pointDeVenteRepository.existsByCodeAndIdNot(dto.getCode(), dto.getId())) {
			throw new IllegalArgumentException("Code point de vente déjà utilisé : " + dto.getCode());
		}
		if (dto.getSiteId() != null) {
			Site site = siteRepository.findById(dto.getSiteId())
					.orElseThrow(() -> new ResourceNotFoundException("Site", dto.getSiteId()));
			entity.setSite(site);
		}
		organisationMapper.updatePdvFromDto(dto, entity);
		PointDeVente saved = pointDeVenteRepository.save(entity);
		log.info("Point de vente mis à jour id={}", saved.getId());
		return saved;
	}

	@Override
	@Transactional
	public void delete(long id) {
		PointDeVente p = pointDeVenteRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("PointDeVente", id));
		pointDeVenteRepository.delete(p);
		log.info("Point de vente supprimé id={}", id);
	}

	@Override
	@Transactional
	public void setActif(long id, boolean actif) {
		PointDeVente p = pointDeVenteRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("PointDeVente", id));
		p.setActif(actif);
		pointDeVenteRepository.save(p);
		log.info("Point de vente id={} actif={}", id, actif);
	}
}
