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
@RequestMapping(value = "/api/categories", produces = "application/json")
public class CategoryController {
    private final CategoryService categoryService;
    private final DtoMapper dtoMapper;

    public CategoryController(CategoryService categoryService, DtoMapper dtoMapper) {
        this.categoryService = categoryService;
        this.dtoMapper = dtoMapper;
    }

    @GetMapping
    public List<CategoryResponseDto> getAll() {
        return categoryService.getAll().stream().map(dtoMapper::toCategoryResponse).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public CategoryResponseDto getById(@PathVariable long id) {
        return dtoMapper.toCategoryResponse(categoryService.loadById(id));
    }

    @GetMapping("/by-code")
    public CategoryResponseDto getByCode(@RequestParam String code) {
        return dtoMapper.toCategoryResponse(categoryService.loadByCode(code));
    }

    @GetMapping("/search")
    public List<CategoryResponseDto> search(
            @RequestParam(defaultValue = "") String code,
            @RequestParam(defaultValue = "") String description) {
        return categoryService.findByCriteria(code, description).stream().map(dtoMapper::toCategoryResponse).collect(Collectors.toList());
    }

    @PostMapping
    public CategoryResponseDto create(
            @Validated({ValidationGroups.OnCreate.class, Default.class}) @RequestBody CategoryRequestDto dto) {
        return dtoMapper.toCategoryResponse(categoryService.save(dtoMapper.toCategory(dto)));
    }

    @PutMapping
    public CategoryResponseDto update(
            @Validated({ValidationGroups.OnUpdate.class, Default.class}) @RequestBody CategoryRequestDto dto) {
        return dtoMapper.toCategoryResponse(categoryService.update(dtoMapper.toCategory(dto)));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        categoryService.remove(categoryService.loadById(id));
    }
}
