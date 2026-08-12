package com.officine.losto.service;

import com.officine.losto.config.*;
import com.officine.losto.entity.*;
import com.officine.losto.model.*;
import jakarta.annotation.*;
import org.springframework.core.io.*;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;
import org.springframework.web.multipart.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;

@Service
public class ProductPhotoStorageService {

    private static final Set<String> ALLOWED_EXT = Set.of("jpg", "jpeg", "png", "gif", "webp", "bmp");

    private final ProductRepo productRepo;
    private final ProductPhotosProperties properties;

    public ProductPhotoStorageService(ProductRepo productRepo, ProductPhotosProperties properties) {
        this.productRepo = productRepo;
        this.properties = properties;
    }

    private static MediaType mediaTypeForExtension(String ext) {
        if (ext == null) {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
        return switch (ext.toLowerCase(Locale.ROOT)) {
            case "jpg", "jpeg" -> MediaType.IMAGE_JPEG;
            case "png" -> MediaType.IMAGE_PNG;
            case "gif" -> MediaType.IMAGE_GIF;
            case "webp" -> MediaType.parseMediaType("image/webp");
            default -> MediaType.APPLICATION_OCTET_STREAM;
        };
    }

    private static String extensionOf(String name) {
        if (name == null) {
            return null;
        }
        int dot = name.lastIndexOf('.');
        if (dot < 0 || dot == name.length() - 1) {
            return null;
        }
        return name.substring(dot + 1);
    }

    @PostConstruct
    public void ensureDirectory() throws IOException {
        Files.createDirectories(properties.getDirectory());
    }

    @Transactional
    public Product replacePhoto(long productId, MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Empty file");
        }
        Product p = productRepo.findById(productId).orElseThrow();
        String ext = extensionOf(file.getOriginalFilename());
        if (ext == null || !ALLOWED_EXT.contains(ext.toLowerCase(Locale.ROOT))) {
            throw new IllegalArgumentException("Unsupported image type");
        }
        deleteStoredFileIfPresent(p);
        String storedName = UUID.randomUUID() + "." + ext.toLowerCase(Locale.ROOT);
        Path target = properties.getDirectory().resolve(storedName);
        Files.copy(file.getInputStream(), target);
        p.setPhotoFilename(storedName);
        return productRepo.save(p);
    }

    @Transactional
    public Product deletePhoto(long productId) {
        Product p = productRepo.findById(productId).orElseThrow();
        deleteStoredFileIfPresent(p);
        p.setPhotoFilename(null);
        return productRepo.save(p);
    }

    public void deleteStoredFileIfPresent(Product p) {
        if (p.getPhotoFilename() == null || p.getPhotoFilename().isBlank()) {
            return;
        }
        Path path = properties.getDirectory().resolve(p.getPhotoFilename());
        try {
            Files.deleteIfExists(path);
        } catch (IOException ignored) {
        }
    }

    public Optional<PhotoResource> loadPhoto(long productId) throws IOException {
        Product p = productRepo.findById(productId).orElse(null);
        if (p == null || p.getPhotoFilename() == null || p.getPhotoFilename().isBlank()) {
            return Optional.empty();
        }
        Path path = properties.getDirectory().resolve(p.getPhotoFilename());
        if (!Files.isRegularFile(path)) {
            return Optional.empty();
        }
        String ext = extensionOf(p.getPhotoFilename());
        return Optional.of(new PhotoResource(new FileSystemResource(path), mediaTypeForExtension(ext)));
    }

    public record PhotoResource(Resource resource, MediaType mediaType) {
    }
}
