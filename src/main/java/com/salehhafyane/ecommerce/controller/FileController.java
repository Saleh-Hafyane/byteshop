// Handles image uploads from the admin form and serves the stored images back to browsers.
package com.salehhafyane.ecommerce.controller;

import com.salehhafyane.ecommerce.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.concurrent.TimeUnit;

// Upload is admin-only; serving images back is public so product pages can show them.
@RestController
@RequiredArgsConstructor
public class FileController {

    private final FileStorageService fileStorageService;

    // POST /api/admin/products/upload — stores the file and returns its future URL, e.g. {url: "/api/files/abc.png"}.
    @PostMapping(value = "/api/admin/products/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> upload(@RequestParam("file") MultipartFile file) {
        String filename = fileStorageService.store(file);
        return ResponseEntity.ok(Map.of("url", "/api/files/" + filename));
    }

    // GET /api/files/{name} — streams the image with its real content type and a one-year cache.
    @GetMapping("/api/files/{filename:[a-zA-Z0-9._-]+}")
    public ResponseEntity<Resource> serve(@PathVariable String filename) {
        Resource resource = fileStorageService.load(filename);
        MediaType contentType = MediaTypeFactory.getMediaType(resource)
                .orElse(MediaType.APPLICATION_OCTET_STREAM);
        return ResponseEntity.ok()
                .contentType(contentType)
                .cacheControl(CacheControl.maxAge(365, TimeUnit.DAYS).cachePublic())
                .body(resource);
    }
}
