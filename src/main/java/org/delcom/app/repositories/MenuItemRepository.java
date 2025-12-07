package org.delcom.app.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.delcom.app.entities.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, UUID> {
    
    // Mencari menu berdasarkan keyword (nama atau deskripsi)
    @Query("SELECT m FROM MenuItem m WHERE (LOWER(m.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(m.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND m.userId = :userId ORDER BY m.createdAt DESC")
    List<MenuItem> findByKeyword(@Param("userId") UUID userId, @Param("keyword") String keyword);

    // Mendapatkan semua menu berdasarkan userId
    @Query("SELECT m FROM MenuItem m WHERE m.userId = :userId ORDER BY m.createdAt DESC")
    List<MenuItem> findAllByUserId(@Param("userId") UUID userId);

    // Mendapatkan menu berdasarkan id dan userId
    @Query("SELECT m FROM MenuItem m WHERE m.id = :id AND m.userId = :userId")
    Optional<MenuItem> findByUserIdAndId(@Param("userId") UUID userId, @Param("id") UUID id);

    // Mendapatkan menu berdasarkan kategori
    @Query("SELECT m FROM MenuItem m WHERE m.category = :category AND m.userId = :userId ORDER BY m.name ASC")
    List<MenuItem> findByCategory(@Param("userId") UUID userId, @Param("category") String category);

    // Mendapatkan menu yang tersedia saja
    @Query("SELECT m FROM MenuItem m WHERE m.isAvailable = true AND m.userId = :userId ORDER BY m.createdAt DESC")
    List<MenuItem> findAvailableMenus(@Param("userId") UUID userId);

    // Untuk data chart - count by category
    @Query("SELECT m.category, COUNT(m) FROM MenuItem m WHERE m.userId = :userId GROUP BY m.category")
    List<Object[]> countByCategory(@Param("userId") UUID userId);

    // Untuk data chart - average price by category
    @Query("SELECT m.category, AVG(m.price) FROM MenuItem m WHERE m.userId = :userId GROUP BY m.category")
    List<Object[]> averagePriceByCategory(@Param("userId") UUID userId);

    // Untuk data chart - total price by category
    @Query("SELECT m.category, SUM(m.price) FROM MenuItem m WHERE m.userId = :userId GROUP BY m.category")
    List<Object[]> totalPriceByCategory(@Param("userId") UUID userId);
}