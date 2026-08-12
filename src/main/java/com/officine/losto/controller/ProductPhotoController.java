package com.officine.losto.controller;

import com.officine.losto.dto.*;
import com.officine.losto.dto.mapper.*;
import com.officine.losto.service.*;
import org.springframework.core.io.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.*;

import java.io.*;

@RestController
@RequestMapping("/api/products")
public class ProductPhotoController {

    private final ProductPhotoStorageService productPhotoStorageService;
    private final DtoMapper dtoMapper;

    public ProductPhotoController(ProductPhotoStorageService productPhotoStorageService, DtoMapper dtoMapper) {
        this.productPhotoStorageService = productPhotoStorageService;
        this.dtoMapper = dtoMapper;
    }

    @GetMapping("/{id}/photo")
    public ResponseEntity<Resource> getPhoto(@PathVariable long id) throws IOException {
        return productPhotoStorageService.loadPhoto(id)
                .map(pr -> ResponseEntity.ok()
                        .contentType(pr.mediaType())
                        .header(HttpHeaders.CACHE_CONTROL, "private, max-age=3600")
                        .body(pr.resource()))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(value = "/{id}/photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ProductResponseDto uploadPhoto(@PathVariable long id, @RequestParam("file") MultipartFile file) throws IOException {
        return dtoMapper.toProductResponse(productPhotoStorageService.replacePhoto(id, file));
    }

    @DeleteMapping(value = "/{id}/photo", produces = MediaType.APPLICATION_JSON_VALUE)
    public ProductResponseDto deletePhoto(@PathVariable long id) {
        return dtoMapper.toProductResponse(productPhotoStorageService.deletePhoto(id));
    }
}
