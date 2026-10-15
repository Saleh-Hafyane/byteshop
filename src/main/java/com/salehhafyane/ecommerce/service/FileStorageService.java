// Saves uploaded product images to a folder on disk and serves them back by filename.
package com.salehhafyane.ecommerce.service;

import com.salehhafyane.ecommerce.exceptions.InvalidImageException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

// Only these image types are accepted; anything else is rejected with 415.
@Service
public class FileStorageService {

    private static final Set<String> ALLOWED_TYPES = Set.of(
            "image/jpeg", "image/png", "image/webp", "image/gif");
    // Real file extension is decided by the content type, never by the client's filename.
    private static final Map<String, String> EXT_BY_TYPE = Map.of(
            "image/jpeg", ".jpg",
            "image/png", ".png",
            "image/webp", ".webp",
            "image/gif", ".gif");
    private static final long MAX_SIZE = 5L * 1024 * 1024; // 5 MB cap, checked before writing anything

    private final Path uploadDir;

    // Creates the upload folder at startup so the first request never fails on a missing directory.
    public FileStorageService(@Value("${app.upload.dir:uploads}") String uploadDir) {
        this.uploadDir = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.uploadDir);
        } catch (IOException e) {
            throw new InvalidImageException("Could not initialize upload directory");
        }
    }

    // Same guarantee at bean startup when Spring runs this service.
    @PostConstruct
    void init() throws IOException {
        Files.createDirectories(uploadDir);
    }

    // Validates type and size, then writes the bytes under a random UUID name and returns that name.
    public String store(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new InvalidImageException("Image file is required");
        }
        if (file.getSize() > MAX_SIZE) {
            throw new InvalidImageException("Image must not exceed 5 MB");
        }
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_TYPES.contains(contentType.toLowerCase())) {
            throw new InvalidImageException("Only JPEG, PNG, WEBP or GIF images are allowed");
        }
        String ext = EXT_BY_TYPE.get(contentType.toLowerCase());
        String filename = UUID.randomUUID() + ext; // random name prevents overwrites and path tricks
        try (InputStream in = file.getInputStream()) {
            Files.copy(in, uploadDir.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new InvalidImageException("Could not store image");
        }
        return filename;
    }

    // Reads a stored image back; the strict name check blocks path traversal like ../../etc/passwd.
    public Resource load(String filename) {
        if (filename == null || !filename.matches("[a-zA-Z0-9._-]+")) {
            throw new InvalidImageException("Invalid filename");
        }
        try {
            Path file = uploadDir.resolve(filename).normalize();
            if (!file.startsWith(uploadDir) || !Files.isRegularFile(file)) {
                throw new InvalidImageException("Image not found");
            }
            return new UrlResource(file.toUri());
        } catch (MalformedURLException e) {
            throw new InvalidImageException("Image not found");
        }
    }
}
