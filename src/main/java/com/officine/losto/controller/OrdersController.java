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
@RequestMapping(value = "/api/orders", produces = "application/json")
public class OrdersController {
    private final OrdersService ordersService;
    private final DtoMapper dtoMapper;

    public OrdersController(OrdersService ordersService, DtoMapper dtoMapper) {
        this.ordersService = ordersService;
        this.dtoMapper = dtoMapper;
    }

    @GetMapping
    public List<OrdersResponseDto> getAll() {
        return ordersService.getAll().stream().map(dtoMapper::toOrdersResponse).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public OrdersResponseDto getById(@PathVariable long id) {
        return dtoMapper.toOrdersResponse(ordersService.loadById(id));
    }

    @GetMapping("/by-code")
    public OrdersResponseDto getByCode(@RequestParam String code) {
        return dtoMapper.toOrdersResponse(ordersService.loadByCode(code));
    }

    @GetMapping("/search")
    public List<OrdersResponseDto> search(
            @RequestParam(defaultValue = "") String number,
            @RequestParam(defaultValue = "") String orderDate,
            @RequestParam(defaultValue = "") String description) {
        return ordersService.findByCriteria(number, orderDate, description).stream()
                .map(dtoMapper::toOrdersResponse)
                .collect(Collectors.toList());
    }

    @PostMapping
    public OrdersResponseDto create(
            @Validated({ValidationGroups.OnCreate.class, Default.class}) @RequestBody OrdersRequestDto dto) {
        return dtoMapper.toOrdersResponse(ordersService.save(dtoMapper.toOrders(dto)));
    }

    @PutMapping
    public OrdersResponseDto update(
            @Validated({ValidationGroups.OnUpdate.class, Default.class}) @RequestBody OrdersRequestDto dto) {
        return dtoMapper.toOrdersResponse(ordersService.update(dtoMapper.toOrders(dto)));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        ordersService.remove(ordersService.loadById(id));
    }
}
