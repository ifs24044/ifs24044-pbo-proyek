package org.delcom.app.services;

import java.util.List;
import java.util.UUID;

import org.delcom.app.entities.MenuItem;
import org.delcom.app.repositories.MenuItemRepository;
import org.springframework.stereotype.Service;

@Service
public class MenuItemService {

    private final MenuItemRepository menuItemRepository;

    public MenuItemService(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    // CREATE - Menambahkan menu baru
    public MenuItem createMenuItem(UUID userId, String name, String category, Double price,
                                   String description, Integer preparationTime, Integer spicyLevel, Integer calories) {
        MenuItem menuItem = new MenuItem();
        menuItem.setUserId(userId);
        menuItem.setName(name);
        menuItem.setCategory(category);
        menuItem.setPrice(price);
        menuItem.setDescription(description);
        menuItem.setPreparationTime(preparationTime);
        menuItem.setSpicyLevel(spicyLevel);
        menuItem.setCalories(calories);
        menuItem.setIsAvailable(true);

        return menuItemRepository.save(menuItem);
    }

    // READ - Mendapatkan semua menu dengan opsi pencarian
    public List<MenuItem> getAllMenuItems(UUID userId, String search) {
        if (search == null || search.isEmpty()) {
            return menuItemRepository.findAllByUserId(userId);
        }
        return menuItemRepository.findByKeyword(userId, search);
    }

    // READ - Mendapatkan menu berdasarkan ID
    public MenuItem getMenuItemById(UUID userId, UUID id) {
        return menuItemRepository.findByUserIdAndId(userId, id).orElse(null);
    }

    // READ - Mendapatkan menu berdasarkan kategori
    public List<MenuItem> getMenuItemsByCategory(UUID userId, String category) {
        return menuItemRepository.findByCategory(userId, category);
    }

    // READ - Mendapatkan menu yang tersedia
    public List<MenuItem> getAvailableMenuItems(UUID userId) {
        return menuItemRepository.findAvailableMenus(userId);
    }

    // UPDATE - Memperbarui menu
    public MenuItem updateMenuItem(UUID userId, UUID id, String name, String category, Double price,
                                   String description, Integer preparationTime, Integer spicyLevel, 
                                   Integer calories, Boolean isAvailable) {
        MenuItem existingMenuItem = menuItemRepository.findByUserIdAndId(userId, id).orElse(null);
        
        if (existingMenuItem == null) {
            return null;
        }

        existingMenuItem.setName(name);
        existingMenuItem.setCategory(category);
        existingMenuItem.setPrice(price);
        existingMenuItem.setDescription(description);
        existingMenuItem.setPreparationTime(preparationTime);
        existingMenuItem.setSpicyLevel(spicyLevel);
        existingMenuItem.setCalories(calories);
        existingMenuItem.setIsAvailable(isAvailable);

        return menuItemRepository.save(existingMenuItem);
    }

    // UPDATE - Memperbarui gambar menu
    public MenuItem updateMenuImage(UUID id, String imageUrl) {
        MenuItem existingMenuItem = menuItemRepository.findById(id).orElse(null);
        
        if (existingMenuItem == null) {
            return null;
        }

        existingMenuItem.setImageUrl(imageUrl);
        return menuItemRepository.save(existingMenuItem);
    }

    // DELETE - Menghapus menu
    public boolean deleteMenuItem(UUID userId, UUID id) {
        MenuItem existingMenuItem = menuItemRepository.findByUserIdAndId(userId, id).orElse(null);
        
        if (existingMenuItem == null) {
            return false;
        }

        menuItemRepository.delete(existingMenuItem);
        return true;
    }

    // CHART DATA - Menghitung jumlah menu per kategori
    public List<Object[]> getMenuCountByCategory(UUID userId) {
        return menuItemRepository.countByCategory(userId);
    }

    // CHART DATA - Menghitung rata-rata harga per kategori
    public List<Object[]> getAveragePriceByCategory(UUID userId) {
        return menuItemRepository.averagePriceByCategory(userId);
    }

    // CHART DATA - Menghitung total harga per kategori
    public List<Object[]> getTotalPriceByCategory(UUID userId) {
        return menuItemRepository.totalPriceByCategory(userId);
    }
}