package org.delcom.app.controllers;

import org.delcom.app.configs.ApiResponse;
import org.delcom.app.configs.AuthContext;
import org.delcom.app.entities.MenuItem;
import org.delcom.app.entities.User;
import org.delcom.app.services.MenuItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MenuItemControllerTests {

    @Mock
    private MenuItemService menuItemService;

    @Mock
    private AuthContext authContext;

    @InjectMocks
    private MenuItemController controller;
    
    private User mockUser;
    private MenuItem mockMenuItem;
    private UUID menuItemId;

    @BeforeEach
    void setUp() {
        // Setup manual injection jika @InjectMocks tidak otomatis menangani field protected
        // Tapi @InjectMocks biasanya cukup. Jika authContext null, kita set manual:
        controller.authContext = authContext;

        mockUser = new User();
        mockUser.setId(UUID.randomUUID());
        mockUser.setName("Test User");
        mockUser.setEmail("test@example.com");

        menuItemId = UUID.randomUUID();
        mockMenuItem = new MenuItem();
        mockMenuItem.setId(menuItemId);
        mockMenuItem.setUserId(mockUser.getId());
        mockMenuItem.setName("Nasi Goreng");
        mockMenuItem.setCategory("Main Course");
        mockMenuItem.setPrice(25000.0);
        mockMenuItem.setDescription("Nasi goreng spesial");
        mockMenuItem.setPreparationTime(15);
        mockMenuItem.setSpicyLevel(2);
        mockMenuItem.setCalories(300);
        mockMenuItem.setIsAvailable(true);
    }

    // ========== CREATE TESTS ==========

    @Test
    @DisplayName("createMenuItem - Success")
    void testCreateMenuItem_Success() {
        // 1. Mock Authentication
        when(authContext.isAuthenticated()).thenReturn(true);
        when(authContext.getAuthUser()).thenReturn(mockUser);

        // 2. Mock Service Call
        when(menuItemService.createMenuItem(
            any(UUID.class), // userId
            eq("Nasi Goreng"), 
            eq("Main Course"), 
            eq(25000.0), 
            eq("Nasi goreng spesial"), 
            eq(15), 
            eq(2), 
            eq(300)
        )).thenReturn(mockMenuItem);

        // 3. Execute
        ResponseEntity<ApiResponse<Map<String, UUID>>> response = controller.createMenuItem(mockMenuItem);

        // 4. Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("success", response.getBody().getStatus());
        assertEquals("Menu berhasil ditambahkan", response.getBody().getMessage());
        assertEquals(menuItemId, response.getBody().getData().get("id"));
    }

    @Test
    @DisplayName("createMenuItem - Name Empty")
    void testCreateMenuItem_NameEmpty() {
        mockMenuItem.setName("");

        ResponseEntity<ApiResponse<Map<String, UUID>>> response = controller.createMenuItem(mockMenuItem);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("fail", response.getBody().getStatus());
        assertEquals("Nama menu tidak boleh kosong", response.getBody().getMessage());
    }

    @Test
    @DisplayName("createMenuItem - Name Null")
    void testCreateMenuItem_NameNull() {
        mockMenuItem.setName(null);

        ResponseEntity<ApiResponse<Map<String, UUID>>> response = controller.createMenuItem(mockMenuItem);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("fail", response.getBody().getStatus());
        assertEquals("Nama menu tidak boleh kosong", response.getBody().getMessage());
    }

    @Test
    @DisplayName("createMenuItem - Category Empty")
    void testCreateMenuItem_CategoryEmpty() {
        mockMenuItem.setCategory("");

        ResponseEntity<ApiResponse<Map<String, UUID>>> response = controller.createMenuItem(mockMenuItem);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("fail", response.getBody().getStatus());
        assertEquals("Kategori tidak boleh kosong", response.getBody().getMessage());
    }

    @Test
    @DisplayName("createMenuItem - Category Null")
    void testCreateMenuItem_CategoryNull() {
        mockMenuItem.setCategory(null);

        ResponseEntity<ApiResponse<Map<String, UUID>>> response = controller.createMenuItem(mockMenuItem);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("fail", response.getBody().getStatus());
        assertEquals("Kategori tidak boleh kosong", response.getBody().getMessage());
    }

    @Test
    @DisplayName("createMenuItem - Price Zero")
    void testCreateMenuItem_PriceZero() {
        mockMenuItem.setPrice(0.0);

        ResponseEntity<ApiResponse<Map<String, UUID>>> response = controller.createMenuItem(mockMenuItem);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("fail", response.getBody().getStatus());
        assertEquals("Harga harus lebih dari 0", response.getBody().getMessage());
    }

    @Test
    @DisplayName("createMenuItem - Price Negative")
    void testCreateMenuItem_PriceNegative() {
        mockMenuItem.setPrice(-100.0);

        ResponseEntity<ApiResponse<Map<String, UUID>>> response = controller.createMenuItem(mockMenuItem);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("fail", response.getBody().getStatus());
        assertEquals("Harga harus lebih dari 0", response.getBody().getMessage());
    }

    @Test
    @DisplayName("createMenuItem - Price Null")
    void testCreateMenuItem_PriceNull() {
        mockMenuItem.setPrice(null);

        ResponseEntity<ApiResponse<Map<String, UUID>>> response = controller.createMenuItem(mockMenuItem);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("fail", response.getBody().getStatus());
        assertEquals("Harga harus lebih dari 0", response.getBody().getMessage());
    }

    @Test
    @DisplayName("createMenuItem - PreparationTime Zero")
    void testCreateMenuItem_PreparationTimeZero() {
        mockMenuItem.setPreparationTime(0);

        ResponseEntity<ApiResponse<Map<String, UUID>>> response = controller.createMenuItem(mockMenuItem);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("fail", response.getBody().getStatus());
        assertEquals("Waktu persiapan harus lebih dari 0", response.getBody().getMessage());
    }

    @Test
    @DisplayName("createMenuItem - PreparationTime Negative")
    void testCreateMenuItem_PreparationTimeNegative() {
        mockMenuItem.setPreparationTime(-5);

        ResponseEntity<ApiResponse<Map<String, UUID>>> response = controller.createMenuItem(mockMenuItem);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("fail", response.getBody().getStatus());
        assertEquals("Waktu persiapan harus lebih dari 0", response.getBody().getMessage());
    }

    @Test
    @DisplayName("createMenuItem - PreparationTime Null")
    void testCreateMenuItem_PreparationTimeNull() {
        mockMenuItem.setPreparationTime(null);

        ResponseEntity<ApiResponse<Map<String, UUID>>> response = controller.createMenuItem(mockMenuItem);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("fail", response.getBody().getStatus());
        assertEquals("Waktu persiapan harus lebih dari 0", response.getBody().getMessage());
    }

    @Test
    @DisplayName("createMenuItem - Unauthenticated")
    void testCreateMenuItem_Unauthenticated() {
        when(authContext.isAuthenticated()).thenReturn(false);

        ResponseEntity<ApiResponse<Map<String, UUID>>> response = controller.createMenuItem(mockMenuItem);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("fail", response.getBody().getStatus());
        assertEquals("User tidak terautentikasi", response.getBody().getMessage());
    }

    // ========== READ ALL TESTS ==========

    @Test
    @DisplayName("getAllMenuItems - Success")
    void testGetAllMenuItems_Success() {
        when(authContext.isAuthenticated()).thenReturn(true);
        when(authContext.getAuthUser()).thenReturn(mockUser);

        List<MenuItem> menuItems = Arrays.asList(mockMenuItem);
        when(menuItemService.getAllMenuItems(any(UUID.class), isNull())).thenReturn(menuItems);

        ResponseEntity<ApiResponse<Map<String, List<MenuItem>>>> response = controller.getAllMenuItems(null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("success", response.getBody().getStatus());
        assertEquals("Daftar menu berhasil diambil", response.getBody().getMessage());
        assertNotNull(response.getBody().getData().get("menuItems"));
        assertEquals(1, response.getBody().getData().get("menuItems").size());
        assertEquals("Nasi Goreng", response.getBody().getData().get("menuItems").get(0).getName());
    }

    @Test
    @DisplayName("getAllMenuItems - With Search")
    void testGetAllMenuItems_WithSearch() {
        when(authContext.isAuthenticated()).thenReturn(true);
        when(authContext.getAuthUser()).thenReturn(mockUser);

        List<MenuItem> menuItems = Arrays.asList(mockMenuItem);
        when(menuItemService.getAllMenuItems(any(UUID.class), eq("Nasi"))).thenReturn(menuItems);

        ResponseEntity<ApiResponse<Map<String, List<MenuItem>>>> response = controller.getAllMenuItems("Nasi");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("success", response.getBody().getStatus());
        assertNotNull(response.getBody().getData().get("menuItems"));
        verify(menuItemService).getAllMenuItems(any(UUID.class), eq("Nasi"));
    }

    @Test
    @DisplayName("getAllMenuItems - Unauthenticated")
    void testGetAllMenuItems_Unauthenticated() {
        when(authContext.isAuthenticated()).thenReturn(false);

        ResponseEntity<ApiResponse<Map<String, List<MenuItem>>>> response = controller.getAllMenuItems(null);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("fail", response.getBody().getStatus());
        assertEquals("User tidak terautentikasi", response.getBody().getMessage());
    }

    // ========== READ BY ID TESTS ==========

    @Test
    @DisplayName("getMenuItemById - Success")
    void testGetMenuItemById_Success() {
        when(authContext.isAuthenticated()).thenReturn(true);
        when(authContext.getAuthUser()).thenReturn(mockUser);
        when(menuItemService.getMenuItemById(any(UUID.class), eq(menuItemId))).thenReturn(mockMenuItem);

        ResponseEntity<ApiResponse<Map<String, MenuItem>>> response = controller.getMenuItemById(menuItemId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("success", response.getBody().getStatus());
        assertEquals("Data menu berhasil diambil", response.getBody().getMessage());
        assertEquals("Nasi Goreng", response.getBody().getData().get("menuItem").getName());
    }

    @Test
    @DisplayName("getMenuItemById - Not Found")
    void testGetMenuItemById_NotFound() {
        when(authContext.isAuthenticated()).thenReturn(true);
        when(authContext.getAuthUser()).thenReturn(mockUser);
        when(menuItemService.getMenuItemById(any(UUID.class), any(UUID.class))).thenReturn(null);

        ResponseEntity<ApiResponse<Map<String, MenuItem>>> response = controller.getMenuItemById(UUID.randomUUID());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("fail", response.getBody().getStatus());
        assertEquals("Menu tidak ditemukan", response.getBody().getMessage());
    }

    @Test
    @DisplayName("getMenuItemById - Unauthenticated")
    void testGetMenuItemById_Unauthenticated() {
        when(authContext.isAuthenticated()).thenReturn(false);

        ResponseEntity<ApiResponse<Map<String, MenuItem>>> response = controller.getMenuItemById(menuItemId);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("fail", response.getBody().getStatus());
        assertEquals("User tidak terautentikasi", response.getBody().getMessage());
    }

    // ========== READ BY CATEGORY TESTS ==========

    @Test
    @DisplayName("getMenuItemsByCategory - Success")
    void testGetMenuItemsByCategory_Success() {
        when(authContext.isAuthenticated()).thenReturn(true);
        when(authContext.getAuthUser()).thenReturn(mockUser);

        List<MenuItem> menuItems = Arrays.asList(mockMenuItem);
        when(menuItemService.getMenuItemsByCategory(any(UUID.class), eq("Main Course")))
                .thenReturn(menuItems);

        ResponseEntity<ApiResponse<Map<String, List<MenuItem>>>> response = 
                controller.getMenuItemsByCategory("Main Course");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("success", response.getBody().getStatus());
        assertEquals("Daftar menu berdasarkan kategori berhasil diambil", response.getBody().getMessage());
        assertNotNull(response.getBody().getData().get("menuItems"));
    }

    @Test
    @DisplayName("getMenuItemsByCategory - Unauthenticated")
    void testGetMenuItemsByCategory_Unauthenticated() {
        when(authContext.isAuthenticated()).thenReturn(false);

        ResponseEntity<ApiResponse<Map<String, List<MenuItem>>>> response = 
                controller.getMenuItemsByCategory("Main Course");

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("fail", response.getBody().getStatus());
        assertEquals("User tidak terautentikasi", response.getBody().getMessage());
    }

    // ========== UPDATE TESTS ==========

    @Test
    @DisplayName("updateMenuItem - Success")
    void testUpdateMenuItem_Success() {
        // 1. Setup
        when(authContext.isAuthenticated()).thenReturn(true);
        when(authContext.getAuthUser()).thenReturn(mockUser);

        // 2. Mock Service to return updated item
        when(menuItemService.updateMenuItem(
                any(UUID.class),
                eq(menuItemId),
                anyString(), // name
                anyString(), // category
                anyDouble(), // price
                anyString(), // description
                anyInt(),    // prep time
                anyInt(),    // spicy level
                anyInt(),    // calories
                anyBoolean() // available
        )).thenReturn(mockMenuItem);

        // 3. Execute
        ResponseEntity<ApiResponse<String>> response = controller.updateMenuItem(menuItemId, mockMenuItem);

        // 4. Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("success", response.getBody().getStatus());
        assertEquals("Menu berhasil diperbarui", response.getBody().getMessage());
    }

    @Test
    @DisplayName("updateMenuItem - Name Empty")
    void testUpdateMenuItem_NameEmpty() {
        mockMenuItem.setName("");

        ResponseEntity<ApiResponse<String>> response = controller.updateMenuItem(menuItemId, mockMenuItem);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("fail", response.getBody().getStatus());
        assertEquals("Nama menu tidak boleh kosong", response.getBody().getMessage());
    }

    @Test
    @DisplayName("updateMenuItem - Name Null")
    void testUpdateMenuItem_NameNull() {
        mockMenuItem.setName(null);

        ResponseEntity<ApiResponse<String>> response = controller.updateMenuItem(menuItemId, mockMenuItem);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("fail", response.getBody().getStatus());
        assertEquals("Nama menu tidak boleh kosong", response.getBody().getMessage());
    }

    @Test
    @DisplayName("updateMenuItem - Category Empty")
    void testUpdateMenuItem_CategoryEmpty() {
        mockMenuItem.setCategory("");

        ResponseEntity<ApiResponse<String>> response = controller.updateMenuItem(menuItemId, mockMenuItem);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("fail", response.getBody().getStatus());
        assertEquals("Kategori tidak boleh kosong", response.getBody().getMessage());
    }

    @Test
    @DisplayName("updateMenuItem - Category Null")
    void testUpdateMenuItem_CategoryNull() {
        mockMenuItem.setCategory(null);

        ResponseEntity<ApiResponse<String>> response = controller.updateMenuItem(menuItemId, mockMenuItem);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("fail", response.getBody().getStatus());
        assertEquals("Kategori tidak boleh kosong", response.getBody().getMessage());
    }

    @Test
    @DisplayName("updateMenuItem - Price Zero")
    void testUpdateMenuItem_PriceZero() {
        mockMenuItem.setPrice(0.0);

        ResponseEntity<ApiResponse<String>> response = controller.updateMenuItem(menuItemId, mockMenuItem);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("fail", response.getBody().getStatus());
        assertEquals("Harga harus lebih dari 0", response.getBody().getMessage());
    }

    @Test
    @DisplayName("updateMenuItem - Price Negative")
    void testUpdateMenuItem_PriceNegative() {
        mockMenuItem.setPrice(-50.0);

        ResponseEntity<ApiResponse<String>> response = controller.updateMenuItem(menuItemId, mockMenuItem);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("fail", response.getBody().getStatus());
        assertEquals("Harga harus lebih dari 0", response.getBody().getMessage());
    }

    @Test
    @DisplayName("updateMenuItem - Price Null")
    void testUpdateMenuItem_PriceNull() {
        mockMenuItem.setPrice(null);

        ResponseEntity<ApiResponse<String>> response = controller.updateMenuItem(menuItemId, mockMenuItem);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("fail", response.getBody().getStatus());
        assertEquals("Harga harus lebih dari 0", response.getBody().getMessage());
    }

    @Test
    @DisplayName("updateMenuItem - Not Found")
    void testUpdateMenuItem_NotFound() {
        when(authContext.isAuthenticated()).thenReturn(true);
        when(authContext.getAuthUser()).thenReturn(mockUser);
        
        // Mock service returning null (not found)
        when(menuItemService.updateMenuItem(
                any(UUID.class), any(UUID.class), anyString(), anyString(), anyDouble(),
                anyString(), anyInt(), anyInt(), anyInt(), anyBoolean()
        )).thenReturn(null);

        ResponseEntity<ApiResponse<String>> response = controller.updateMenuItem(UUID.randomUUID(), mockMenuItem);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("fail", response.getBody().getStatus());
        assertEquals("Menu tidak ditemukan", response.getBody().getMessage());
    }

    @Test
    @DisplayName("updateMenuItem - Unauthenticated")
    void testUpdateMenuItem_Unauthenticated() {
        when(authContext.isAuthenticated()).thenReturn(false);

        ResponseEntity<ApiResponse<String>> response = controller.updateMenuItem(menuItemId, mockMenuItem);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("fail", response.getBody().getStatus());
        assertEquals("User tidak terautentikasi", response.getBody().getMessage());
    }

    // ========== DELETE TESTS ==========

    @Test
    @DisplayName("deleteMenuItem - Success")
    void testDeleteMenuItem_Success() {
        when(authContext.isAuthenticated()).thenReturn(true);
        when(authContext.getAuthUser()).thenReturn(mockUser);
        when(menuItemService.deleteMenuItem(any(UUID.class), eq(menuItemId))).thenReturn(true);

        ResponseEntity<ApiResponse<String>> response = controller.deleteMenuItem(menuItemId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("success", response.getBody().getStatus());
        assertEquals("Menu berhasil dihapus", response.getBody().getMessage());
    }

    @Test
    @DisplayName("deleteMenuItem - Not Found")
    void testDeleteMenuItem_NotFound() {
        when(authContext.isAuthenticated()).thenReturn(true);
        when(authContext.getAuthUser()).thenReturn(mockUser);
        when(menuItemService.deleteMenuItem(any(UUID.class), any(UUID.class))).thenReturn(false);

        ResponseEntity<ApiResponse<String>> response = controller.deleteMenuItem(UUID.randomUUID());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("fail", response.getBody().getStatus());
        assertEquals("Menu tidak ditemukan", response.getBody().getMessage());
    }

    @Test
    @DisplayName("deleteMenuItem - Unauthenticated")
    void testDeleteMenuItem_Unauthenticated() {
        when(authContext.isAuthenticated()).thenReturn(false);

        ResponseEntity<ApiResponse<String>> response = controller.deleteMenuItem(menuItemId);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("fail", response.getBody().getStatus());
        assertEquals("User tidak terautentikasi", response.getBody().getMessage());
    }

    // ========== CHART TESTS ==========

    @Test
    @DisplayName("getMenuCountByCategory - Success")
    void testGetMenuCountByCategory_Success() {
        when(authContext.isAuthenticated()).thenReturn(true);
        when(authContext.getAuthUser()).thenReturn(mockUser);

        List<Object[]> chartData = Arrays.asList(
                new Object[]{"Main Course", 10L},
                new Object[]{"Dessert", 5L}
        );
        when(menuItemService.getMenuCountByCategory(any(UUID.class))).thenReturn(chartData);

        ResponseEntity<ApiResponse<Map<String, List<Object[]>>>> response = 
                controller.getMenuCountByCategory();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("success", response.getBody().getStatus());
        assertEquals("Data chart berhasil diambil", response.getBody().getMessage());
        assertNotNull(response.getBody().getData().get("data"));
        assertEquals(2, response.getBody().getData().get("data").size());
    }

    @Test
    @DisplayName("getMenuCountByCategory - Unauthenticated")
    void testGetMenuCountByCategory_Unauthenticated() {
        when(authContext.isAuthenticated()).thenReturn(false);

        ResponseEntity<ApiResponse<Map<String, List<Object[]>>>> response = 
                controller.getMenuCountByCategory();

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("fail", response.getBody().getStatus());
        assertEquals("User tidak terautentikasi", response.getBody().getMessage());
    }

    @Test
    @DisplayName("getAveragePriceByCategory - Success")
    void testGetAveragePriceByCategory_Success() {
        when(authContext.isAuthenticated()).thenReturn(true);
        when(authContext.getAuthUser()).thenReturn(mockUser);

        List<Object[]> chartData = Arrays.asList(
                new Object[]{"Main Course", 25000.0},
                new Object[]{"Dessert", 15000.0}
        );
        when(menuItemService.getAveragePriceByCategory(any(UUID.class))).thenReturn(chartData);

        ResponseEntity<ApiResponse<Map<String, List<Object[]>>>> response = 
                controller.getAveragePriceByCategory();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("success", response.getBody().getStatus());
        assertEquals("Data chart berhasil diambil", response.getBody().getMessage());
        assertNotNull(response.getBody().getData().get("data"));
    }

    @Test
    @DisplayName("getAveragePriceByCategory - Unauthenticated")
    void testGetAveragePriceByCategory_Unauthenticated() {
        when(authContext.isAuthenticated()).thenReturn(false);

        ResponseEntity<ApiResponse<Map<String, List<Object[]>>>> response = 
                controller.getAveragePriceByCategory();

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("fail", response.getBody().getStatus());
        assertEquals("User tidak terautentikasi", response.getBody().getMessage());
    }
}