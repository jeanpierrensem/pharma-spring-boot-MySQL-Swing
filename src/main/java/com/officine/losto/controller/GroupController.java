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
@RequestMapping(value = "/api/groups", produces = "application/json")
public class GroupController {
    private final GroupService groupService;
    private final DtoMapper dtoMapper;

    public GroupController(GroupService groupService, DtoMapper dtoMapper) {
        this.groupService = groupService;
        this.dtoMapper = dtoMapper;
    }

    @GetMapping
    public List<AppGroupResponseDto> getAll() {
        return groupService.getAll().stream().map(dtoMapper::toGroupResponse).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public AppGroupResponseDto getById(@PathVariable long id) {
        return dtoMapper.toGroupResponse(groupService.loadById(id));
    }

    @GetMapping("/by-name")
    public AppGroupResponseDto getByName(@RequestParam String name) {
        return dtoMapper.toGroupResponse(groupService.loadByName(name));
    }

    @GetMapping("/search")
    public List<AppGroupResponseDto> search(
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "") String description) {
        return groupService.findByCriteria(name, description).stream().map(dtoMapper::toGroupResponse).collect(Collectors.toList());
    }

    @PostMapping
    public AppGroupResponseDto create(
            @Validated({ValidationGroups.OnCreate.class, Default.class}) @RequestBody AppGroupRequestDto dto) {
        return dtoMapper.toGroupResponse(groupService.save(dtoMapper.toAppGroup(dto)));
    }

    @PutMapping
    public AppGroupResponseDto update(
            @Validated({ValidationGroups.OnUpdate.class, Default.class}) @RequestBody AppGroupRequestDto dto) {
        AppGroupResponseDto response = dtoMapper.toGroupResponse(groupService.update(dtoMapper.toAppGroup(dto)));
        if (dto.getMenuIds() != null && dto.getId() != null) {
            groupService.assignMenusToGroup(dto.getId(), dto.getMenuIds());
            return dtoMapper.toGroupResponse(groupService.loadById(dto.getId()));
        }
        return response;
    }

    @PostMapping("/{id}/menus")
    public AppGroupResponseDto updateMenus(
            @PathVariable long id,
            @RequestBody GroupMenusUpdateRequestDto dto) {
        groupService.assignMenusToGroup(id, dto == null ? null : dto.getMenuIds());
        return dtoMapper.toGroupResponse(groupService.loadById(id));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        groupService.remove(groupService.loadById(id));
    }
}
