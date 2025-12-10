package org.delcom.app.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class MenuItemFormTests {

    private MenuItemForm menuItemForm;

    @BeforeEach
    void setup() {
        menuItemForm = new MenuItemForm();
    }

    // ============================================
    // CONSTRUCTOR TESTS
    // ============================================

    @Test
    @DisplayName("Constructor default membuat objek kosong")
    void constructor_default_membuat_objek_kosong() {
        // Act
        MenuItemForm form = new MenuItemForm();

        // Assert
        assertNull(form.getId());
        assertNull(form.getName());
        assertNull(form.getCategory());
        assertNull(form.getPrice());
        assertNull(form.getDescription());
        assertTrue(form.getIsAvailable()); // Default true
        assertNull(form.getPreparationTime());
        assertNull(form.getSpicyLevel());
        assertNull(form.getCalories());
        assertNull(form.getConfirmName());
    }

    // ============================================
    // GETTER & SETTER TESTS - ID
    // ============================================

    @Test
    @DisplayName("Setter dan Getter untuk ID bekerja dengan benar")
    void setter_dan_getter_untuk_id_bekerja_dengan_benar() {
        // Arrange
        UUID expectedId = UUID.randomUUID();

        // Act
        menuItemForm.setId(expectedId);
        UUID actualId = menuItemForm.getId();

        // Assert
        assertEquals(expectedId, actualId);
    }

    @Test
    @DisplayName("ID dapat di-set ke null")
    void id_dapat_di_set_ke_null() {
        // Arrange
        menuItemForm.setId(UUID.randomUUID());

        // Act
        menuItemForm.setId(null);

        // Assert
        assertNull(menuItemForm.getId());
    }

    // ============================================
    // GETTER & SETTER TESTS - NAME
    // ============================================

    @Test
    @DisplayName("Setter dan Getter untuk Name bekerja dengan benar")
    void setter_dan_getter_untuk_name_bekerja_dengan_benar() {
        // Arrange
        String expectedName = "Nasi Goreng Spesial";

        // Act
        menuItemForm.setName(expectedName);
        String actualName = menuItemForm.getName();

        // Assert
        assertEquals(expectedName, actualName);
    }

    @Test
    @DisplayName("Name dapat di-set ke null")
    void name_dapat_di_set_ke_null() {
        // Arrange
        menuItemForm.setName("Test Menu");

        // Act
        menuItemForm.setName(null);

        // Assert
        assertNull(menuItemForm.getName());
    }

    @Test
    @DisplayName("Name dapat berupa string kosong")
    void name_dapat_berupa_string_kosong() {
        // Act
        menuItemForm.setName("");

        // Assert
        assertEquals("", menuItemForm.getName());
    }

    // ============================================
    // GETTER & SETTER TESTS - CATEGORY
    // ============================================

    @Test
    @DisplayName("Setter dan Getter untuk Category bekerja dengan benar")
    void setter_dan_getter_untuk_category_bekerja_dengan_benar() {
        // Arrange
        String expectedCategory = "Main Course";

        // Act
        menuItemForm.setCategory(expectedCategory);
        String actualCategory = menuItemForm.getCategory();

        // Assert
        assertEquals(expectedCategory, actualCategory);
    }

    @Test
    @DisplayName("Category dapat di-set dengan berbagai nilai valid")
    void category_dapat_di_set_dengan_berbagai_nilai_valid() {
        // Arrange
        String[] validCategories = {"Appetizer", "Main Course", "Dessert", "Beverage"};

        for (String category : validCategories) {
            // Act
            menuItemForm.setCategory(category);

            // Assert
            assertEquals(category, menuItemForm.getCategory());
        }
    }

    // ============================================
    // GETTER & SETTER TESTS - PRICE
    // ============================================

    @Test
    @DisplayName("Setter dan Getter untuk Price bekerja dengan benar")
    void setter_dan_getter_untuk_price_bekerja_dengan_benar() {
        // Arrange
        Double expectedPrice = 25000.0;

        // Act
        menuItemForm.setPrice(expectedPrice);
        Double actualPrice = menuItemForm.getPrice();

        // Assert
        assertEquals(expectedPrice, actualPrice);
    }

    @Test
    @DisplayName("Price dapat di-set ke 0")
    void price_dapat_di_set_ke_0() {
        // Act
        menuItemForm.setPrice(0.0);

        // Assert
        assertEquals(0.0, menuItemForm.getPrice());
    }

    @Test
    @DisplayName("Price dapat menyimpan nilai desimal")
    void price_dapat_menyimpan_nilai_desimal() {
        // Arrange
        Double expectedPrice = 15500.50;

        // Act
        menuItemForm.setPrice(expectedPrice);

        // Assert
        assertEquals(expectedPrice, menuItemForm.getPrice());
    }

    @Test
    @DisplayName("Price dapat di-set ke nilai besar")
    void price_dapat_di_set_ke_nilai_besar() {
        // Arrange
        Double expectedPrice = 999999.99;

        // Act
        menuItemForm.setPrice(expectedPrice);

        // Assert
        assertEquals(expectedPrice, menuItemForm.getPrice());
    }

    // ============================================
    // GETTER & SETTER TESTS - DESCRIPTION
    // ============================================

    @Test
    @DisplayName("Setter dan Getter untuk Description bekerja dengan benar")
    void setter_dan_getter_untuk_description_bekerja_dengan_benar() {
        // Arrange
        String expectedDescription = "Nasi goreng dengan bumbu spesial dan telur mata sapi";

        // Act
        menuItemForm.setDescription(expectedDescription);
        String actualDescription = menuItemForm.getDescription();

        // Assert
        assertEquals(expectedDescription, actualDescription);
    }

    @Test
    @DisplayName("Description dapat berupa string panjang")
    void description_dapat_berupa_string_panjang() {
        // Arrange
        String longDescription = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. ".repeat(10);

        // Act
        menuItemForm.setDescription(longDescription);

        // Assert
        assertEquals(longDescription, menuItemForm.getDescription());
    }

    // ============================================
    // GETTER & SETTER TESTS - IS_AVAILABLE
    // ============================================

    @Test
    @DisplayName("Setter dan Getter untuk IsAvailable bekerja dengan benar")
    void setter_dan_getter_untuk_isAvailable_bekerja_dengan_benar() {
        // Act
        menuItemForm.setIsAvailable(false);

        // Assert
        assertFalse(menuItemForm.getIsAvailable());
    }

    @Test
    @DisplayName("IsAvailable default value adalah true")
    void isAvailable_default_value_adalah_true() {
        // Act
        MenuItemForm form = new MenuItemForm();

        // Assert
        assertTrue(form.getIsAvailable());
    }

    @Test
    @DisplayName("IsAvailable dapat di-toggle")
    void isAvailable_dapat_di_toggle() {
        // Act & Assert
        menuItemForm.setIsAvailable(true);
        assertTrue(menuItemForm.getIsAvailable());

        menuItemForm.setIsAvailable(false);
        assertFalse(menuItemForm.getIsAvailable());

        menuItemForm.setIsAvailable(true);
        assertTrue(menuItemForm.getIsAvailable());
    }

    // ============================================
    // GETTER & SETTER TESTS - PREPARATION_TIME
    // ============================================

    @Test
    @DisplayName("Setter dan Getter untuk PreparationTime bekerja dengan benar")
    void setter_dan_getter_untuk_preparationTime_bekerja_dengan_benar() {
        // Arrange
        Integer expectedTime = 15;

        // Act
        menuItemForm.setPreparationTime(expectedTime);
        Integer actualTime = menuItemForm.getPreparationTime();

        // Assert
        assertEquals(expectedTime, actualTime);
    }

    @Test
    @DisplayName("PreparationTime dapat di-set ke 0")
    void preparationTime_dapat_di_set_ke_0() {
        // Act
        menuItemForm.setPreparationTime(0);

        // Assert
        assertEquals(0, menuItemForm.getPreparationTime());
    }

    @Test
    @DisplayName("PreparationTime dapat di-set ke nilai besar")
    void preparationTime_dapat_di_set_ke_nilai_besar() {
        // Arrange
        Integer expectedTime = 180;

        // Act
        menuItemForm.setPreparationTime(expectedTime);

        // Assert
        assertEquals(expectedTime, menuItemForm.getPreparationTime());
    }

    // ============================================
    // GETTER & SETTER TESTS - SPICY_LEVEL
    // ============================================

    @Test
    @DisplayName("Setter dan Getter untuk SpicyLevel bekerja dengan benar")
    void setter_dan_getter_untuk_spicyLevel_bekerja_dengan_benar() {
        // Arrange
        Integer expectedLevel = 3;

        // Act
        menuItemForm.setSpicyLevel(expectedLevel);
        Integer actualLevel = menuItemForm.getSpicyLevel();

        // Assert
        assertEquals(expectedLevel, actualLevel);
    }

    @Test
    @DisplayName("SpicyLevel dapat di-set ke 0 (tidak pedas)")
    void spicyLevel_dapat_di_set_ke_0() {
        // Act
        menuItemForm.setSpicyLevel(0);

        // Assert
        assertEquals(0, menuItemForm.getSpicyLevel());
    }

    @Test
    @DisplayName("SpicyLevel dapat di-set dengan berbagai level")
    void spicyLevel_dapat_di_set_dengan_berbagai_level() {
        // Arrange
        Integer[] levels = {0, 1, 2, 3, 4, 5};

        for (Integer level : levels) {
            // Act
            menuItemForm.setSpicyLevel(level);

            // Assert
            assertEquals(level, menuItemForm.getSpicyLevel());
        }
    }

    // ============================================
    // GETTER & SETTER TESTS - CALORIES
    // ============================================

    @Test
    @DisplayName("Setter dan Getter untuk Calories bekerja dengan benar")
    void setter_dan_getter_untuk_calories_bekerja_dengan_benar() {
        // Arrange
        Integer expectedCalories = 450;

        // Act
        menuItemForm.setCalories(expectedCalories);
        Integer actualCalories = menuItemForm.getCalories();

        // Assert
        assertEquals(expectedCalories, actualCalories);
    }

    @Test
    @DisplayName("Calories dapat di-set ke 0")
    void calories_dapat_di_set_ke_0() {
        // Act
        menuItemForm.setCalories(0);

        // Assert
        assertEquals(0, menuItemForm.getCalories());
    }

    @Test
    @DisplayName("Calories dapat di-set ke nilai besar")
    void calories_dapat_di_set_ke_nilai_besar() {
        // Arrange
        Integer expectedCalories = 2000;

        // Act
        menuItemForm.setCalories(expectedCalories);

        // Assert
        assertEquals(expectedCalories, menuItemForm.getCalories());
    }

    // ============================================
    // GETTER & SETTER TESTS - CONFIRM_NAME
    // ============================================

    @Test
    @DisplayName("Setter dan Getter untuk ConfirmName bekerja dengan benar")
    void setter_dan_getter_untuk_confirmName_bekerja_dengan_benar() {
        // Arrange
        String expectedConfirmName = "Nasi Goreng Spesial";

        // Act
        menuItemForm.setConfirmName(expectedConfirmName);
        String actualConfirmName = menuItemForm.getConfirmName();

        // Assert
        assertEquals(expectedConfirmName, actualConfirmName);
    }

    @Test
    @DisplayName("ConfirmName dapat di-set ke null")
    void confirmName_dapat_di_set_ke_null() {
        // Arrange
        menuItemForm.setConfirmName("Test");

        // Act
        menuItemForm.setConfirmName(null);

        // Assert
        assertNull(menuItemForm.getConfirmName());
    }

    // ============================================
    // INTEGRATION TESTS
    // ============================================

    @Test
    @DisplayName("Integration test - form lengkap untuk menu baru")
    void integration_test_form_lengkap_untuk_menu_baru() {
        // Arrange
        UUID id = UUID.randomUUID();
        String name = "Nasi Goreng Spesial";
        String category = "Main Course";
        Double price = 25000.0;
        String description = "Nasi goreng dengan bumbu spesial";
        Boolean isAvailable = true;
        Integer preparationTime = 15;
        Integer spicyLevel = 2;
        Integer calories = 450;

        // Act
        menuItemForm.setId(id);
        menuItemForm.setName(name);
        menuItemForm.setCategory(category);
        menuItemForm.setPrice(price);
        menuItemForm.setDescription(description);
        menuItemForm.setIsAvailable(isAvailable);
        menuItemForm.setPreparationTime(preparationTime);
        menuItemForm.setSpicyLevel(spicyLevel);
        menuItemForm.setCalories(calories);

        // Assert
        assertEquals(id, menuItemForm.getId());
        assertEquals(name, menuItemForm.getName());
        assertEquals(category, menuItemForm.getCategory());
        assertEquals(price, menuItemForm.getPrice());
        assertEquals(description, menuItemForm.getDescription());
        assertEquals(isAvailable, menuItemForm.getIsAvailable());
        assertEquals(preparationTime, menuItemForm.getPreparationTime());
        assertEquals(spicyLevel, menuItemForm.getSpicyLevel());
        assertEquals(calories, menuItemForm.getCalories());
    }

    @Test
    @DisplayName("Integration test - form untuk menu Appetizer")
    void integration_test_form_untuk_menu_appetizer() {
        // Arrange & Act
        menuItemForm.setName("Spring Roll");
        menuItemForm.setCategory("Appetizer");
        menuItemForm.setPrice(15000.0);
        menuItemForm.setDescription("Lumpia goreng dengan isian sayuran");
        menuItemForm.setPreparationTime(10);
        menuItemForm.setCalories(200);
        menuItemForm.setSpicyLevel(1);

        // Assert
        assertEquals("Spring Roll", menuItemForm.getName());
        assertEquals("Appetizer", menuItemForm.getCategory());
        assertEquals(15000.0, menuItemForm.getPrice());
        assertTrue(menuItemForm.getIsAvailable()); // Default
    }

    @Test
    @DisplayName("Integration test - form untuk menu Dessert")
    void integration_test_form_untuk_menu_dessert() {
        // Arrange & Act
        menuItemForm.setName("Es Krim Cokelat");
        menuItemForm.setCategory("Dessert");
        menuItemForm.setPrice(12000.0);
        menuItemForm.setDescription("Es krim cokelat premium");
        menuItemForm.setPreparationTime(5);
        menuItemForm.setCalories(300);
        menuItemForm.setSpicyLevel(0); // Tidak pedas

        // Assert
        assertEquals("Es Krim Cokelat", menuItemForm.getName());
        assertEquals("Dessert", menuItemForm.getCategory());
        assertEquals(0, menuItemForm.getSpicyLevel());
    }

    @Test
    @DisplayName("Integration test - form untuk menu Beverage")
    void integration_test_form_untuk_menu_beverage() {
        // Arrange & Act
        menuItemForm.setName("Jus Jeruk");
        menuItemForm.setCategory("Beverage");
        menuItemForm.setPrice(10000.0);
        menuItemForm.setDescription("Jus jeruk segar");
        menuItemForm.setPreparationTime(5);
        menuItemForm.setCalories(100);
        menuItemForm.setIsAvailable(true);

        // Assert
        assertEquals("Jus Jeruk", menuItemForm.getName());
        assertEquals("Beverage", menuItemForm.getCategory());
        assertTrue(menuItemForm.getIsAvailable());
    }

    @Test
    @DisplayName("Integration test - form untuk menu yang habis")
    void integration_test_form_untuk_menu_yang_habis() {
        // Arrange & Act
        menuItemForm.setName("Steak Premium");
        menuItemForm.setCategory("Main Course");
        menuItemForm.setPrice(85000.0);
        menuItemForm.setIsAvailable(false); // Habis
        menuItemForm.setPreparationTime(30);

        // Assert
        assertEquals("Steak Premium", menuItemForm.getName());
        assertFalse(menuItemForm.getIsAvailable());
    }

    @Test
    @DisplayName("Integration test - form untuk konfirmasi hapus")
    void integration_test_form_untuk_konfirmasi_hapus() {
        // Arrange
        UUID id = UUID.randomUUID();
        String menuName = "Nasi Goreng Spesial";

        // Act
        menuItemForm.setId(id);
        menuItemForm.setName(menuName);
        menuItemForm.setConfirmName(menuName);

        // Assert
        assertEquals(id, menuItemForm.getId());
        assertEquals(menuName, menuItemForm.getName());
        assertEquals(menuName, menuItemForm.getConfirmName());
    }

    // ============================================
    // EDGE CASES
    // ============================================

    @Test
    @DisplayName("Edge case - semua field opsional null kecuali isAvailable")
    void edge_case_semua_field_opsional_null_kecuali_isAvailable() {
        // Act
        MenuItemForm form = new MenuItemForm();
        form.setName("Basic Menu");
        form.setCategory("Main Course");
        form.setPrice(10000.0);

        // Assert - Field opsional boleh null
        assertNull(form.getDescription());
        assertNull(form.getPreparationTime());
        assertNull(form.getSpicyLevel());
        assertNull(form.getCalories());
        assertNull(form.getConfirmName());
        
        // isAvailable tetap ada default value
        assertTrue(form.getIsAvailable());
    }

    @Test
    @DisplayName("Edge case - price dengan nilai negatif")
    void edge_case_price_dengan_nilai_negatif() {
        // Act
        menuItemForm.setPrice(-1000.0);

        // Assert
        assertEquals(-1000.0, menuItemForm.getPrice());
    }

    @Test
    @DisplayName("Edge case - preparationTime negatif")
    void edge_case_preparationTime_negatif() {
        // Act
        menuItemForm.setPreparationTime(-5);

        // Assert
        assertEquals(-5, menuItemForm.getPreparationTime());
    }

    @Test
    @DisplayName("Edge case - spicyLevel negatif")
    void edge_case_spicyLevel_negatif() {
        // Act
        menuItemForm.setSpicyLevel(-1);

        // Assert
        assertEquals(-1, menuItemForm.getSpicyLevel());
    }

    @Test
    @DisplayName("Edge case - calories negatif")
    void edge_case_calories_negatif() {
        // Act
        menuItemForm.setCalories(-100);

        // Assert
        assertEquals(-100, menuItemForm.getCalories());
    }

    @Test
    @DisplayName("Edge case - name dengan karakter khusus")
    void edge_case_name_dengan_karakter_khusus() {
        // Arrange
        String specialName = "Nasi Goreng \"Spesial\" & Pedas!!! (Extra Hot)";

        // Act
        menuItemForm.setName(specialName);

        // Assert
        assertEquals(specialName, menuItemForm.getName());
    }

    @Test
    @DisplayName("Edge case - description dengan line breaks")
    void edge_case_description_dengan_line_breaks() {
        // Arrange
        String descWithBreaks = "Line 1\nLine 2\nLine 3";

        // Act
        menuItemForm.setDescription(descWithBreaks);

        // Assert
        assertEquals(descWithBreaks, menuItemForm.getDescription());
    }

    @Test
    @DisplayName("Edge case - category dengan spasi")
    void edge_case_category_dengan_spasi() {
        // Arrange
        String categoryWithSpaces = "  Main Course  ";

        // Act
        menuItemForm.setCategory(categoryWithSpaces);

        // Assert
        assertEquals(categoryWithSpaces, menuItemForm.getCategory());
    }

    @Test
    @DisplayName("Edge case - confirmName berbeda dengan name")
    void edge_case_confirmName_berbeda_dengan_name() {
        // Act
        menuItemForm.setName("Original Name");
        menuItemForm.setConfirmName("Different Name");

        // Assert
        assertNotEquals(menuItemForm.getName(), menuItemForm.getConfirmName());
    }

    @Test
    @DisplayName("Edge case - price dengan banyak desimal")
    void edge_case_price_dengan_banyak_desimal() {
        // Arrange
        Double priceWithDecimals = 15000.123456789;

        // Act
        menuItemForm.setPrice(priceWithDecimals);

        // Assert
        assertEquals(priceWithDecimals, menuItemForm.getPrice());
    }
}