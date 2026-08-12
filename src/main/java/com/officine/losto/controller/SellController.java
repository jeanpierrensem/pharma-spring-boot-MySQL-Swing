package com.officine.losto.controller;

import com.officine.losto.dto.*;
import com.officine.losto.dto.mapper.*;
import com.officine.losto.service.*;
import com.officine.losto.validation.*;
import jakarta.validation.groups.*;
import org.springframework.format.annotation.*;
import org.springframework.validation.annotation.*;
import org.springframework.web.bind.annotation.*;

import java.time.*;
import java.util.*;
import java.util.stream.*;

@RestController
@RequestMapping(value = "/api/sells", produces = "application/json")
public class SellController {
    private final SellService sellService;
    private final DtoMapper dtoMapper;

    public SellController(SellService sellService, DtoMapper dtoMapper) {
        this.sellService = sellService;
        this.dtoMapper = dtoMapper;
    }

    @GetMapping
    public List<SellResponseDto> getAll() {
        return sellService.getAll().stream().map(dtoMapper::toSellResponse).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public SellResponseDto getById(@PathVariable long id) {
        return dtoMapper.toSellResponse(sellService.loadById(id));
    }

    @GetMapping("/by-code")
    public SellResponseDto getByCode(@RequestParam String code) {
        return dtoMapper.toSellResponse(sellService.loadByCode(code));
    }

    @GetMapping("/search")
    public List<SellResponseDto> search(
            @RequestParam(defaultValue = "") String number,
            @RequestParam(defaultValue = "") String seller,
            @RequestParam(defaultValue = "") String client,
            @RequestParam(defaultValue = "") String sellType) {
        return sellService.findByCriteria(number, seller, client, sellType).stream()
                .map(dtoMapper::toSellResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/filter")
    public List<SellResponseDto> filter(
            @RequestParam(required = false) Long siteId,
            @RequestParam(required = false) Long pointDeVenteId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(required = false) Long effectueeParUserId) {
        return sellService.findFiltered(siteId, pointDeVenteId, from, to, effectueeParUserId).stream()
                .map(dtoMapper::toSellResponse)
                .collect(Collectors.toList());
    }

    @PostMapping
    public SellResponseDto create(
            @Validated({ValidationGroups.OnCreate.class, Default.class}) @RequestBody SellRequestDto dto) {
        return dtoMapper.toSellResponse(sellService.save(dtoMapper.toSell(dto)));
    }

    @PutMapping
    public SellResponseDto update(
            @Validated({ValidationGroups.OnUpdate.class, Default.class}) @RequestBody SellRequestDto dto) {
        return dtoMapper.toSellResponse(sellService.update(dtoMapper.mergeSell(dto)));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        sellService.remove(sellService.loadById(id));
    }
}
