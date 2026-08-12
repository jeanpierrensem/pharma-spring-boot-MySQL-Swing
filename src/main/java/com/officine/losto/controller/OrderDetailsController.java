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
@RequestMapping(value = "/api/order-details", produces = "application/json")
public class OrderDetailsController {
    private final OrderDetailsService orderDetailsService;
    private final OrdersService ordersService;
    private final DtoMapper dtoMapper;

    public OrderDetailsController(
            OrderDetailsService orderDetailsService,
            OrdersService ordersService,
            DtoMapper dtoMapper) {
        this.orderDetailsService = orderDetailsService;
        this.ordersService = ordersService;
        this.dtoMapper = dtoMapper;
    }

    @GetMapping
    public List<OrdersDetailsResponseDto> getAll() {
        return orderDetailsService.getAll().stream().map(dtoMapper::toOrdersDetailsResponse).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public OrdersDetailsResponseDto getById(@PathVariable long id) {
        return dtoMapper.toOrdersDetailsResponse(orderDetailsService.loadById(id));
    }

    @GetMapping("/by-order/{orderId}")
    public OrdersDetailsResponseDto getByOrder(@PathVariable long orderId) {
        Orders order = ordersService.loadById(orderId);
        return dtoMapper.toOrdersDetailsResponse(orderDetailsService.loadByOrders(order));
    }

    @PostMapping
    public OrdersDetailsResponseDto create(
            @Validated({ValidationGroups.OnCreate.class, Default.class}) @RequestBody OrdersDetailsRequestDto dto) {
        return dtoMapper.toOrdersDetailsResponse(orderDetailsService.save(dtoMapper.toOrdersDetails(dto)));
    }

    @PutMapping
    public OrdersDetailsResponseDto update(
            @Validated({ValidationGroups.OnUpdate.class, Default.class}) @RequestBody OrdersDetailsRequestDto dto) {
        return dtoMapper.toOrdersDetailsResponse(orderDetailsService.update(dtoMapper.toOrdersDetails(dto)));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        orderDetailsService.remove(orderDetailsService.loadById(id));
    }
}
