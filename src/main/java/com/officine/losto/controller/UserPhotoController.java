package com.officine.losto.controller;

import com.officine.losto.dto.*;
import com.officine.losto.dto.mapper.*;
import com.officine.losto.service.*;
import org.springframework.core.io.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.*;

import java.io.*;

/**
 * Serves and updates user profile photos (binary), separate from {@link UserController} JSON defaults.
 */
@RestController
@RequestMapping("/api/users")
public class UserPhotoController {

    private final UserPhotoStorageService userPhotoStorageService;
    private final DtoMapper dtoMapper;

    public UserPhotoController(UserPhotoStorageService userPhotoStorageService, DtoMapper dtoMapper) {
        this.userPhotoStorageService = userPhotoStorageService;
        this.dtoMapper = dtoMapper;
    }

    @GetMapping("/{id}/photo")
    public ResponseEntity<Resource> getPhoto(@PathVariable long id) throws IOException {
        return userPhotoStorageService.loadPhoto(id)
                .map(pr -> ResponseEntity.ok()
                        .contentType(pr.mediaType())
                        .header(HttpHeaders.CACHE_CONTROL, "private, max-age=3600")
                        .body(pr.resource()))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(value = "/{id}/photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public AppUserResponseDto uploadPhoto(@PathVariable long id, @RequestParam("file") MultipartFile file) throws IOException {
        return dtoMapper.toUserResponse(userPhotoStorageService.replacePhoto(id, file));
    }

    @DeleteMapping(value = "/{id}/photo", produces = MediaType.APPLICATION_JSON_VALUE)
    public AppUserResponseDto deletePhoto(@PathVariable long id) {
        return dtoMapper.toUserResponse(userPhotoStorageService.deletePhoto(id));
    }
}
