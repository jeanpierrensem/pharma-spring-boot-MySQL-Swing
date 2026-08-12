package com.officine.losto.controller;

import com.officine.losto.dto.*;
import com.officine.losto.dto.mapper.*;
import com.officine.losto.service.*;
import com.officine.losto.validation.*;
import jakarta.validation.groups.*;
import org.springframework.transaction.annotation.*;
import org.springframework.validation.annotation.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.*;

@RestController
@RequestMapping(value = "/api/menus", produces = "application/json")
public class MenuController {
    private final MenuService menuService;
    private final DtoMapper dtoMapper;

    public MenuController(MenuService menuService, DtoMapper dtoMapper) {
        this.menuService = menuService;
        this.dtoMapper = dtoMapper;
    }

    @GetMapping
    public List<MenuResponseDto> getAll() {
        return menuService.getAll().stream().map(dtoMapper::toMenuResponse).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public MenuResponseDto getById(@PathVariable long id) {
        return dtoMapper.toMenuResponse(menuService.loadById(id));
    }

    @GetMapping("/by-name")
    public MenuResponseDto getByName(@RequestParam String name) {
        return dtoMapper.toMenuResponse(menuService.loadByName(name));
    }

    @GetMapping("/search")
    public List<MenuResponseDto> search(
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "") String description) {
        return menuService.findByCriteria(name, description).stream().map(dtoMapper::toMenuResponse).collect(Collectors.toList());
    }

    /** Arbre habilitation (profondeur 3), sans état de sélection par groupe (plus de cases à cocher côté API). */
    /**
     * Transaction ouverte jusqu’au mapping DTO (évite LazyInitializationException sur {@link com.officine.losto.entity.Menu#getChildren()}).
     */
    @GetMapping("/tree/habilitation")
    @Transactional(readOnly = true)
    public List<MenuTreeNodeDto> getHabilitationTree() {
        return menuService.loadHabilitationMenuTree().stream()
                .map(dtoMapper::toMenuTreeNode)
                .collect(Collectors.toList());
    }

    /**
     * Ré-applique le catalogue {@link com.officine.losto.catalog.MenuSecurityCatalog} en base (après évolution des IHM).
     */
    @PostMapping("/sync-catalog")
    public int syncCatalog() {
        return menuService.syncHabilitationCatalog();
    }

    @PostMapping
    public MenuResponseDto create(
            @Validated({ValidationGroups.OnCreate.class, Default.class}) @RequestBody MenuRequestDto dto) {
        return dtoMapper.toMenuResponse(menuService.save(dtoMapper.toMenu(dto)));
    }

    @PutMapping
    public MenuResponseDto update(
            @Validated({ValidationGroups.OnUpdate.class, Default.class}) @RequestBody MenuRequestDto dto) {
        return dtoMapper.toMenuResponse(menuService.update(dtoMapper.toMenu(dto)));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        menuService.remove(menuService.loadById(id));
    }
}
