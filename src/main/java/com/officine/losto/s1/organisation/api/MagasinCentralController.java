package com.officine.losto.s1.organisation.api;

import com.officine.losto.entity.MagasinCentral;
import com.officine.losto.s1.organisation.dto.MagasinCentralRequestDto;
import com.officine.losto.s1.organisation.dto.MagasinCentralResponseDto;
import com.officine.losto.s1.organisation.exception.ResourceNotFoundException;
import com.officine.losto.s1.organisation.mapper.OrganisationMapper;
import com.officine.losto.s1.organisation.service.MagasinCentralService;
import com.officine.losto.validation.ValidationGroups;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/magasins-centraux", produces = "application/json")
@RequiredArgsConstructor
public class MagasinCentralController {

	private final MagasinCentralService magasinCentralService;
	private final OrganisationMapper organisationMapper;

	@GetMapping
	public List<MagasinCentralResponseDto> list() {
		return magasinCentralService.findAll().stream()
				.map(organisationMapper::toMagasinResponse)
				.collect(Collectors.toList());
	}

	@GetMapping("/{id}")
	public MagasinCentralResponseDto getById(@PathVariable long id) {
		return organisationMapper.toMagasinResponse(magasinCentralService.getById(id));
	}

	@GetMapping("/by-site/{siteId}")
	public MagasinCentralResponseDto getBySite(@PathVariable long siteId) {
		MagasinCentral m = magasinCentralService.findBySiteId(siteId)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Aucun magasin central pour le site id=" + siteId));
		return organisationMapper.toMagasinResponse(m);
	}

	@PostMapping
	public MagasinCentralResponseDto create(
			@Validated({ ValidationGroups.OnCreate.class, Default.class }) @RequestBody MagasinCentralRequestDto dto) {
		MagasinCentral saved = magasinCentralService.create(dto);
		return organisationMapper.toMagasinResponse(saved);
	}

	@PutMapping
	public MagasinCentralResponseDto update(
			@Validated({ ValidationGroups.OnUpdate.class, Default.class }) @RequestBody MagasinCentralRequestDto dto) {
		MagasinCentral saved = magasinCentralService.update(dto);
		return organisationMapper.toMagasinResponse(saved);
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable long id) {
		magasinCentralService.delete(id);
	}
}
