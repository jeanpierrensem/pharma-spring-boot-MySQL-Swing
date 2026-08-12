package com.officine.losto.controller;

import com.officine.losto.dto.*;
import com.officine.losto.dto.mapper.*;
import com.officine.losto.service.*;
import com.officine.losto.validation.*;
import jakarta.validation.groups.*;
import org.springframework.validation.annotation.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.*;

@RestController
@RequestMapping(value = "/api/providers", produces = "application/json")
public class ProviderController {
    private final ProviderService providerService;
    private final DtoMapper dtoMapper;

    public ProviderController(ProviderService providerService, DtoMapper dtoMapper) {
        this.providerService = providerService;
        this.dtoMapper = dtoMapper;
    }

    @GetMapping
    public List<ProviderResponseDto> getAll() {
        return providerService.getAll().stream().map(dtoMapper::toProviderResponse).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ProviderResponseDto getById(@PathVariable long id) {
        return dtoMapper.toProviderResponse(providerService.loadById(id));
    }

    @GetMapping("/by-code")
    public ProviderResponseDto getByCode(@RequestParam String code) {
        return dtoMapper.toProviderResponse(providerService.loadByCode(code));
    }

    @GetMapping("/search")
    public List<ProviderResponseDto> search(
            @RequestParam(defaultValue = "") String code,
            @RequestParam(defaultValue = "") String designation) {
        return providerService.findByCriteria(code, designation).stream().map(dtoMapper::toProviderResponse).collect(Collectors.toList());
    }

    @PostMapping
    public ProviderResponseDto create(
            @Validated({ValidationGroups.OnCreate.class, Default.class}) @RequestBody ProviderRequestDto dto) {
        return dtoMapper.toProviderResponse(providerService.save(dtoMapper.toProvider(dto)));
    }

    @PutMapping
    public ProviderResponseDto update(
            @Validated({ValidationGroups.OnUpdate.class, Default.class}) @RequestBody ProviderRequestDto dto) {
        return dtoMapper.toProviderResponse(providerService.update(dtoMapper.toProvider(dto)));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        providerService.remove(providerService.loadById(id));
    }
}
