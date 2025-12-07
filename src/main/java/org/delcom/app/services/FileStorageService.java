package org.delcom.app.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * Service untuk mengelola penyimpanan file gambar menu
 * Adapted from TodoCover to MenuItem
 */
@Service
public class FileStorageService {
    
    @Value("${app.upload.dir:./uploads}")
    protected String uploadDir; // protected untuk testing

    /**
     * Menyimpan file gambar menu ke server
     * Changed from: cover_[todoId] → menu_[menuId]
     * 
     * @param file - MultipartFile dari form upload
     * @param menuId - UUID dari menu item
     * @return String - nama file (contoh: menu_abc-123.jpg)
     * @throws IOException jika gagal menyimpan
     */
    public String storeFile(MultipartFile file, UUID menuId) throws IOException {
        // 1. Buat directory jika belum ada
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // 2. Ambil ekstensi file
        String originalFilename = file.getOriginalFilename();
        String fileExtension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        // 3. Generate nama file unik untuk menu
        // Format: menu_[UUID].[ext]
        String filename = "menu_" + menuId.toString() + fileExtension;

        // 4. Simpan file (REPLACE_EXISTING untuk update gambar)
        Path filePath = uploadPath.resolve(filename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return filename;
    }

    /**
     * Menghapus file gambar menu
     */
    public boolean deleteFile(String filename) {
        try {
            Path filePath = Paths.get(uploadDir).resolve(filename);
            return Files.deleteIfExists(filePath);
        } catch (IOException e) {
            System.err.println("Error deleting file: " + e.getMessage());
            return false;
        }
    }

    /**
     * Load file untuk ditampilkan
     */
    public Path loadFile(String filename) {
        return Paths.get(uploadDir).resolve(filename);
    }

    /**
     * Cek apakah file exists
     */
    public boolean fileExists(String filename) {
        if (filename == null || filename.isEmpty()) {
            return false;
        }
        return Files.exists(loadFile(filename));
    }

    /**
     * Get upload directory path
     */
    public String getUploadDir() {
        return uploadDir;
    }
}