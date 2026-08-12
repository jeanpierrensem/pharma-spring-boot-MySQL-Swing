package com.officine.losto.controller;

import com.officine.losto.dto.*;
import com.officine.losto.dto.mapper.*;
import com.officine.losto.entity.*;
import com.officine.losto.service.*;
import com.officine.losto.validation.*;
import jakarta.validation.groups.*;
import org.springframework.validation.annotation.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.*;

@RestController
@RequestMapping(value = "/api/sell-details", produces = "application/json")
public class SellDetailsController {
    private final SellDetailsService sellDetailsService;
    private final SellService sellService;
    private final DtoMapper dtoMapper;

    public SellDetailsController(SellDetailsService sellDetailsService, SellService sellService, DtoMapper dtoMapper) {
        this.sellDetailsService = sellDetailsService;
        this.sellService = sellService;
        this.dtoMapper = dtoMapper;
    }

    @GetMapping
    public List<SellDetailsResponseDto> getAll() {
        return sellDetailsService.getAll().stream().map(dtoMapper::toSellDetailsResponse).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public SellDetailsResponseDto getById(@PathVariable long id) {
        return dtoMapper.toSellDetailsResponse(sellDetailsService.loadById(id));
    }

    @GetMapping("/by-sell/{sellId}")
    public List<SellDetailsResponseDto> getBySell(@PathVariable long sellId) {
        Sell sell = sellService.loadById(sellId);
        return sellDetailsService.loadBySell(sell).stream().map(dtoMapper::toSellDetailsResponse).collect(Collectors.toList());
    }

    @PostMapping
    public SellDetailsResponseDto create(
            @Validated({ValidationGroups.OnCreate.class, Default.class}) @RequestBody SellDetailsRequestDto dto) {
        return dtoMapper.toSellDetailsResponse(sellDetailsService.save(dtoMapper.toSellDetails(dto)));
    }

    @PutMapping
    public SellDetailsResponseDto update(
            @Validated({ValidationGroups.OnUpdate.class, Default.class}) @RequestBody SellDetailsRequestDto dto) {
        return dtoMapper.toSellDetailsResponse(sellDetailsService.update(dtoMapper.toSellDetails(dto)));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        sellDetailsService.remove(sellDetailsService.loadById(id));
    }
}
