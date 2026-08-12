package com.officine.losto.s1.organisation.service;

import com.officine.losto.entity.MagasinCentral;
import com.officine.losto.entity.Site;
import com.officine.losto.s1.organisation.dto.MagasinCentralRequestDto;
import com.officine.losto.s1.organisation.exception.ResourceNotFoundException;
import com.officine.losto.s1.organisation.mapper.OrganisationMapper;
import com.officine.losto.s1.organisation.repository.MagasinCentralRepository;
import com.officine.losto.s1.organisation.repository.SiteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MagasinCentralServiceImpl implements MagasinCentralService {

	private final MagasinCentralRepository magasinCentralRepository;
	private final SiteRepository siteRepository;
	private final OrganisationMapper organisationMapper;

	@Override
	@Transactional(readOnly = true)
	public List<MagasinCentral> findAll() {
		return magasinCentralRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public MagasinCentral getById(long id) {
		return magasinCentralRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("MagasinCentral", id));
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<MagasinCentral> findBySiteId(long siteId) {
		if (!siteRepository.existsById(siteId)) {
			throw new ResourceNotFoundException("Site", siteId);
		}
		return magasinCentralRepository.findBySite_Id(siteId);
	}

	@Override
	@Transactional
	public MagasinCentral create(MagasinCentralRequestDto dto) {
		Site site = siteRepository.findById(dto.getSiteId())
				.orElseThrow(() -> new ResourceNotFoundException("Site", dto.getSiteId()));
		if (magasinCentralRepository.existsBySite_Id(site.getId())) {
			throw new IllegalArgumentException("Un magasin central existe déjà pour ce site.");
		}
		if (magasinCentralRepository.existsByCode(dto.getCode())) {
			throw new IllegalArgumentException("Code magasin déjà utilisé : " + dto.getCode());
		}
		MagasinCentral m = MagasinCentral.builder()
				.site(site)
				.code(dto.getCode())
				.libelle(dto.getLibelle())
				.build();
		MagasinCentral saved = magasinCentralRepository.save(m);
		log.info("Magasin central créé id={} siteId={}", saved.getId(), site.getId());
		return saved;
	}

	@Override
	@Transactional
	public MagasinCentral update(MagasinCentralRequestDto dto) {
		MagasinCentral entity = magasinCentralRepository.findById(dto.getId())
				.orElseThrow(() -> new ResourceNotFoundException("MagasinCentral", dto.getId()));
		if (dto.getCode() != null && magasinCentralRepository.existsByCodeAndIdNot(dto.getCode(), dto.getId())) {
			throw new IllegalArgumentException("Code magasin déjà utilisé : " + dto.getCode());
		}
		organisationMapper.updateMagasinFromDto(dto, entity);
		MagasinCentral saved = magasinCentralRepository.save(entity);
		log.info("Magasin central mis à jour id={}", saved.getId());
		return saved;
	}

	@Override
	@Transactional
	public void delete(long id) {
		MagasinCentral m = magasinCentralRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("MagasinCentral", id));
		magasinCentralRepository.delete(m);
		log.info("Magasin central supprimé id={}", id);
	}
}
