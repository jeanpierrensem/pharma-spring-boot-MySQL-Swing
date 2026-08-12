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
@RequestMapping(value = "/api/thresholds", produces = "application/json")
public class ThresholdController {
    private final ThresholdService thresholdService;
    private final DtoMapper dtoMapper;

    public ThresholdController(ThresholdService thresholdService, DtoMapper dtoMapper) {
        this.thresholdService = thresholdService;
        this.dtoMapper = dtoMapper;
    }

    @GetMapping
    public List<ThresholdResponseDto> getAll() {
        return thresholdService.getAll().stream().map(dtoMapper::toThresholdResponse).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ThresholdResponseDto getById(@PathVariable long id) {
        return dtoMapper.toThresholdResponse(thresholdService.loadById(id));
    }

    @GetMapping("/by-name")
    public ThresholdResponseDto getByName(@RequestParam String name) {
        return dtoMapper.toThresholdResponse(thresholdService.loadByName(name));
    }

    @GetMapping("/search")
    public List<ThresholdResponseDto> search(
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "") String description) {
        return thresholdService.findByCriteria(name, description).stream().map(dtoMapper::toThresholdResponse).collect(Collectors.toList());
    }

    @PostMapping
    public ThresholdResponseDto create(
            @Validated({ValidationGroups.OnCreate.class, Default.class}) @RequestBody ThresholdRequestDto dto) {
        return dtoMapper.toThresholdResponse(thresholdService.save(dtoMapper.toThreshold(dto)));
    }

    @PutMapping
    public ThresholdResponseDto update(
            @Validated({ValidationGroups.OnUpdate.class, Default.class}) @RequestBody ThresholdRequestDto dto) {
        return dtoMapper.toThresholdResponse(thresholdService.update(dtoMapper.toThreshold(dto)));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        thresholdService.remove(thresholdService.loadById(id));
    }
}
