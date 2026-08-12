package com.officine.losto.controller;

import com.itextpdf.text.*;
import com.officine.losto.dto.*;
import com.officine.losto.dto.auth.*;
import com.officine.losto.dto.mapper.*;
import com.officine.losto.security.*;
import com.officine.losto.service.*;
import com.officine.losto.validation.*;
import jakarta.validation.groups.*;
import org.springframework.http.*;
import org.springframework.security.core.annotation.*;
import org.springframework.validation.annotation.*;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.*;
import java.time.*;
import java.util.List;
import java.util.stream.*;

@RestController
@RequestMapping(value = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
    private final UserService userService;
    private final DtoMapper dtoMapper;
    private final UserPdfExportService userPdfExportService;
    private final AuthService authService;

    public UserController(UserService userService, DtoMapper dtoMapper, UserPdfExportService userPdfExportService,
                          AuthService authService) {
        this.userService = userService;
        this.dtoMapper = dtoMapper;
        this.userPdfExportService = userPdfExportService;
        this.authService = authService;
    }

    @GetMapping(produces = "application/json")
    public List<AppUserResponseDto> getAll() {
        return userService.getAll().stream().map(dtoMapper::toUserResponse).collect(Collectors.toList());
    }

    @GetMapping("/me")
    public CurrentUserResponseDto me(@AuthenticationPrincipal OfficineUserDetails principal) {
        return authService.currentUser(principal.getUsername());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppUserResponseDto> getById(@PathVariable long id) {
        var user = userService.loadById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dtoMapper.toUserResponse(user));
    }

    @GetMapping("/by-name")
    public ResponseEntity<AppUserResponseDto> getByName(@RequestParam String name) {
        var user = userService.loadByName(name);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dtoMapper.toUserResponse(user));
    }

    @GetMapping("/search")
    public List<AppUserResponseDto> search(
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "") String login,
            @RequestParam(defaultValue = "") String email) {
        return userService.findByCriteria(name, login, email).stream().map(dtoMapper::toUserResponse).collect(Collectors.toList());
    }

    @PostMapping
    public AppUserResponseDto create(
            @Validated({ValidationGroups.OnCreate.class, Default.class}) @RequestBody AppUserRequestDto dto) {
        return dtoMapper.toUserResponse(userService.save(dtoMapper.toAppUser(dto)));
    }

    @PutMapping
    public AppUserResponseDto update(
            @Validated({ValidationGroups.OnUpdate.class, Default.class}) @RequestBody AppUserRequestDto dto) {
        return dtoMapper.toUserResponse(userService.update(dtoMapper.toAppUser(dto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        var user = userService.loadById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        userService.remove(user);
        return ResponseEntity.noContent().build();
    }

    /**
     * PDF export of users. If the body is omitted or {@code userIds} is null, all users are exported.
     * If {@code userIds} is an empty array, the PDF contains no data rows (header only).
     * Otherwise rows follow the given id order (unknown ids are skipped).
     */
    @PostMapping(value = "/export/pdf", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> exportUsersPdf(@RequestBody(required = false) UserExportPdfRequest req)
            throws DocumentException {
        List<AppUserResponseDto> dtos = resolveExportUsers(req);
        byte[] pdf = userPdfExportService.buildAllUsersListPdf(dtos);
        String filename = "officine-users-" + LocalDate.now() + ".pdf";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.attachment()
                .filename(filename, StandardCharsets.UTF_8)
                .build());
        headers.setCacheControl(CacheControl.noStore());
        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }

    private List<AppUserResponseDto> resolveExportUsers(UserExportPdfRequest req) {
        if (req == null || req.userIds() == null) {
            return userService.getAll().stream().map(dtoMapper::toUserResponse).toList();
        }
        if (req.userIds().isEmpty()) {
            return List.of();
        }
        return userService.findAllByIdsInOrder(req.userIds()).stream()
                .map(dtoMapper::toUserResponse)
                .toList();
    }
}
