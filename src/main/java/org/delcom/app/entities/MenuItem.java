package org.delcom.app.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "menu_items")
public class MenuItem {

    // ===== ATRIBUT WAJIB (4) =====
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "uuid")
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // ===== ATRIBUT TAMBAHAN (minimal 4, total jadi 8+) =====
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "category", nullable = false, length = 50)
    private String category; // Appetizer, Main Course, Dessert, Beverage

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "image_url", length = 255)
    private String imageUrl;

    @Column(name = "is_available", nullable = false, columnDefinition = "boolean default true")
    private Boolean isAvailable = true;

    @Column(name = "preparation_time", nullable = false)
    private Integer preparationTime; // dalam menit

    @Column(name = "spicy_level", nullable = false)
    private Integer spicyLevel; // 0-5 (0 = tidak pedas, 5 = sangat pedas)

    @Column(name = "calories", nullable = true)
    private Integer calories; // kalori (opsional)

    // ===== CONSTRUCTOR =====
    public MenuItem() {
    }

    public MenuItem(UUID userId, String name, String category, Double price, 
                    String description, Integer preparationTime, Integer spicyLevel) {
        this.userId = userId;
        this.name = name;
        this.category = category;
        this.price = price;
        this.description = description;
        this.preparationTime = preparationTime;
        this.spicyLevel = spicyLevel;
        this.isAvailable = true;
    }

    // ===== GETTER & SETTER =====
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public Integer getPreparationTime() {
        return preparationTime;
    }

    public void setPreparationTime(Integer preparationTime) {
        this.preparationTime = preparationTime;
    }

    public Integer getSpicyLevel() {
        return spicyLevel;
    }

    public void setSpicyLevel(Integer spicyLevel) {
        this.spicyLevel = spicyLevel;
    }

    public Integer getCalories() {
        return calories;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // ===== LIFECYCLE CALLBACKS =====
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}