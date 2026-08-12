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
@RequestMapping(value = "/api/sections", produces = "application/json")
public class SectionController {
    private final SectionService sectionService;
    private final DtoMapper dtoMapper;

    public SectionController(SectionService sectionService, DtoMapper dtoMapper) {
        this.sectionService = sectionService;
        this.dtoMapper = dtoMapper;
    }

    @GetMapping
    public List<SectionResponseDto> getAll() {
        return sectionService.getAll().stream().map(dtoMapper::toSectionResponse).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public SectionResponseDto getById(@PathVariable long id) {
        return dtoMapper.toSectionResponse(sectionService.loadById(id));
    }

    @GetMapping("/by-code")
    public SectionResponseDto getByCode(@RequestParam String code) {
        return dtoMapper.toSectionResponse(sectionService.loadByCode(code));
    }

    @GetMapping("/search")
    public List<SectionResponseDto> search(
            @RequestParam(defaultValue = "") String code,
            @RequestParam(defaultValue = "") String description) {
        return sectionService.findByCriteria(code, description).stream().map(dtoMapper::toSectionResponse).collect(Collectors.toList());
    }

    @PostMapping
    public SectionResponseDto create(
            @Validated({ValidationGroups.OnCreate.class, Default.class}) @RequestBody SectionRequestDto dto) {
        return dtoMapper.toSectionResponse(sectionService.save(dtoMapper.toSection(dto)));
    }

    @PutMapping
    public SectionResponseDto update(
            @Validated({ValidationGroups.OnUpdate.class, Default.class}) @RequestBody SectionRequestDto dto) {
        return dtoMapper.toSectionResponse(sectionService.update(dtoMapper.toSection(dto)));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        sectionService.remove(sectionService.loadById(id));
    }
}
