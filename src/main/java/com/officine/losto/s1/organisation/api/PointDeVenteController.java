package com.officine.losto.s1.organisation.api;

import com.officine.losto.entity.PointDeVente;
import com.officine.losto.s1.organisation.dto.PointDeVenteRequestDto;
import com.officine.losto.s1.organisation.dto.PointDeVenteResponseDto;
import com.officine.losto.s1.organisation.mapper.OrganisationMapper;
import com.officine.losto.s1.organisation.service.PointDeVenteService;
import com.officine.losto.validation.ValidationGroups;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/points-de-vente", produces = "application/json")
@RequiredArgsConstructor
public class PointDeVenteController {

	private final PointDeVenteService pointDeVenteService;
	private final OrganisationMapper organisationMapper;

	@GetMapping
	public List<PointDeVenteResponseDto> list() {
		return pointDeVenteService.findAll().stream()
				.map(organisationMapper::toPdvResponse)
				.collect(Collectors.toList());
	}

	@GetMapping("/{id}")
	public PointDeVenteResponseDto getById(@PathVariable long id) {
		return organisationMapper.toPdvResponse(pointDeVenteService.getById(id));
	}

	@GetMapping("/by-site/{siteId}")
	public List<PointDeVenteResponseDto> listBySite(@PathVariable long siteId) {
		return pointDeVenteService.findBySiteId(siteId).stream()
				.map(organisationMapper::toPdvResponse)
				.collect(Collectors.toList());
	}

	@PostMapping
	public PointDeVenteResponseDto create(
			@Validated({ ValidationGroups.OnCreate.class, Default.class }) @RequestBody PointDeVenteRequestDto dto) {
		PointDeVente saved = pointDeVenteService.create(dto);
		return organisationMapper.toPdvResponse(saved);
	}

	@PutMapping
	public PointDeVenteResponseDto update(
			@Validated({ ValidationGroups.OnUpdate.class, Default.class }) @RequestBody PointDeVenteRequestDto dto) {
		PointDeVente saved = pointDeVenteService.update(dto);
		return organisationMapper.toPdvResponse(saved);
	}

	@PatchMapping("/{id}/actif")
	public PointDeVenteResponseDto setActif(@PathVariable long id, @RequestParam boolean actif) {
		pointDeVenteService.setActif(id, actif);
		return organisationMapper.toPdvResponse(pointDeVenteService.getById(id));
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable long id) {
		pointDeVenteService.delete(id);
	}
}
