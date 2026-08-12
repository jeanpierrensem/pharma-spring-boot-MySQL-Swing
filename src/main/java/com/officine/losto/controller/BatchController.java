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
@RequestMapping(value = "/api/batches", produces = "application/json")
public class BatchController {
    private final BatchService batchService;
    private final DtoMapper dtoMapper;

    public BatchController(BatchService batchService, DtoMapper dtoMapper) {
        this.batchService = batchService;
        this.dtoMapper = dtoMapper;
    }

    @GetMapping
    public List<BatchResponseDto> getAll() {
        return batchService.getAll().stream().map(dtoMapper::toBatchResponse).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public BatchResponseDto getById(@PathVariable long id) {
        return dtoMapper.toBatchResponse(batchService.loadById(id));
    }

    @GetMapping("/by-code")
    public BatchResponseDto getByCode(@RequestParam String code) {
        return dtoMapper.toBatchResponse(batchService.loadByCode(code));
    }

    @GetMapping("/search")
    public List<BatchResponseDto> search(@RequestParam(defaultValue = "") String code) {
        return batchService.findByCriteria(code).stream().map(dtoMapper::toBatchResponse).collect(Collectors.toList());
    }

    @GetMapping("/by-provider/{providerId}")
    public List<BatchResponseDto> listByProvider(@PathVariable long providerId) {
        return batchService.findByProviderId(providerId).stream()
                .map(dtoMapper::toBatchResponse)
                .collect(Collectors.toList());
    }

    @PostMapping
    public BatchResponseDto create(
            @Validated({ValidationGroups.OnCreate.class, Default.class}) @RequestBody BatchRequestDto dto) {
        return dtoMapper.toBatchResponse(batchService.save(dtoMapper.toBatch(dto)));
    }

    @PutMapping
    public BatchResponseDto update(
            @Validated({ValidationGroups.OnUpdate.class, Default.class}) @RequestBody BatchRequestDto dto) {
        return dtoMapper.toBatchResponse(batchService.update(dtoMapper.toBatch(dto)));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        batchService.remove(batchService.loadById(id));
    }
}
