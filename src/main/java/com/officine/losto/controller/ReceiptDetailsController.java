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
@RequestMapping(value = "/api/receipt-details", produces = "application/json")
public class ReceiptDetailsController {
    private final ReceiptDetailsService receiptDetailsService;
    private final DtoMapper dtoMapper;

    public ReceiptDetailsController(ReceiptDetailsService receiptDetailsService, DtoMapper dtoMapper) {
        this.receiptDetailsService = receiptDetailsService;
        this.dtoMapper = dtoMapper;
    }

    @GetMapping
    public List<ReceiptDetailsResponseDto> getAll() {
        return receiptDetailsService.getAll().stream().map(dtoMapper::toReceiptDetailsResponse).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ReceiptDetailsResponseDto getById(@PathVariable long id) {
        return dtoMapper.toReceiptDetailsResponse(receiptDetailsService.loadById(id));
    }

    @PostMapping
    public ReceiptDetailsResponseDto create(
            @Validated({ValidationGroups.OnCreate.class, Default.class}) @RequestBody ReceiptDetailsRequestDto dto) {
        return dtoMapper.toReceiptDetailsResponse(receiptDetailsService.save(dtoMapper.toReceiptDetails(dto)));
    }

    @PutMapping
    public ReceiptDetailsResponseDto update(
            @Validated({ValidationGroups.OnUpdate.class, Default.class}) @RequestBody ReceiptDetailsRequestDto dto) {
        return dtoMapper.toReceiptDetailsResponse(receiptDetailsService.update(dtoMapper.toReceiptDetails(dto)));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        receiptDetailsService.remove(receiptDetailsService.loadById(id));
    }
}
