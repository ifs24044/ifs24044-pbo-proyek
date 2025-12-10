package org.delcom.app.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MenuImageFormTests {

    private MenuImageForm menuImageForm;
    private MultipartFile mockMultipartFile;

    @BeforeEach
    void setup() {
        menuImageForm = new MenuImageForm();
        mockMultipartFile = mock(MultipartFile.class);
    }

    @Test
    @DisplayName("Constructor default membuat objek kosong")
    void constructor_default_membuat_objek_kosong() {
        // Act
        MenuImageForm form = new MenuImageForm();

        // Assert
        assertNull(form.getId());
        assertNull(form.getImageFile());
    }

    @Test
    @DisplayName("Setter dan Getter untuk ID bekerja dengan benar")
    void setter_dan_getter_untuk_id_bekerja_dengan_benar() {
        // Arrange
        UUID expectedId = UUID.randomUUID();

        // Act
        menuImageForm.setId(expectedId);
        UUID actualId = menuImageForm.getId();

        // Assert
        assertEquals(expectedId, actualId);
    }

    @Test
    @DisplayName("Setter dan Getter untuk imageFile bekerja dengan benar")
    void setter_dan_getter_untuk_imageFile_bekerja_dengan_benar() {
        // Act
        menuImageForm.setImageFile(mockMultipartFile);
        MultipartFile actualFile = menuImageForm.getImageFile();

        // Assert
        assertEquals(mockMultipartFile, actualFile);
    }

    @Test
    @DisplayName("isEmpty return true ketika imageFile null")
    void isEmpty_return_true_ketika_imageFile_null() {
        // Arrange
        menuImageForm.setImageFile(null);

        // Act
        boolean result = menuImageForm.isEmpty();

        // Assert
        assertTrue(result);
    }

    @Test
    @DisplayName("isEmpty return true ketika imageFile empty")
    void isEmpty_return_true_ketika_imageFile_empty() {
        // Arrange
        when(mockMultipartFile.isEmpty()).thenReturn(true);
        menuImageForm.setImageFile(mockMultipartFile);

        // Act
        boolean result = menuImageForm.isEmpty();

        // Assert
        assertTrue(result);
    }

    @Test
    @DisplayName("isEmpty return false ketika imageFile tidak empty")
    void isEmpty_return_false_ketika_imageFile_tidak_empty() {
        // Arrange
        when(mockMultipartFile.isEmpty()).thenReturn(false);
        menuImageForm.setImageFile(mockMultipartFile);

        // Act
        boolean result = menuImageForm.isEmpty();

        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("getOriginalFilename return null ketika imageFile null")
    void getOriginalFilename_return_null_ketika_imageFile_null() {
        // Arrange
        menuImageForm.setImageFile(null);

        // Act
        String result = menuImageForm.getOriginalFilename();

        // Assert
        assertNull(result);
    }

    @Test
    @DisplayName("getOriginalFilename return filename ketika imageFile ada")
    void getOriginalFilename_return_filename_ketika_imageFile_ada() {
        // Arrange
        String expectedFilename = "menu-photo.jpg";
        when(mockMultipartFile.getOriginalFilename()).thenReturn(expectedFilename);
        when(mockMultipartFile.isEmpty()).thenReturn(false);
        menuImageForm.setImageFile(mockMultipartFile);

        // Act
        String result = menuImageForm.getOriginalFilename();

        // Assert
        assertEquals(expectedFilename, result);
    }

    @Test
    @DisplayName("getContentType return null ketika imageFile null")
    void getContentType_return_null_ketika_imageFile_null() {
        // Arrange
        menuImageForm.setImageFile(null);

        // Act
        String result = menuImageForm.getContentType();

        // Assert
        assertNull(result);
    }

    @Test
    @DisplayName("getContentType return content type ketika imageFile ada")
    void getContentType_return_content_type_ketika_imageFile_ada() {
        // Arrange
        String expectedContentType = "image/jpeg";
        when(mockMultipartFile.isEmpty()).thenReturn(false);
        when(mockMultipartFile.getContentType()).thenReturn(expectedContentType);
        menuImageForm.setImageFile(mockMultipartFile);

        // Act
        String result = menuImageForm.getContentType();

        // Assert
        assertEquals(expectedContentType, result);
    }

    @Test
    @DisplayName("getSize return 0 ketika imageFile null")
    void getSize_return_0_ketika_imageFile_null() {
        // Arrange
        menuImageForm.setImageFile(null);

        // Act
        long result = menuImageForm.getSize();

        // Assert
        assertEquals(0, result);
    }

    @Test
    @DisplayName("getSize return file size ketika imageFile ada")
    void getSize_return_file_size_ketika_imageFile_ada() {
        // Arrange
        long expectedSize = 1024 * 500; // 500KB
        when(mockMultipartFile.isEmpty()).thenReturn(false);
        when(mockMultipartFile.getSize()).thenReturn(expectedSize);
        menuImageForm.setImageFile(mockMultipartFile);

        // Act
        long result = menuImageForm.getSize();

        // Assert
        assertEquals(expectedSize, result);
    }

    @Test
    @DisplayName("isValidImage return false ketika imageFile null")
    void isValidImage_return_false_ketika_imageFile_null() {
        // Arrange
        menuImageForm.setImageFile(null);

        // Act
        boolean result = menuImageForm.isValidImage();

        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("isValidImage return false ketika imageFile empty")
    void isValidImage_return_false_ketika_imageFile_empty() {
        // Arrange
        when(mockMultipartFile.isEmpty()).thenReturn(true);
        menuImageForm.setImageFile(mockMultipartFile);

        // Act
        boolean result = menuImageForm.isValidImage();

        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("isValidImage return false ketika contentType null")
    void isValidImage_return_false_ketika_contentType_null() {
        // Arrange
        when(mockMultipartFile.isEmpty()).thenReturn(false);
        when(mockMultipartFile.getContentType()).thenReturn(null);
        menuImageForm.setImageFile(mockMultipartFile);

        // Act
        boolean result = menuImageForm.isValidImage();

        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("isValidImage return true untuk image/jpeg")
    void isValidImage_return_true_untuk_image_jpeg() {
        // Arrange
        when(mockMultipartFile.isEmpty()).thenReturn(false);
        when(mockMultipartFile.getContentType()).thenReturn("image/jpeg");
        menuImageForm.setImageFile(mockMultipartFile);

        // Act
        boolean result = menuImageForm.isValidImage();

        // Assert
        assertTrue(result);
    }

    @Test
    @DisplayName("isValidImage return true untuk image/jpg")
    void isValidImage_return_true_untuk_image_jpg() {
        // Arrange
        when(mockMultipartFile.isEmpty()).thenReturn(false);
        when(mockMultipartFile.getContentType()).thenReturn("image/jpg");
        menuImageForm.setImageFile(mockMultipartFile);

        // Act
        boolean result = menuImageForm.isValidImage();

        // Assert
        assertTrue(result);
    }

    @Test
    @DisplayName("isValidImage return true untuk image/png")
    void isValidImage_return_true_untuk_image_png() {
        // Arrange
        when(mockMultipartFile.isEmpty()).thenReturn(false);
        when(mockMultipartFile.getContentType()).thenReturn("image/png");
        menuImageForm.setImageFile(mockMultipartFile);

        // Act
        boolean result = menuImageForm.isValidImage();

        // Assert
        assertTrue(result);
    }

    @Test
    @DisplayName("isValidImage return true untuk image/gif")
    void isValidImage_return_true_untuk_image_gif() {
        // Arrange
        when(mockMultipartFile.isEmpty()).thenReturn(false);
        when(mockMultipartFile.getContentType()).thenReturn("image/gif");
        menuImageForm.setImageFile(mockMultipartFile);

        // Act
        boolean result = menuImageForm.isValidImage();

        // Assert
        assertTrue(result);
    }

    @Test
    @DisplayName("isValidImage return false untuk content type non-image")
    void isValidImage_return_false_untuk_content_type_non_image() {
        // Arrange
        String[] invalidContentTypes = {
                "text/plain",
                "application/pdf",
                "application/octet-stream",
                "video/mp4",
                "audio/mpeg",
                "image/svg+xml", // SVG tidak didukung
                "image/bmp", // BMP tidak didukung
                "image/webp" // WebP tidak didukung di MenuImageForm
        };

        for (String contentType : invalidContentTypes) {
            when(mockMultipartFile.isEmpty()).thenReturn(false);
            when(mockMultipartFile.getContentType()).thenReturn(contentType);
            menuImageForm.setImageFile(mockMultipartFile);

            // Act
            boolean result = menuImageForm.isValidImage();

            // Assert
            assertFalse(result, "Should return false for content type: " + contentType);
        }
    }

    @Test
    @DisplayName("isSizeValid return false ketika imageFile null")
    void isSizeValid_return_false_ketika_imageFile_null() {
        // Arrange
        menuImageForm.setImageFile(null);
        long maxSize = 1024 * 1024; // 1MB

        // Act
        boolean result = menuImageForm.isSizeValid(maxSize);

        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("isSizeValid return true ketika file size sama dengan maxSize")
    void isSizeValid_return_true_ketika_file_size_sama_dengan_maxSize() {
        // Arrange
        long maxSize = 1024 * 1024; // 1MB
        when(mockMultipartFile.isEmpty()).thenReturn(false);
        when(mockMultipartFile.getSize()).thenReturn(maxSize);
        menuImageForm.setImageFile(mockMultipartFile);

        // Act
        boolean result = menuImageForm.isSizeValid(maxSize);

        // Assert
        assertTrue(result);
    }

    @Test
    @DisplayName("isSizeValid return true ketika file size kurang dari maxSize")
    void isSizeValid_return_true_ketika_file_size_kurang_dari_maxSize() {
        // Arrange
        long maxSize = 1024 * 1024; // 1MB
        long fileSize = 512 * 1024; // 0.5MB
        when(mockMultipartFile.isEmpty()).thenReturn(false);
        when(mockMultipartFile.getSize()).thenReturn(fileSize);
        menuImageForm.setImageFile(mockMultipartFile);

        // Act
        boolean result = menuImageForm.isSizeValid(maxSize);

        // Assert
        assertTrue(result);
    }

    @Test
    @DisplayName("isSizeValid return false ketika file size lebih dari maxSize")
    void isSizeValid_return_false_ketika_file_size_lebih_dari_maxSize() {
        // Arrange
        long maxSize = 1024 * 1024; // 1MB
        long fileSize = 2 * 1024 * 1024; // 2MB
        when(mockMultipartFile.isEmpty()).thenReturn(false);
        when(mockMultipartFile.getSize()).thenReturn(fileSize);
        menuImageForm.setImageFile(mockMultipartFile);

        // Act
        boolean result = menuImageForm.isSizeValid(maxSize);

        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("isSizeValid return true untuk file size 0 dengan maxSize 0")
    void isSizeValid_return_true_untuk_file_size_0_dengan_maxSize_0() {
        // Arrange
        when(mockMultipartFile.isEmpty()).thenReturn(false);
        when(mockMultipartFile.getSize()).thenReturn(0L);
        menuImageForm.setImageFile(mockMultipartFile);

        // Act
        boolean result = menuImageForm.isSizeValid(0L);

        // Assert
        assertTrue(result);
    }

    @Test
    @DisplayName("Integration test - form valid untuk image JPEG ukuran normal")
    void integration_test_form_valid_untuk_image_JPEG_ukuran_normal() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(mockMultipartFile.isEmpty()).thenReturn(false);
        when(mockMultipartFile.getContentType()).thenReturn("image/jpeg");
        when(mockMultipartFile.getSize()).thenReturn(500 * 1024L); // 500KB
        when(mockMultipartFile.getOriginalFilename()).thenReturn("nasi-goreng.jpg");

        menuImageForm.setId(id);
        menuImageForm.setImageFile(mockMultipartFile);

        // Assert semua kondisi
        assertFalse(menuImageForm.isEmpty());
        assertEquals("nasi-goreng.jpg", menuImageForm.getOriginalFilename());
        assertEquals("image/jpeg", menuImageForm.getContentType());
        assertEquals(500 * 1024L, menuImageForm.getSize());
        assertTrue(menuImageForm.isValidImage());
        assertTrue(menuImageForm.isSizeValid(1024 * 1024)); // 1MB max
        assertEquals(id, menuImageForm.getId());
    }

    @Test
    @DisplayName("Integration test - form valid untuk image PNG ukuran normal")
    void integration_test_form_valid_untuk_image_PNG_ukuran_normal() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(mockMultipartFile.isEmpty()).thenReturn(false);
        when(mockMultipartFile.getContentType()).thenReturn("image/png");
        when(mockMultipartFile.getSize()).thenReturn(800 * 1024L); // 800KB
        when(mockMultipartFile.getOriginalFilename()).thenReturn("rendang.png");

        menuImageForm.setId(id);
        menuImageForm.setImageFile(mockMultipartFile);

        // Assert semua kondisi
        assertFalse(menuImageForm.isEmpty());
        assertEquals("rendang.png", menuImageForm.getOriginalFilename());
        assertEquals("image/png", menuImageForm.getContentType());
        assertEquals(800 * 1024L, menuImageForm.getSize());
        assertTrue(menuImageForm.isValidImage());
        assertTrue(menuImageForm.isSizeValid(5 * 1024 * 1024)); // 5MB max
        assertEquals(id, menuImageForm.getId());
    }

    @Test
    @DisplayName("Integration test - form invalid untuk file besar")
    void integration_test_form_invalid_untuk_file_besar() {
        // Arrange
        when(mockMultipartFile.isEmpty()).thenReturn(false);
        when(mockMultipartFile.getContentType()).thenReturn("image/png");
        when(mockMultipartFile.getSize()).thenReturn(10 * 1024 * 1024L); // 10MB
        when(mockMultipartFile.getOriginalFilename()).thenReturn("large-menu-image.png");

        menuImageForm.setImageFile(mockMultipartFile);

        // Assert
        assertFalse(menuImageForm.isEmpty());
        assertTrue(menuImageForm.isValidImage()); // Masih valid sebagai image
        assertFalse(menuImageForm.isSizeValid(5 * 1024 * 1024)); // Tapi size melebihi 5MB
    }

    @Test
    @DisplayName("Integration test - form invalid untuk content type tidak didukung")
    void integration_test_form_invalid_untuk_content_type_tidak_didukung() {
        // Arrange
        when(mockMultipartFile.isEmpty()).thenReturn(false);
        when(mockMultipartFile.getContentType()).thenReturn("application/pdf");
        when(mockMultipartFile.getSize()).thenReturn(100 * 1024L); // 100KB
        when(mockMultipartFile.getOriginalFilename()).thenReturn("menu.pdf");

        menuImageForm.setImageFile(mockMultipartFile);

        // Assert
        assertFalse(menuImageForm.isEmpty());
        assertFalse(menuImageForm.isValidImage()); // Invalid karena bukan image
        assertTrue(menuImageForm.isSizeValid(1 * 1024 * 1024)); // Size OK tapi bukan image
    }

    @Test
    @DisplayName("Edge case - contentType case sensitive")
    void edge_case_contentType_case_sensitive() {
        // Arrange - MenuImageForm menggunakan equals() yang case-sensitive
        String[] caseVariations = {
                "IMAGE/JPEG",
                "Image/Jpeg",
                "image/JPEG",
                "IMAGE/jpeg"
        };

        for (String contentType : caseVariations) {
            when(mockMultipartFile.isEmpty()).thenReturn(false);
            when(mockMultipartFile.getContentType()).thenReturn(contentType);
            menuImageForm.setImageFile(mockMultipartFile);

            // Act
            boolean result = menuImageForm.isValidImage();

            // Assert
            assertFalse(result, "Should return false for case variation: " + contentType);
        }
    }

    @Test
    @DisplayName("Edge case - file dengan size 0 byte")
    void edge_case_file_dengan_size_0_byte() {
        // Arrange
        when(mockMultipartFile.isEmpty()).thenReturn(false);
        when(mockMultipartFile.getContentType()).thenReturn("image/jpeg");
        when(mockMultipartFile.getSize()).thenReturn(0L);
        when(mockMultipartFile.getOriginalFilename()).thenReturn("empty.jpg");

        menuImageForm.setImageFile(mockMultipartFile);

        // Act & Assert
        assertFalse(menuImageForm.isEmpty()); // File object ada
        assertTrue(menuImageForm.isValidImage()); // Content type valid
        assertTrue(menuImageForm.isSizeValid(1024)); // Size valid (0 <= maxSize)
        assertEquals(0L, menuImageForm.getSize());
    }

    @Test
    @DisplayName("Edge case - filename dengan karakter khusus")
    void edge_case_filename_dengan_karakter_khusus() {
        // Arrange
        String specialFilename = "menu-makanan-2024_v1.0 (copy).jpg";
        when(mockMultipartFile.isEmpty()).thenReturn(false);
        when(mockMultipartFile.getOriginalFilename()).thenReturn(specialFilename);

        menuImageForm.setImageFile(mockMultipartFile);

        // Act
        String result = menuImageForm.getOriginalFilename();

        // Assert
        assertEquals(specialFilename, result);
    }

    @Test
    @DisplayName("Boundary test - maxSize tepat di batas")
    void boundary_test_maxSize_tepat_di_batas() {
        // Arrange
        long exactSize = 5 * 1024 * 1024; // Exactly 5MB
        when(mockMultipartFile.isEmpty()).thenReturn(false);
        when(mockMultipartFile.getSize()).thenReturn(exactSize);
        menuImageForm.setImageFile(mockMultipartFile);

        // Act & Assert
        assertTrue(menuImageForm.isSizeValid(exactSize)); // Exactly at limit
        assertTrue(menuImageForm.isSizeValid(exactSize + 1)); // Just above limit in maxSize
        assertFalse(menuImageForm.isSizeValid(exactSize - 1)); // Just below limit in maxSize
    }

    @Test
    @DisplayName("Null safety - semua method handle null dengan aman")
    void null_safety_semua_method_handle_null_dengan_aman() {
        // Arrange
        menuImageForm.setImageFile(null);

        // Act & Assert - tidak throw exception
        assertDoesNotThrow(() -> {
            menuImageForm.isEmpty();
            menuImageForm.isValidImage();
            menuImageForm.isSizeValid(1024);
            menuImageForm.getOriginalFilename();
            menuImageForm.getContentType();
            menuImageForm.getSize();
        });

        // Assert values
        assertTrue(menuImageForm.isEmpty());
        assertFalse(menuImageForm.isValidImage());
        assertFalse(menuImageForm.isSizeValid(1024));
        assertNull(menuImageForm.getOriginalFilename());
        assertNull(menuImageForm.getContentType());
        assertEquals(0, menuImageForm.getSize());
    }
}