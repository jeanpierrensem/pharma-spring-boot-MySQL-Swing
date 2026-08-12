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
@RequestMapping(value = "/api/packagings", produces = "application/json")
public class PackagingController {
    private final PackagingService packagingService;
    private final DtoMapper dtoMapper;

    public PackagingController(PackagingService packagingService, DtoMapper dtoMapper) {
        this.packagingService = packagingService;
        this.dtoMapper = dtoMapper;
    }

    @GetMapping
    public List<PackagingResponseDto> getAll() {
        return packagingService.getAll().stream().map(dtoMapper::toPackagingResponse).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public PackagingResponseDto getById(@PathVariable long id) {
        return dtoMapper.toPackagingResponse(packagingService.loadById(id));
    }

    @GetMapping("/by-code")
    public PackagingResponseDto getByCode(@RequestParam String code) {
        return dtoMapper.toPackagingResponse(packagingService.loadByCode(code));
    }

    @GetMapping("/search")
    public List<PackagingResponseDto> search(
            @RequestParam(defaultValue = "") String code,
            @RequestParam(defaultValue = "") String description) {
        return packagingService.findByCriteria(code, description).stream().map(dtoMapper::toPackagingResponse).collect(Collectors.toList());
    }

    @PostMapping
    public PackagingResponseDto create(
            @Validated({ValidationGroups.OnCreate.class, Default.class}) @RequestBody PackagingRequestDto dto) {
        return dtoMapper.toPackagingResponse(packagingService.save(dtoMapper.toPackaging(dto)));
    }

    @PutMapping
    public PackagingResponseDto update(
            @Validated({ValidationGroups.OnUpdate.class, Default.class}) @RequestBody PackagingRequestDto dto) {
        return dtoMapper.toPackagingResponse(packagingService.update(dtoMapper.toPackaging(dto)));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        packagingService.remove(packagingService.loadById(id));
    }
}
