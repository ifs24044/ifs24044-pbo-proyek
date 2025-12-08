package org.delcom.app.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MenuItemTests {

    @Test
    @DisplayName("Membuat instance dari kelas MenuItem")
    void testMembuatInstanceMenuItem() throws Exception {

        // ====== Constructor utama ======
        {
            UUID userId = UUID.randomUUID();

            MenuItem item = new MenuItem(
                userId,
                "Nasi Goreng",
                "Main Course",
                25000.0,
                "Nasi goreng spesial dengan telur",
                10,
                2
            );

            assertEquals(userId, item.getUserId());
            assertEquals("Nasi Goreng", item.getName());
            assertEquals("Main Course", item.getCategory());
            assertEquals(25000.0, item.getPrice());
            assertEquals("Nasi goreng spesial dengan telur", item.getDescription());
            assertEquals(10, item.getPreparationTime());
            assertEquals(2, item.getSpicyLevel());
            assertEquals(true, item.getIsAvailable());
        }

        // ====== Default constructor ======
        {
            MenuItem item = new MenuItem();

            assertEquals(null, item.getId());
            assertEquals(null, item.getUserId());
            assertEquals(null, item.getName());
            assertEquals(null, item.getCategory());
            assertEquals(null, item.getPrice());
            assertEquals(null, item.getDescription());
            assertEquals(null, item.getPreparationTime());
            assertEquals(null, item.getSpicyLevel());
            assertEquals(true, item.getIsAvailable()); // default value
        }

        // ====== Setter & lifecycle ======
        {
            MenuItem item = new MenuItem();

            UUID generatedId = UUID.randomUUID();
            UUID generatedUserId = UUID.randomUUID();

            item.setId(generatedId);
            item.setUserId(generatedUserId);
            item.setName("Mie Ayam");
            item.setCategory("Main Course");
            item.setPrice(15000.0);
            item.setDescription("Mie ayam dengan topping ayam kecap");
            item.setImageUrl("http://example.com/mieayam.jpg");
            item.setIsAvailable(false);
            item.setPreparationTime(7);
            item.setSpicyLevel(1);
            item.setCalories(450);

            // Jalankan PrePersist & PreUpdate
            item.onCreate();
            item.onUpdate();

            assertEquals(generatedId, item.getId());
            assertEquals(generatedUserId, item.getUserId());
            assertEquals("Mie Ayam", item.getName());
            assertEquals("Main Course", item.getCategory());
            assertEquals(15000.0, item.getPrice());
            assertEquals("Mie ayam dengan topping ayam kecap", item.getDescription());
            assertEquals("http://example.com/mieayam.jpg", item.getImageUrl());
            assertEquals(false, item.getIsAvailable());
            assertEquals(7, item.getPreparationTime());
            assertEquals(1, item.getSpicyLevel());
            assertEquals(450, item.getCalories());

            // Pastikan createdAt dan updatedAt terisi
            assertTrue(item.getCreatedAt() != null);
            assertTrue(item.getUpdatedAt() != null);
        }
    }
}
