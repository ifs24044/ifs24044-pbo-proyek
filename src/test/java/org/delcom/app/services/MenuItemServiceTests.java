package org.delcom.app.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.delcom.app.entities.MenuItem;
import org.delcom.app.repositories.MenuItemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class MenuItemServiceTests {

    private final MenuItemRepository menuItemRepository = Mockito.mock(MenuItemRepository.class);
    private final MenuItemService menuItemService = new MenuItemService(menuItemRepository);

    // ============================
    // CREATE
    // ============================
    @Test
    @DisplayName("createMenuItem - Membuat menu baru")
    void testCreateMenuItem() {
        UUID userId = UUID.randomUUID();

        MenuItem savedItem = new MenuItem();
        savedItem.setId(UUID.randomUUID());

        when(menuItemRepository.save(any(MenuItem.class))).thenReturn(savedItem);

        MenuItem result = menuItemService.createMenuItem(
            userId, "Nasi Goreng", "Main Course", 25000.0,
            "Enak", 10, 1, 300
        );

        assertNotNull(result);
        assertNotNull(result.getId());
        verify(menuItemRepository, times(1)).save(any(MenuItem.class));
    }

    @Test
    @DisplayName("createMenuItem - Dengan description null")
    void testCreateMenuItem_NullDescription() {
        UUID userId = UUID.randomUUID();

        MenuItem savedItem = new MenuItem();
        savedItem.setId(UUID.randomUUID());

        when(menuItemRepository.save(any(MenuItem.class))).thenReturn(savedItem);

        MenuItem result = menuItemService.createMenuItem(
            userId, "Es Teh", "Beverage", 5000.0,
            null, 5, 0, null
        );

        assertNotNull(result);
        verify(menuItemRepository, times(1)).save(any(MenuItem.class));
    }

    // ============================
    // READ – getAllMenuItems
    // ============================
    @Test
    @DisplayName("getAllMenuItems - tanpa search (null)")
    void testGetAllMenuItems_NoSearch() {
        UUID userId = UUID.randomUUID();
        List<MenuItem> mockList = new ArrayList<>();

        when(menuItemRepository.findAllByUserId(userId)).thenReturn(mockList);

        List<MenuItem> result = menuItemService.getAllMenuItems(userId, null);

        assertEquals(mockList, result);
        verify(menuItemRepository, times(1)).findAllByUserId(userId);
        verify(menuItemRepository, never()).findByKeyword(any(), any());
    }

    @Test
    @DisplayName("getAllMenuItems - tanpa search (empty string)")
    void testGetAllMenuItems_EmptySearch() {
        UUID userId = UUID.randomUUID();
        List<MenuItem> mockList = new ArrayList<>();

        when(menuItemRepository.findAllByUserId(userId)).thenReturn(mockList);

        List<MenuItem> result = menuItemService.getAllMenuItems(userId, "");

        assertEquals(mockList, result);
        verify(menuItemRepository, times(1)).findAllByUserId(userId);
        verify(menuItemRepository, never()).findByKeyword(any(), any());
    }

    @Test
    @DisplayName("getAllMenuItems - dengan search")
    void testGetAllMenuItems_WithSearch() {
        UUID userId = UUID.randomUUID();
        String search = "nasi";

        List<MenuItem> mockList = new ArrayList<>();
        when(menuItemRepository.findByKeyword(userId, search)).thenReturn(mockList);

        List<MenuItem> result = menuItemService.getAllMenuItems(userId, search);

        assertEquals(mockList, result);
        verify(menuItemRepository, times(1)).findByKeyword(userId, search);
        verify(menuItemRepository, never()).findAllByUserId(any());
    }

    // ============================
    // READ – getMenuItemById
    // ============================
    @Test
    @DisplayName("getMenuItemById - menu ditemukan")
    void testGetMenuItemById_Found() {
        UUID userId = UUID.randomUUID();
        UUID id = UUID.randomUUID();

        MenuItem item = new MenuItem();
        when(menuItemRepository.findByUserIdAndId(userId, id)).thenReturn(Optional.of(item));

        MenuItem result = menuItemService.getMenuItemById(userId, id);

        assertNotNull(result);
        verify(menuItemRepository, times(1)).findByUserIdAndId(userId, id);
    }

    @Test
    @DisplayName("getMenuItemById - menu tidak ditemukan")
    void testGetMenuItemById_NotFound() {
        UUID userId = UUID.randomUUID();
        UUID id = UUID.randomUUID();

        when(menuItemRepository.findByUserIdAndId(userId, id)).thenReturn(Optional.empty());

        MenuItem result = menuItemService.getMenuItemById(userId, id);

        assertNull(result);
        verify(menuItemRepository, times(1)).findByUserIdAndId(userId, id);
    }

    // ============================
    // READ – getMenuItemsByCategory
    // ============================
    @Test
    @DisplayName("getMenuItemsByCategory - berhasil mengambil data")
    void testGetMenuItemsByCategory() {
        UUID userId = UUID.randomUUID();
        List<MenuItem> mockList = List.of(new MenuItem());

        when(menuItemRepository.findByCategory(userId, "Main Course")).thenReturn(mockList);

        List<MenuItem> result = menuItemService.getMenuItemsByCategory(userId, "Main Course");

        assertEquals(mockList, result);
        verify(menuItemRepository, times(1)).findByCategory(userId, "Main Course");
    }

    @Test
    @DisplayName("getMenuItemsByCategory - empty result")
    void testGetMenuItemsByCategory_Empty() {
        UUID userId = UUID.randomUUID();
        List<MenuItem> mockList = new ArrayList<>();

        when(menuItemRepository.findByCategory(userId, "Dessert")).thenReturn(mockList);

        List<MenuItem> result = menuItemService.getMenuItemsByCategory(userId, "Dessert");

        assertTrue(result.isEmpty());
        verify(menuItemRepository, times(1)).findByCategory(userId, "Dessert");
    }

    // ============================
    // READ – getAvailableMenuItems
    // ============================
    @Test
    @DisplayName("getAvailableMenuItems - berhasil mengambil menu tersedia")
    void testGetAvailableMenuItems() {
        UUID userId = UUID.randomUUID();
        List<MenuItem> mockList = List.of(new MenuItem());

        when(menuItemRepository.findAvailableMenus(userId)).thenReturn(mockList);

        List<MenuItem> result = menuItemService.getAvailableMenuItems(userId);

        assertEquals(mockList, result);
        verify(menuItemRepository, times(1)).findAvailableMenus(userId);
    }

    @Test
    @DisplayName("getAvailableMenuItems - empty result")
    void testGetAvailableMenuItems_Empty() {
        UUID userId = UUID.randomUUID();
        List<MenuItem> mockList = new ArrayList<>();

        when(menuItemRepository.findAvailableMenus(userId)).thenReturn(mockList);

        List<MenuItem> result = menuItemService.getAvailableMenuItems(userId);

        assertTrue(result.isEmpty());
        verify(menuItemRepository, times(1)).findAvailableMenus(userId);
    }

    // ============================
    // UPDATE – updateMenuItem
    // ============================
    @Test
    @DisplayName("updateMenuItem - berhasil update")
    void testUpdateMenuItem_Success() {
        UUID userId = UUID.randomUUID();
        UUID id = UUID.randomUUID();

        MenuItem existing = new MenuItem();
        when(menuItemRepository.findByUserIdAndId(userId, id)).thenReturn(Optional.of(existing));
        when(menuItemRepository.save(existing)).thenReturn(existing);

        MenuItem result = menuItemService.updateMenuItem(
            userId, id,
            "Mie Ayam", "Main Course", 15000.0,
            "Lezat", 5, 1, 500, true
        );

        assertNotNull(result);
        assertEquals("Mie Ayam", existing.getName());
        assertEquals("Main Course", existing.getCategory());
        assertEquals(15000.0, existing.getPrice());
        assertEquals("Lezat", existing.getDescription());
        assertEquals(5, existing.getPreparationTime());
        assertEquals(1, existing.getSpicyLevel());
        assertEquals(500, existing.getCalories());
        assertTrue(existing.getIsAvailable());
        verify(menuItemRepository, times(1)).save(existing);
    }

    @Test
    @DisplayName("updateMenuItem - menu tidak ditemukan")
    void testUpdateMenuItem_NotFound() {
        UUID userId = UUID.randomUUID();
        UUID id = UUID.randomUUID();

        when(menuItemRepository.findByUserIdAndId(userId, id)).thenReturn(Optional.empty());

        MenuItem result = menuItemService.updateMenuItem(
            userId, id, "A", "B", 1.0, "C", 1, 1, 100, true
        );

        assertNull(result);
        verify(menuItemRepository, times(0)).save(any());
    }

    @Test
    @DisplayName("updateMenuItem - update dengan null values")
    void testUpdateMenuItem_WithNullValues() {
        UUID userId = UUID.randomUUID();
        UUID id = UUID.randomUUID();

        MenuItem existing = new MenuItem();
        when(menuItemRepository.findByUserIdAndId(userId, id)).thenReturn(Optional.of(existing));
        when(menuItemRepository.save(existing)).thenReturn(existing);

        MenuItem result = menuItemService.updateMenuItem(
            userId, id,
            "Kopi", "Beverage", 10000.0,
            null, 3, 0, null, false
        );

        assertNotNull(result);
        assertNull(existing.getDescription());
        assertNull(existing.getCalories());
        assertFalse(existing.getIsAvailable());
        verify(menuItemRepository, times(1)).save(existing);
    }

    // ============================
    // UPDATE – updateMenuImage
    // ============================
    @Test
    @DisplayName("updateMenuImage - berhasil update gambar")
    void testUpdateMenuImage_Success() {
        UUID id = UUID.randomUUID();
        MenuItem item = new MenuItem();

        when(menuItemRepository.findById(id)).thenReturn(Optional.of(item));
        when(menuItemRepository.save(item)).thenReturn(item);

        MenuItem result = menuItemService.updateMenuImage(id, "http://example.com/img.jpg");

        assertNotNull(result);
        assertEquals("http://example.com/img.jpg", item.getImageUrl());
        verify(menuItemRepository, times(1)).findById(id);
        verify(menuItemRepository, times(1)).save(item);
    }

    @Test
    @DisplayName("updateMenuImage - menu tidak ditemukan")
    void testUpdateMenuImage_NotFound() {
        UUID id = UUID.randomUUID();

        when(menuItemRepository.findById(id)).thenReturn(Optional.empty());

        MenuItem result = menuItemService.updateMenuImage(id, "url");

        assertNull(result);
        verify(menuItemRepository, times(1)).findById(id);
        verify(menuItemRepository, never()).save(any());
    }

    @Test
    @DisplayName("updateMenuImage - dengan null imageUrl")
    void testUpdateMenuImage_NullUrl() {
        UUID id = UUID.randomUUID();
        MenuItem item = new MenuItem();

        when(menuItemRepository.findById(id)).thenReturn(Optional.of(item));
        when(menuItemRepository.save(item)).thenReturn(item);

        MenuItem result = menuItemService.updateMenuImage(id, null);

        assertNotNull(result);
        assertNull(item.getImageUrl());
        verify(menuItemRepository, times(1)).save(item);
    }

    // ============================
    // DELETE – deleteMenuItem
    // ============================
    @Test
    @DisplayName("deleteMenuItem - berhasil delete")
    void testDeleteMenuItem_Success() {
        UUID userId = UUID.randomUUID();
        UUID id = UUID.randomUUID();

        MenuItem item = new MenuItem();
        when(menuItemRepository.findByUserIdAndId(userId, id)).thenReturn(Optional.of(item));
        doNothing().when(menuItemRepository).delete(item);

        boolean result = menuItemService.deleteMenuItem(userId, id);

        assertTrue(result);
        verify(menuItemRepository, times(1)).findByUserIdAndId(userId, id);
        verify(menuItemRepository, times(1)).delete(item);
    }

    @Test
    @DisplayName("deleteMenuItem - menu tidak ditemukan")
    void testDeleteMenuItem_NotFound() {
        UUID userId = UUID.randomUUID();
        UUID id = UUID.randomUUID();

        when(menuItemRepository.findByUserIdAndId(userId, id)).thenReturn(Optional.empty());

        boolean result = menuItemService.deleteMenuItem(userId, id);

        assertFalse(result);
        verify(menuItemRepository, times(1)).findByUserIdAndId(userId, id);
        verify(menuItemRepository, never()).delete(any());
    }

    // ============================
    // CHART DATA
    // ============================
    @Test
    @DisplayName("getMenuCountByCategory - berhasil mengambil data")
    void testGetMenuCountByCategory() {
        UUID userId = UUID.randomUUID();

        List<Object[]> mockList = Arrays.asList(
            new Object[]{"Main Course", 5L},
            new Object[]{"Beverage", 3L}
        );

        when(menuItemRepository.countByCategory(userId)).thenReturn(mockList);

        List<Object[]> result = menuItemService.getMenuCountByCategory(userId);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(menuItemRepository, times(1)).countByCategory(userId);
    }

    @Test
    @DisplayName("getMenuCountByCategory - empty result")
    void testGetMenuCountByCategory_Empty() {
        UUID userId = UUID.randomUUID();

        List<Object[]> mockList = new ArrayList<>();
        when(menuItemRepository.countByCategory(userId)).thenReturn(mockList);

        List<Object[]> result = menuItemService.getMenuCountByCategory(userId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(menuItemRepository, times(1)).countByCategory(userId);
    }

    @Test
    @DisplayName("getAveragePriceByCategory - berhasil mengambil data")
    void testGetAveragePriceByCategory() {
        UUID userId = UUID.randomUUID();

        List<Object[]> mockList = Arrays.asList(
            new Object[]{"Main Course", 20000.0},
            new Object[]{"Dessert", 15000.0}
        );

        when(menuItemRepository.averagePriceByCategory(userId)).thenReturn(mockList);

        List<Object[]> result = menuItemService.getAveragePriceByCategory(userId);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(menuItemRepository, times(1)).averagePriceByCategory(userId);
    }

    @Test
    @DisplayName("getAveragePriceByCategory - empty result")
    void testGetAveragePriceByCategory_Empty() {
        UUID userId = UUID.randomUUID();

        List<Object[]> mockList = new ArrayList<>();
        when(menuItemRepository.averagePriceByCategory(userId)).thenReturn(mockList);

        List<Object[]> result = menuItemService.getAveragePriceByCategory(userId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(menuItemRepository, times(1)).averagePriceByCategory(userId);
    }

    @Test
    @DisplayName("getTotalPriceByCategory - berhasil mengambil data")
    void testGetTotalPriceByCategory() {
        UUID userId = UUID.randomUUID();

        List<Object[]> mockList = Arrays.asList(
            new Object[]{"Beverage", 45000.0},
            new Object[]{"Snack", 30000.0}
        );

        when(menuItemRepository.totalPriceByCategory(userId)).thenReturn(mockList);

        List<Object[]> result = menuItemService.getTotalPriceByCategory(userId);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(menuItemRepository, times(1)).totalPriceByCategory(userId);
    }

    @Test
    @DisplayName("getTotalPriceByCategory - empty result")
    void testGetTotalPriceByCategory_Empty() {
        UUID userId = UUID.randomUUID();

        List<Object[]> mockList = new ArrayList<>();
        when(menuItemRepository.totalPriceByCategory(userId)).thenReturn(mockList);

        List<Object[]> result = menuItemService.getTotalPriceByCategory(userId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(menuItemRepository, times(1)).totalPriceByCategory(userId);
    }
}