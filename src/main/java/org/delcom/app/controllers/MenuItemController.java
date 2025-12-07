package org.delcom.app.controllers;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.delcom.app.configs.ApiResponse;
import org.delcom.app.configs.AuthContext;
import org.delcom.app.entities.MenuItem;
import org.delcom.app.entities.User;
import org.delcom.app.services.MenuItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/menu-items")
public class MenuItemController {
    
    private final MenuItemService menuItemService;

    @Autowired
    protected AuthContext authContext;

    public MenuItemController(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }

    // CREATE - Menambahkan menu baru
    @PostMapping
    public ResponseEntity<ApiResponse<Map<String, UUID>>> createMenuItem(@RequestBody MenuItem reqMenuItem) {
        
        // Validasi input
        if (reqMenuItem.getName() == null || reqMenuItem.getName().isEmpty()) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>("fail", "Nama menu tidak boleh kosong", null));
        }
        if (reqMenuItem.getCategory() == null || reqMenuItem.getCategory().isEmpty()) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>("fail", "Kategori tidak boleh kosong", null));
        }
        if (reqMenuItem.getPrice() == null || reqMenuItem.getPrice() <= 0) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>("fail", "Harga harus lebih dari 0", null));
        }
        if (reqMenuItem.getPreparationTime() == null || reqMenuItem.getPreparationTime() <= 0) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>("fail", "Waktu persiapan harus lebih dari 0", null));
        }
        // Validasi autentikasi
        if (!authContext.isAuthenticated()) {
            return ResponseEntity.status(403)
                .body(new ApiResponse<>("fail", "User tidak terautentikasi", null));
        }
        User authUser = authContext.getAuthUser();

        MenuItem newMenuItem = menuItemService.createMenuItem(
            authUser.getId(),
            reqMenuItem.getName(),
            reqMenuItem.getCategory(),
            reqMenuItem.getPrice(),
            reqMenuItem.getDescription(),
            reqMenuItem.getPreparationTime(),
            reqMenuItem.getSpicyLevel(),
            reqMenuItem.getCalories()
        );

        return ResponseEntity.ok(new ApiResponse<>(
            "success",
            "Menu berhasil ditambahkan",
            Map.of("id", newMenuItem.getId())
        ));
    }

    // READ - Mendapatkan semua menu dengan opsi pencarian
    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, List<MenuItem>>>> getAllMenuItems(
            @RequestParam(required = false) String search) {
        
        if (!authContext.isAuthenticated()) {
            return ResponseEntity.status(403)
                .body(new ApiResponse<>("fail", "User tidak terautentikasi", null));
        }
        User authUser = authContext.getAuthUser();

        List<MenuItem> menuItems = menuItemService.getAllMenuItems(authUser.getId(), search);
        return ResponseEntity.ok(new ApiResponse<>(
            "success",
            "Daftar menu berhasil diambil",
            Map.of("menuItems", menuItems)
        ));
    }

    // READ - Mendapatkan menu berdasarkan ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Map<String, MenuItem>>> getMenuItemById(@PathVariable UUID id) {
        
        if (!authContext.isAuthenticated()) {
            return ResponseEntity.status(403)
                .body(new ApiResponse<>("fail", "User tidak terautentikasi", null));
        }
        User authUser = authContext.getAuthUser();

        MenuItem menuItem = menuItemService.getMenuItemById(authUser.getId(), id);
        if (menuItem == null) {
            return ResponseEntity.status(404)
                .body(new ApiResponse<>("fail", "Menu tidak ditemukan", null));
        }

        return ResponseEntity.ok(new ApiResponse<>(
            "success",
            "Data menu berhasil diambil",
            Map.of("menuItem", menuItem)
        ));
    }

    // READ - Mendapatkan menu berdasarkan kategori
    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse<Map<String, List<MenuItem>>>> getMenuItemsByCategory(
            @PathVariable String category) {
        
        if (!authContext.isAuthenticated()) {
            return ResponseEntity.status(403)
                .body(new ApiResponse<>("fail", "User tidak terautentikasi", null));
        }
        User authUser = authContext.getAuthUser();

        List<MenuItem> menuItems = menuItemService.getMenuItemsByCategory(authUser.getId(), category);
        return ResponseEntity.ok(new ApiResponse<>(
            "success",
            "Daftar menu berdasarkan kategori berhasil diambil",
            Map.of("menuItems", menuItems)
        ));
    }

    // UPDATE - Memperbarui menu
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> updateMenuItem(
            @PathVariable UUID id, 
            @RequestBody MenuItem reqMenuItem) {
        
        // Validasi input
        if (reqMenuItem.getName() == null || reqMenuItem.getName().isEmpty()) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>("fail", "Nama menu tidak boleh kosong", null));
        }
        if (reqMenuItem.getCategory() == null || reqMenuItem.getCategory().isEmpty()) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>("fail", "Kategori tidak boleh kosong", null));
        }
        if (reqMenuItem.getPrice() == null || reqMenuItem.getPrice() <= 0) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>("fail", "Harga harus lebih dari 0", null));
        }

        if (!authContext.isAuthenticated()) {
            return ResponseEntity.status(403)
                .body(new ApiResponse<>("fail", "User tidak terautentikasi", null));
        }
        User authUser = authContext.getAuthUser();

        MenuItem updatedMenuItem = menuItemService.updateMenuItem(
            authUser.getId(),
            id,
            reqMenuItem.getName(),
            reqMenuItem.getCategory(),
            reqMenuItem.getPrice(),
            reqMenuItem.getDescription(),
            reqMenuItem.getPreparationTime(),
            reqMenuItem.getSpicyLevel(),
            reqMenuItem.getCalories(),
            reqMenuItem.getIsAvailable()
        );

        if (updatedMenuItem == null) {
            return ResponseEntity.status(404)
                .body(new ApiResponse<>("fail", "Menu tidak ditemukan", null));
        }

        return ResponseEntity.ok(new ApiResponse<>(
            "success",
            "Menu berhasil diperbarui",
            null
        ));
    }

    // DELETE - Menghapus menu
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteMenuItem(@PathVariable UUID id) {
        
        if (!authContext.isAuthenticated()) {
            return ResponseEntity.status(403)
                .body(new ApiResponse<>("fail", "User tidak terautentikasi", null));
        }
        User authUser = authContext.getAuthUser();

        boolean status = menuItemService.deleteMenuItem(authUser.getId(), id);
        if (!status) {
            return ResponseEntity.status(404)
                .body(new ApiResponse<>("fail", "Menu tidak ditemukan", null));
        }

        return ResponseEntity.ok(new ApiResponse<>(
            "success",
            "Menu berhasil dihapus",
            null
        ));
    }

    // CHART - Data jumlah menu per kategori
    @GetMapping("/chart/count-by-category")
    public ResponseEntity<ApiResponse<Map<String, List<Object[]>>>> getMenuCountByCategory() {
        
        if (!authContext.isAuthenticated()) {
            return ResponseEntity.status(403)
                .body(new ApiResponse<>("fail", "User tidak terautentikasi", null));
        }
        User authUser = authContext.getAuthUser();

        List<Object[]> data = menuItemService.getMenuCountByCategory(authUser.getId());
        return ResponseEntity.ok(new ApiResponse<>(
            "success",
            "Data chart berhasil diambil",
            Map.of("data", data)
        ));
    }

    // CHART - Data rata-rata harga per kategori
    @GetMapping("/chart/average-price-by-category")
    public ResponseEntity<ApiResponse<Map<String, List<Object[]>>>> getAveragePriceByCategory() {
        
        if (!authContext.isAuthenticated()) {
            return ResponseEntity.status(403)
                .body(new ApiResponse<>("fail", "User tidak terautentikasi", null));
        }
        User authUser = authContext.getAuthUser();

        List<Object[]> data = menuItemService.getAveragePriceByCategory(authUser.getId());
        return ResponseEntity.ok(new ApiResponse<>(
            "success",
            "Data chart berhasil diambil",
            Map.of("data", data)
        ));
    }
}