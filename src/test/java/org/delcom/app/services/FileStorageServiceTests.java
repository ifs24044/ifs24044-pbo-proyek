package org.delcom.app.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.MockedStatic;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit Test untuk FileStorageService - Restaurant Menu Management
 * Adapted from TodoCover to MenuItem
 */
class FileStorageServiceTest {

    private FileStorageService fileStorageService;
    private MultipartFile mockMultipartFile;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setup() {
        fileStorageService = new FileStorageService();
        // Override uploadDir dengan temporary directory untuk testing
        fileStorageService.uploadDir = tempDir.toString();
        mockMultipartFile = mock(MultipartFile.class);
    }

    // ============================================
    // TEST STORE FILE - SUCCESS SCENARIOS
    // ============================================

    @Test
    @DisplayName("Store file berhasil menyimpan gambar menu dengan extension")
    void storeFile_berhasil_menyimpan_gambar_menu_dengan_extension() throws Exception {
        // Arrange
        UUID menuId = UUID.randomUUID();
        String originalFilename = "nasi-goreng.jpg";
        String expectedFilename = "menu_" + menuId + ".jpg";
        byte[] fileContent = "fake image content".getBytes();

        when(mockMultipartFile.getOriginalFilename()).thenReturn(originalFilename);
        when(mockMultipartFile.getInputStream()).thenReturn(new ByteArrayInputStream(fileContent));

        // Act
        String result = fileStorageService.storeFile(mockMultipartFile, menuId);

        // Assert
        assertEquals(expectedFilename, result);

        // Verify file benar-benar ada dan contentnya sesuai
        Path expectedFile = tempDir.resolve(expectedFilename);
        assertTrue(Files.exists(expectedFile));
        assertArrayEquals(fileContent, Files.readAllBytes(expectedFile));
    }

    @Test
    @DisplayName("Store file berhasil dengan format PNG")
    void storeFile_berhasil_dengan_format_png() throws Exception {
        // Arrange
        UUID menuId = UUID.randomUUID();
        String originalFilename = "sate-ayam.png";
        String expectedFilename = "menu_" + menuId + ".png";
        byte[] fileContent = "fake png image".getBytes();

        when(mockMultipartFile.getOriginalFilename()).thenReturn(originalFilename);
        when(mockMultipartFile.getInputStream()).thenReturn(new ByteArrayInputStream(fileContent));

        // Act
        String result = fileStorageService.storeFile(mockMultipartFile, menuId);

        // Assert
        assertEquals(expectedFilename, result);
        assertTrue(Files.exists(tempDir.resolve(expectedFilename)));
    }

    @Test
    @DisplayName("Store file berhasil dengan format GIF")
    void storeFile_berhasil_dengan_format_gif() throws Exception {
        // Arrange
        UUID menuId = UUID.randomUUID();
        String originalFilename = "es-teh.gif";
        String expectedFilename = "menu_" + menuId + ".gif";
        byte[] fileContent = "fake gif image".getBytes();

        when(mockMultipartFile.getOriginalFilename()).thenReturn(originalFilename);
        when(mockMultipartFile.getInputStream()).thenReturn(new ByteArrayInputStream(fileContent));

        // Act
        String result = fileStorageService.storeFile(mockMultipartFile, menuId);

        // Assert
        assertEquals(expectedFilename, result);
        assertTrue(Files.exists(tempDir.resolve(expectedFilename)));
    }

    @Test
    @DisplayName("Store file berhasil tanpa extension ketika original filename null")
    void storeFile_berhasil_tanpa_extension_ketika_originalFilename_null() throws Exception {
        // Arrange
        UUID menuId = UUID.randomUUID();
        String expectedFilename = "menu_" + menuId.toString();
        byte[] fileContent = "fake content".getBytes();

        when(mockMultipartFile.getOriginalFilename()).thenReturn(null);
        when(mockMultipartFile.getInputStream()).thenReturn(new ByteArrayInputStream(fileContent));

        // Act
        String result = fileStorageService.storeFile(mockMultipartFile, menuId);

        // Assert
        assertEquals(expectedFilename, result);
        assertTrue(Files.exists(tempDir.resolve(expectedFilename)));
    }

    @Test
    @DisplayName("Store file berhasil tanpa extension ketika tidak ada dot")
    void storeFile_berhasil_tanpa_extension_ketika_tidak_ada_dot() throws Exception {
        // Arrange
        UUID menuId = UUID.randomUUID();
        String expectedFilename = "menu_" + menuId.toString();
        byte[] fileContent = "fake content".getBytes();

        when(mockMultipartFile.getOriginalFilename()).thenReturn("filename");
        when(mockMultipartFile.getInputStream()).thenReturn(new ByteArrayInputStream(fileContent));

        // Act
        String result = fileStorageService.storeFile(mockMultipartFile, menuId);

        // Assert
        assertEquals(expectedFilename, result);
        assertTrue(Files.exists(tempDir.resolve(expectedFilename)));
    }

    @Test
    @DisplayName("Store file berhasil dengan complex extension")
    void storeFile_berhasil_dengan_complex_extension() throws Exception {
        // Arrange
        UUID menuId = UUID.randomUUID();
        String originalFilename = "menu-special.final.jpeg";
        String expectedFilename = "menu_" + menuId + ".jpeg";
        byte[] fileContent = "fake jpeg content".getBytes();

        when(mockMultipartFile.getOriginalFilename()).thenReturn(originalFilename);
        when(mockMultipartFile.getInputStream()).thenReturn(new ByteArrayInputStream(fileContent));

        // Act
        String result = fileStorageService.storeFile(mockMultipartFile, menuId);

        // Assert
        assertEquals(expectedFilename, result);
        assertTrue(Files.exists(tempDir.resolve(expectedFilename)));
    }

    @Test
    @DisplayName("Store file membuat directory ketika belum ada")
    void storeFile_membuat_directory_ketika_belum_ada() throws Exception {
        // Arrange
        UUID menuId = UUID.randomUUID();
        Path customUploadDir = tempDir.resolve("custom-upload");
        fileStorageService.uploadDir = customUploadDir.toString();

        when(mockMultipartFile.getOriginalFilename()).thenReturn("menu-image.jpg");
        when(mockMultipartFile.getInputStream()).thenReturn(new ByteArrayInputStream("content".getBytes()));

        // Act
        String result = fileStorageService.storeFile(mockMultipartFile, menuId);

        // Assert
        assertTrue(Files.exists(customUploadDir));
        assertTrue(Files.isDirectory(customUploadDir));
        assertTrue(Files.exists(customUploadDir.resolve(result)));
    }

    @Test
    @DisplayName("Store file menggantikan gambar menu yang sudah ada (update)")
    void storeFile_menggantikan_gambar_menu_yang_sudah_ada() throws Exception {
        // Arrange
        UUID menuId = UUID.randomUUID();
        String originalFilename = "menu-baru.jpg";
        String expectedFilename = "menu_" + menuId + ".jpg";

        // Create existing file dengan content lama
        Path existingFile = tempDir.resolve(expectedFilename);
        Files.write(existingFile, "old image content".getBytes());

        byte[] newContent = "new image content".getBytes();

        when(mockMultipartFile.getOriginalFilename()).thenReturn(originalFilename);
        when(mockMultipartFile.getInputStream()).thenReturn(new ByteArrayInputStream(newContent));

        // Act
        String result = fileStorageService.storeFile(mockMultipartFile, menuId);

        // Assert
        assertEquals(expectedFilename, result);
        assertArrayEquals(newContent, Files.readAllBytes(existingFile));
    }

    // ============================================
    // TEST STORE FILE - ERROR SCENARIOS
    // ============================================

    @Test
    @DisplayName("Store file melemparkan exception ketika IOException terjadi")
    void storeFile_melemparkan_exception_ketika_ioexception_terjadi() throws Exception {
        // Arrange
        UUID menuId = UUID.randomUUID();

        when(mockMultipartFile.getOriginalFilename()).thenReturn("menu.jpg");
        when(mockMultipartFile.getInputStream()).thenThrow(new IOException("Simulated IO error"));

        // Act & Assert
        assertThrows(IOException.class, () -> {
            fileStorageService.storeFile(mockMultipartFile, menuId);
        });
    }

    // ============================================
    // TEST DELETE FILE
    // ============================================

    @Test
    @DisplayName("Delete file berhasil menghapus gambar menu yang ada")
    void deleteFile_berhasil_menghapus_gambar_menu_yang_ada() throws Exception {
        // Arrange
        String filename = "menu_abc123.jpg";
        Path testFile = tempDir.resolve(filename);
        Files.write(testFile, "menu image content".getBytes());
        assertTrue(Files.exists(testFile));

        // Act
        boolean result = fileStorageService.deleteFile(filename);

        // Assert
        assertTrue(result);
        assertFalse(Files.exists(testFile));
    }

    @Test
    @DisplayName("Delete file return false ketika file tidak ada")
    void deleteFile_return_false_ketika_file_tidak_ada() {
        // Arrange
        String nonExistentFilename = "menu_tidak-ada.jpg";

        // Act
        boolean result = fileStorageService.deleteFile(nonExistentFilename);

        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("Delete file return false ketika IOException terjadi")
    void deleteFile_return_false_ketika_ioexception() throws Exception {
        // Arrange
        String filename = "menu_test.jpg";
        Path filePath = Paths.get(fileStorageService.uploadDir).resolve(filename);

        // Mock Files class untuk melemparkan IOException
        try (MockedStatic<Files> filesMock = mockStatic(Files.class)) {
            filesMock.when(() -> Files.deleteIfExists(filePath))
                    .thenThrow(new IOException("Permission denied"));

            // Act
            boolean result = fileStorageService.deleteFile(filename);

            // Assert
            assertFalse(result);
        }
    }

    // ============================================
    // TEST LOAD FILE
    // ============================================

    @Test
    @DisplayName("Load file return path yang benar")
    void loadFile_return_path_yang_benar() {
        // Arrange
        String filename = "menu_xyz789.jpg";
        Path expectedPath = tempDir.resolve(filename);

        // Act
        Path result = fileStorageService.loadFile(filename);

        // Assert
        assertEquals(expectedPath, result);
    }

    @Test
    @DisplayName("Load file dengan berbagai format extension")
    void loadFile_dengan_berbagai_format_extension() {
        // Arrange & Act & Assert
        String[] filenames = {
            "menu_abc.jpg",
            "menu_def.png",
            "menu_ghi.gif",
            "menu_jkl.jpeg"
        };

        for (String filename : filenames) {
            Path expectedPath = tempDir.resolve(filename);
            Path result = fileStorageService.loadFile(filename);
            assertEquals(expectedPath, result);
        }
    }

    // ============================================
    // TEST FILE EXISTS
    // ============================================

    @Test
    @DisplayName("File exists return true ketika gambar menu ada")
    void fileExists_return_true_ketika_gambar_menu_ada() throws Exception {
        // Arrange
        String filename = "menu_existing.jpg";
        Path existingFile = tempDir.resolve(filename);
        Files.write(existingFile, "menu image content".getBytes());

        // Act
        boolean result = fileStorageService.fileExists(filename);

        // Assert
        assertTrue(result);
    }

    @Test
    @DisplayName("File exists return false ketika gambar menu tidak ada")
    void fileExists_return_false_ketika_gambar_menu_tidak_ada() {
        // Arrange
        String nonExistentFilename = "menu_tidak-ada.jpg";

        // Act
        boolean result = fileStorageService.fileExists(nonExistentFilename);

        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("File exists return false ketika filename null")
    void fileExists_return_false_ketika_filename_null() {
        // Act
        boolean result = fileStorageService.fileExists(null);

        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("File exists return false ketika filename kosong")
    void fileExists_return_false_ketika_filename_kosong() {
        // Act
        boolean result = fileStorageService.fileExists("");

        // Assert
        assertFalse(result);
    }

    // ============================================
    // TEST GET UPLOAD DIR
    // ============================================

    @Test
    @DisplayName("Get upload dir return directory yang benar")
    void getUploadDir_return_directory_yang_benar() {
        // Act
        String result = fileStorageService.getUploadDir();

        // Assert
        assertEquals(tempDir.toString(), result);
    }

    // ============================================
    // INTEGRATION TEST SCENARIOS
    // ============================================

    @Test
    @DisplayName("Skenario lengkap: store, exists, load, delete")
    void skenario_lengkap_store_exists_load_delete() throws Exception {
        // Arrange
        UUID menuId = UUID.randomUUID();
        String originalFilename = "rendang.jpg";
        byte[] fileContent = "rendang image".getBytes();

        when(mockMultipartFile.getOriginalFilename()).thenReturn(originalFilename);
        when(mockMultipartFile.getInputStream()).thenReturn(new ByteArrayInputStream(fileContent));

        // Act & Assert - STORE
        String filename = fileStorageService.storeFile(mockMultipartFile, menuId);
        assertNotNull(filename);

        // Act & Assert - EXISTS
        assertTrue(fileStorageService.fileExists(filename));

        // Act & Assert - LOAD
        Path loadedPath = fileStorageService.loadFile(filename);
        assertTrue(Files.exists(loadedPath));
        assertArrayEquals(fileContent, Files.readAllBytes(loadedPath));

        // Act & Assert - DELETE
        boolean deleted = fileStorageService.deleteFile(filename);
        assertTrue(deleted);
        assertFalse(fileStorageService.fileExists(filename));
    }

    @Test
    @DisplayName("Multiple menu dapat menyimpan gambar dengan UUID berbeda")
    void multiple_menu_dapat_menyimpan_gambar_dengan_uuid_berbeda() throws Exception {
        // Arrange
        UUID menuId1 = UUID.randomUUID();
        UUID menuId2 = UUID.randomUUID();
        UUID menuId3 = UUID.randomUUID();

        MultipartFile file1 = mock(MultipartFile.class);
        MultipartFile file2 = mock(MultipartFile.class);
        MultipartFile file3 = mock(MultipartFile.class);

        when(file1.getOriginalFilename()).thenReturn("soto.jpg");
        when(file1.getInputStream()).thenReturn(new ByteArrayInputStream("soto".getBytes()));

        when(file2.getOriginalFilename()).thenReturn("gado-gado.png");
        when(file2.getInputStream()).thenReturn(new ByteArrayInputStream("gado-gado".getBytes()));

        when(file3.getOriginalFilename()).thenReturn("es-campur.gif");
        when(file3.getInputStream()).thenReturn(new ByteArrayInputStream("es-campur".getBytes()));

        // Act
        String filename1 = fileStorageService.storeFile(file1, menuId1);
        String filename2 = fileStorageService.storeFile(file2, menuId2);
        String filename3 = fileStorageService.storeFile(file3, menuId3);

        // Assert
        assertTrue(fileStorageService.fileExists(filename1));
        assertTrue(fileStorageService.fileExists(filename2));
        assertTrue(fileStorageService.fileExists(filename3));

        // Verify different filenames
        assertNotEquals(filename1, filename2);
        assertNotEquals(filename2, filename3);
        assertNotEquals(filename1, filename3);
    }
}