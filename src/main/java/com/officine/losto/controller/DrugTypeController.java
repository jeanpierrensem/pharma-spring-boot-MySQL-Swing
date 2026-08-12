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
@RequestMapping(value = "/api/drug-types", produces = "application/json")
public class DrugTypeController {
    private final DrugTypeService drugTypeService;
    private final DtoMapper dtoMapper;

    public DrugTypeController(DrugTypeService drugTypeService, DtoMapper dtoMapper) {
        this.drugTypeService = drugTypeService;
        this.dtoMapper = dtoMapper;
    }

    @GetMapping
    public List<DrugTypeResponseDto> getAll() {
        return drugTypeService.getAll().stream().map(dtoMapper::toDrugTypeResponse).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public DrugTypeResponseDto getById(@PathVariable long id) {
        return dtoMapper.toDrugTypeResponse(drugTypeService.loadById(id));
    }

    @GetMapping("/by-code")
    public DrugTypeResponseDto getByCode(@RequestParam String code) {
        return dtoMapper.toDrugTypeResponse(drugTypeService.loadByCode(code));
    }

    @GetMapping("/search")
    public List<DrugTypeResponseDto> search(
            @RequestParam(defaultValue = "") String code,
            @RequestParam(defaultValue = "") String description) {
        return drugTypeService.findByCriteria(code, description).stream().map(dtoMapper::toDrugTypeResponse).collect(Collectors.toList());
    }

    @PostMapping
    public DrugTypeResponseDto create(
            @Validated({ValidationGroups.OnCreate.class, Default.class}) @RequestBody DrugTypeRequestDto dto) {
        return dtoMapper.toDrugTypeResponse(drugTypeService.save(dtoMapper.toDrugType(dto)));
    }

    @PutMapping
    public DrugTypeResponseDto update(
            @Validated({ValidationGroups.OnUpdate.class, Default.class}) @RequestBody DrugTypeRequestDto dto) {
        return dtoMapper.toDrugTypeResponse(drugTypeService.update(dtoMapper.toDrugType(dto)));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        drugTypeService.remove(drugTypeService.loadById(id));
    }
}
