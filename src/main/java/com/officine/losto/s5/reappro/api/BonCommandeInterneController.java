package com.officine.losto.s5.reappro.api;

import com.officine.losto.dto.mapper.DtoMapper;
import com.officine.losto.entity.StatutBonCommandeInterne;
import com.officine.losto.s5.reappro.dto.BonCommandeInterneMergeResult;
import com.officine.losto.s5.reappro.dto.BonCommandeInterneRequestDto;
import com.officine.losto.s5.reappro.dto.BonCommandeInterneResponseDto;
import com.officine.losto.s5.reappro.dto.BonTraitementMagasinRequestDto;
import com.officine.losto.s5.reappro.dto.SuggestBonLineResponseDto;
import com.officine.losto.s5.reappro.service.BonCommandeInterneServiceImpl;
import com.officine.losto.s5.reappro.service.BonSuggestLinesService;
import com.officine.losto.validation.ValidationGroups;
import jakarta.validation.groups.Default;
import org.springframework.format.annotation.DateTimeFormat;
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

@RestController
@RequestMapping(value = "/api/bons-commande-interne", produces = "application/json")
public class BonCommandeInterneController {

	private final BonCommandeInterneServiceImpl bonService;
	private final BonSuggestLinesService bonSuggestLinesService;
	private final DtoMapper dtoMapper;

	public BonCommandeInterneController(
			BonCommandeInterneServiceImpl bonService,
			BonSuggestLinesService bonSuggestLinesService,
			DtoMapper dtoMapper) {
		this.bonService = bonService;
		this.bonSuggestLinesService = bonSuggestLinesService;
		this.dtoMapper = dtoMapper;
	}

	@GetMapping
	public List<BonCommandeInterneResponseDto> getAll() {
		return bonService.getAll().stream().map(dtoMapper::toBonResponse).collect(Collectors.toList());
	}

	@GetMapping("/filter")
	public List<BonCommandeInterneResponseDto> filter(
			@RequestParam(required = false) Long siteId,
			@RequestParam(required = false) Long pointDeVenteId,
			@RequestParam(required = false) Long magasinCentralId,
			@RequestParam(required = false) StatutBonCommandeInterne statut,
			@RequestParam(required = false) List<StatutBonCommandeInterne> statuts,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
		return bonService
				.findFiltered(siteId, pointDeVenteId, magasinCentralId, statut, statuts, from, to)
				.stream()
				.map(dtoMapper::toBonResponse)
				.collect(Collectors.toList());
	}

	/** Bons ENVOYE ou PARTIEL encore à traiter pour un magasin central. */
	@GetMapping("/en-cours-traitement")
	public List<BonCommandeInterneResponseDto> enCoursTraitement(
			@RequestParam long magasinCentralId) {
		return bonService.findEnCoursTraitementMagasin(magasinCentralId).stream()
				.map(dtoMapper::toBonResponse)
				.collect(Collectors.toList());
	}

	@GetMapping("/suggest-lines")
	public List<SuggestBonLineResponseDto> suggestLines(@RequestParam long pointDeVenteId) {
		return bonSuggestLinesService.suggestLinesForPointDeVente(pointDeVenteId);
	}

	@GetMapping("/{id}")
	public BonCommandeInterneResponseDto getById(@PathVariable long id) {
		return dtoMapper.toBonResponse(bonService.loadById(id));
	}

	@PostMapping
	public BonCommandeInterneResponseDto create(
			@Validated({ ValidationGroups.OnCreate.class, Default.class })
			@RequestBody BonCommandeInterneRequestDto dto) {
		return dtoMapper.toBonResponse(bonService.save(dtoMapper.toBon(dto)));
	}

	@PutMapping
	public BonCommandeInterneResponseDto update(
			@Validated({ ValidationGroups.OnUpdate.class, Default.class })
			@RequestBody BonCommandeInterneRequestDto dto) {
		BonCommandeInterneMergeResult merged = dtoMapper.mergeBon(dto);
		return dtoMapper.toBonResponse(bonService.save(merged.bon(), merged.statutAvantFusion()));
	}

	@PostMapping("/traiter")
	public BonCommandeInterneResponseDto traiter(
			@Validated @RequestBody BonTraitementMagasinRequestDto dto) {
		return dtoMapper.toBonResponse(bonService.traiterAuMagasinCentral(dto));
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable long id) {
		bonService.remove(bonService.loadById(id));
	}
}
