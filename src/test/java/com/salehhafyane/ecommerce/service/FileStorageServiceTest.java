// Pure unit test for the upload feature: valid files store and serve, bad input fails fast.
package com.salehhafyane.ecommerce.service;

import com.salehhafyane.ecommerce.exceptions.InvalidImageException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

// Runs against a temporary folder, so no Spring context and no real uploads dir.
class FileStorageServiceTest {

    // Fresh empty directory handed to the service by JUnit for each test.
    @TempDir
    Path tempDir;

    // Smallest byte sequence that counts as a real PNG for the happy-path test.
    private static byte[] minimalPng() {
        return new byte[]{(byte) 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A,
                0x00, 0x00, 0x00, 0x0D, 0x49, 0x48, 0x44, 0x52,
                0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x01,
                0x08, 0x02, 0x00, 0x00, 0x00, (byte) 0x90, 0x77, 0x53, (byte) 0xDE};
    }

    @Test
    void storeValidPngWritesUuidFileAndServesIt() throws Exception {
        FileStorageService service = new FileStorageService(tempDir.toString());
        MockMultipartFile file = new MockMultipartFile("file", "photo.png", "image/png", minimalPng());

        String filename = service.store(file);

        assertTrue(filename.matches("[0-9a-f-]+\\.png"), "Stored name must be a UUID: " + filename);
        assertArrayEquals(minimalPng(), Files.readAllBytes(tempDir.resolve(filename)));

        Resource resource = service.load(filename);
        assertTrue(resource.exists());
        assertTrue(resource.contentLength() > 0);
    }

    @Test
    void storeTextFileThrows() {
        FileStorageService service = new FileStorageService(tempDir.toString());
        MockMultipartFile file = new MockMultipartFile("file", "notes.txt", "text/plain", "hello".getBytes());

        assertThrows(InvalidImageException.class, () -> service.store(file));
    }

    @Test
    void storeOversizeFileThrows() {
        FileStorageService service = new FileStorageService(tempDir.toString());
        byte[] big = new byte[5 * 1024 * 1024 + 1];
        MockMultipartFile file = new MockMultipartFile("file", "big.png", "image/png", big);

        assertThrows(InvalidImageException.class, () -> service.store(file));
    }

    @Test
    void loadMissingFileThrows() {
        FileStorageService service = new FileStorageService(tempDir.toString());

        assertThrows(InvalidImageException.class, () -> service.load("does-not-exist.png"));
    }
}
