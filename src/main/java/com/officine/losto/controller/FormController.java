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
@RequestMapping(value = "/api/forms", produces = "application/json")
public class FormController {
    private final FormService formService;
    private final DtoMapper dtoMapper;

    public FormController(FormService formService, DtoMapper dtoMapper) {
        this.formService = formService;
        this.dtoMapper = dtoMapper;
    }

    @GetMapping
    public List<FormResponseDto> getAll() {
        return formService.getAll().stream().map(dtoMapper::toFormResponse).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public FormResponseDto getById(@PathVariable long id) {
        return dtoMapper.toFormResponse(formService.loadById(id));
    }

    @GetMapping("/by-code")
    public FormResponseDto getByCode(@RequestParam String code) {
        return dtoMapper.toFormResponse(formService.loadByCode(code));
    }

    @GetMapping("/search")
    public List<FormResponseDto> search(
            @RequestParam(defaultValue = "") String code,
            @RequestParam(defaultValue = "") String description) {
        return formService.findByCriteria(code, description).stream().map(dtoMapper::toFormResponse).collect(Collectors.toList());
    }

    @PostMapping
    public FormResponseDto create(
            @Validated({ValidationGroups.OnCreate.class, Default.class}) @RequestBody FormRequestDto dto) {
        return dtoMapper.toFormResponse(formService.save(dtoMapper.toForm(dto)));
    }

    @PutMapping
    public FormResponseDto update(
            @Validated({ValidationGroups.OnUpdate.class, Default.class}) @RequestBody FormRequestDto dto) {
        return dtoMapper.toFormResponse(formService.update(dtoMapper.toForm(dto)));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        formService.remove(formService.loadById(id));
    }
}
