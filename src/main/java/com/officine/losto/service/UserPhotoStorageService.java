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
public class UserPhotoStorageService {

    private static final Set<String> ALLOWED_EXT = Set.of("jpg", "jpeg", "png", "gif", "webp", "bmp");

    private final UserRepo userRepo;
    private final UserPhotosProperties properties;

    public UserPhotoStorageService(UserRepo userRepo, UserPhotosProperties properties) {
        this.userRepo = userRepo;
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
    public AppUser replacePhoto(long userId, MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Empty file");
        }
        AppUser user = userRepo.findById(userId).orElseThrow();
        String ext = extensionOf(file.getOriginalFilename());
        if (ext == null || !ALLOWED_EXT.contains(ext.toLowerCase(Locale.ROOT))) {
            throw new IllegalArgumentException("Unsupported image type");
        }
        deleteStoredFileIfPresent(user);
        String storedName = UUID.randomUUID() + "." + ext.toLowerCase(Locale.ROOT);
        Path target = properties.getDirectory().resolve(storedName);
        Files.copy(file.getInputStream(), target);
        user.setPhotoFilename(storedName);
        return userRepo.save(user);
    }

    @Transactional
    public AppUser deletePhoto(long userId) {
        AppUser user = userRepo.findById(userId).orElseThrow();
        deleteStoredFileIfPresent(user);
        user.setPhotoFilename(null);
        return userRepo.save(user);
    }

    public void deleteStoredFileIfPresent(AppUser user) {
        if (user.getPhotoFilename() == null || user.getPhotoFilename().isBlank()) {
            return;
        }
        Path p = properties.getDirectory().resolve(user.getPhotoFilename());
        try {
            Files.deleteIfExists(p);
        } catch (IOException ignored) {
        }
    }

    public Optional<PhotoResource> loadPhoto(long userId) throws IOException {
        AppUser user = userRepo.findById(userId).orElse(null);
        if (user == null || user.getPhotoFilename() == null || user.getPhotoFilename().isBlank()) {
            return Optional.empty();
        }
        Path path = properties.getDirectory().resolve(user.getPhotoFilename());
        if (!Files.isRegularFile(path)) {
            return Optional.empty();
        }
        String ext = extensionOf(user.getPhotoFilename());
        MediaType mediaType = mediaTypeForExtension(ext);
        return Optional.of(new PhotoResource(new FileSystemResource(path), mediaType));
    }

    public record PhotoResource(Resource resource, MediaType mediaType) {
    }
}
