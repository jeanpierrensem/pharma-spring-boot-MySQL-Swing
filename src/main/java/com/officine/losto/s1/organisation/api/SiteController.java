package com.officine.losto.s1.organisation.api;

import com.officine.losto.entity.PointDeVente;
import com.officine.losto.entity.Site;
import com.officine.losto.s1.organisation.dto.PointDeVenteResponseDto;
import com.officine.losto.s1.organisation.dto.SiteRequestDto;
import com.officine.losto.s1.organisation.dto.SiteResponseDto;
import com.officine.losto.s1.organisation.mapper.OrganisationMapper;
import com.officine.losto.s1.organisation.service.SiteService;
import com.officine.losto.validation.ValidationGroups;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/sites", produces = "application/json")
@RequiredArgsConstructor
public class SiteController {

	private final SiteService siteService;
	private final OrganisationMapper organisationMapper;

	@GetMapping
	public List<SiteResponseDto> list() {
		return siteService.findAll().stream()
				.map(siteService::buildResponse)
				.collect(Collectors.toList());
	}

	@GetMapping("/{id}")
	public SiteResponseDto getById(@PathVariable long id) {
		return siteService.buildResponse(siteService.getById(id));
	}

	@GetMapping("/{siteId}/points-de-vente")
	public List<PointDeVenteResponseDto> listPointsDeVente(@PathVariable long siteId) {
		return siteService.findPointsDeVenteBySite(siteId).stream()
				.map(organisationMapper::toPdvResponse)
				.collect(Collectors.toList());
	}

	@PostMapping
	public SiteResponseDto create(
			@Validated({ ValidationGroups.OnCreate.class, Default.class }) @RequestBody SiteRequestDto dto) {
		Site saved = siteService.create(dto);
		return siteService.buildResponse(saved);
	}

	@PutMapping
	public SiteResponseDto update(
			@Validated({ ValidationGroups.OnUpdate.class, Default.class }) @RequestBody SiteRequestDto dto) {
		Site saved = siteService.update(dto);
		return siteService.buildResponse(saved);
	}

	@PatchMapping("/{id}/actif")
	public SiteResponseDto setActif(@PathVariable long id, @RequestParam boolean actif) {
		siteService.setActif(id, actif);
		return siteService.buildResponse(siteService.getById(id));
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable long id) {
		siteService.delete(id);
	}
}
