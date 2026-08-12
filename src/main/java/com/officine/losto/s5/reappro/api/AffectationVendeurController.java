package com.officine.losto.s5.reappro.api;

import com.officine.losto.dto.mapper.DtoMapper;
import com.officine.losto.s5.reappro.dto.AffectationVendeurRequestDto;
import com.officine.losto.s5.reappro.dto.AffectationVendeurResponseDto;
import com.officine.losto.s5.reappro.service.AffectationVendeurServiceImpl;
import com.officine.losto.validation.ValidationGroups;
import jakarta.validation.groups.Default;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.format.annotation.DateTimeFormat;

@RestController
@RequestMapping(value = "/api/affectations-vendeur", produces = "application/json")
public class AffectationVendeurController {

	private final AffectationVendeurServiceImpl service;
	private final DtoMapper dtoMapper;

	public AffectationVendeurController(AffectationVendeurServiceImpl service, DtoMapper dtoMapper) {
		this.service = service;
		this.dtoMapper = dtoMapper;
	}

	@GetMapping
	public List<AffectationVendeurResponseDto> getAll() {
		return service.getAll().stream().map(dtoMapper::toAffectationResponse).collect(Collectors.toList());
	}

	@GetMapping("/planning")
	public List<AffectationVendeurResponseDto> planning(
			@RequestParam long pointDeVenteId,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
		return service.findForPlanning(pointDeVenteId, from, to).stream()
				.map(dtoMapper::toAffectationResponse)
				.collect(Collectors.toList());
	}

	@GetMapping("/{id}")
	public AffectationVendeurResponseDto getById(@PathVariable long id) {
		return dtoMapper.toAffectationResponse(service.loadById(id));
	}

	@GetMapping("/filter")
	public List<AffectationVendeurResponseDto> filter(
			@RequestParam(required = false) Long pointDeVenteId,
			@RequestParam(required = false) Long appUserId) {
		return service.findFiltered(pointDeVenteId, appUserId).stream()
				.map(dtoMapper::toAffectationResponse)
				.collect(Collectors.toList());
	}

	@PostMapping
	public AffectationVendeurResponseDto create(
			@Validated({ ValidationGroups.OnCreate.class, Default.class })
			@RequestBody AffectationVendeurRequestDto dto) {
		return dtoMapper.toAffectationResponse(service.save(dtoMapper.toAffectation(dto)));
	}

	@PutMapping
	public AffectationVendeurResponseDto update(
			@Validated({ ValidationGroups.OnUpdate.class, Default.class })
			@RequestBody AffectationVendeurRequestDto dto) {
		return dtoMapper.toAffectationResponse(service.save(dtoMapper.mergeAffectation(dto)));
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable long id) {
		service.remove(service.loadById(id));
	}
}
