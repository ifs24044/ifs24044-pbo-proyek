package org.delcom.app.views;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

import org.delcom.app.dto.MenuImageForm;
import org.delcom.app.dto.MenuItemForm;
import org.delcom.app.entities.MenuItem;
import org.delcom.app.entities.User;
import org.delcom.app.services.FileStorageService;
import org.delcom.app.services.MenuItemService;
import org.delcom.app.utils.ConstUtil;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/menu-items")
public class MenuItemView {

    private final MenuItemService menuItemService;
    private final FileStorageService fileStorageService;

    public MenuItemView(MenuItemService menuItemService, FileStorageService fileStorageService) {
        this.menuItemService = menuItemService;
        this.fileStorageService = fileStorageService;
    }

    // ============================================
    // CREATE - Tambah Menu Baru
    // ============================================
    @PostMapping("/add")
    public String postAddMenuItem(@Valid @ModelAttribute("menuItemForm") MenuItemForm menuItemForm,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            @RequestParam(value = "isAvailable", required = false, defaultValue = "false") Boolean isAvailable,
            RedirectAttributes redirectAttributes,
            HttpSession session,
            Model model) {

        // Autentikasi user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if ((authentication instanceof AnonymousAuthenticationToken)) {
            return "redirect:/auth/logout";
        }
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof User)) {
            return "redirect:/auth/logout";
        }
        User authUser = (User) principal;

        System.out.println("=== ADD MENU DEBUG ===");
        System.out.println("isAvailable from form: " + isAvailable);
        System.out.println("calories from form: " + menuItemForm.getCalories());

        // Validasi form
        if (menuItemForm.getName() == null || menuItemForm.getName().isBlank()) {
            redirectAttributes.addFlashAttribute("error", "Nama menu tidak boleh kosong");
            redirectAttributes.addFlashAttribute("addMenuModalOpen", true);
            return "redirect:/";
        }

        if (menuItemForm.getCategory() == null || menuItemForm.getCategory().isBlank()) {
            redirectAttributes.addFlashAttribute("error", "Kategori tidak boleh kosong");
            redirectAttributes.addFlashAttribute("addMenuModalOpen", true);
            return "redirect:/";
        }

        if (menuItemForm.getPrice() == null || menuItemForm.getPrice() <= 0) {
            redirectAttributes.addFlashAttribute("error", "Harga harus lebih dari 0");
            redirectAttributes.addFlashAttribute("addMenuModalOpen", true);
            return "redirect:/";
        }

        if (menuItemForm.getPreparationTime() == null || menuItemForm.getPreparationTime() <= 0) {
            redirectAttributes.addFlashAttribute("error", "Waktu persiapan harus lebih dari 0");
            redirectAttributes.addFlashAttribute("addMenuModalOpen", true);
            return "redirect:/";
        }

        // Simpan menu item
        try {
                
            var entity = menuItemService.createMenuItem(
                    authUser.getId(),
                    menuItemForm.getName(),
                    menuItemForm.getCategory(),
                    menuItemForm.getPrice(),
                    menuItemForm.getDescription(),
                    menuItemForm.getPreparationTime(),
                    0, // Default spicyLevel = 0 (tidak pedas)
                    menuItemForm.getCalories()); // ✅ GUNAKAN KALORI DARI FORM

            if (entity == null) {
                redirectAttributes.addFlashAttribute("error", "Gagal menambahkan menu");
                redirectAttributes.addFlashAttribute("addMenuModalOpen", true);
                return "redirect:/";
            }

            // Update status ketersediaan
            entity.setIsAvailable(isAvailable);
            menuItemService.updateMenuItem(
                authUser.getId(),
                entity.getId(),
                entity.getName(),
                entity.getCategory(),
                entity.getPrice(),
                entity.getDescription(),
                entity.getPreparationTime(),
                entity.getSpicyLevel(),
                entity.getCalories(),
                isAvailable);

            System.out.println("✅ Menu saved with isAvailable: " + isAvailable + ", calories: " + entity.getCalories());

            // Handle upload gambar jika ada
            if (imageFile != null && !imageFile.isEmpty()) {
                try {
                    // Validasi file type
                    String contentType = imageFile.getContentType();
                    if (contentType == null || 
                        (!contentType.equals("image/jpeg") && 
                        !contentType.equals("image/png") && 
                        !contentType.equals("image/gif") &&
                        !contentType.equals("image/jpg"))) {
                        redirectAttributes.addFlashAttribute("error", "Menu berhasil ditambahkan, tapi format gambar tidak didukung");
                        return "redirect:/";
                    }

                    // Validasi file size (max 5MB)
                    if (imageFile.getSize() > 5 * 1024 * 1024) {
                        redirectAttributes.addFlashAttribute("error", "Menu berhasil ditambahkan, tapi ukuran gambar terlalu besar (max 5MB)");
                        return "redirect:/";
                    }

                    // Simpan file
                    String fileName = fileStorageService.storeFile(imageFile, entity.getId());
                    
                    // Update menu dengan gambar
                    menuItemService.updateMenuImage(entity.getId(), fileName);
                    
                    redirectAttributes.addFlashAttribute("success", "Menu dan gambar berhasil ditambahkan!");
                } catch (IOException e) {
                    System.err.println("Error uploading image: " + e.getMessage());
                    redirectAttributes.addFlashAttribute("success", "Menu berhasil ditambahkan, tapi gagal upload gambar");
                }
            } else {
                redirectAttributes.addFlashAttribute("success", "Menu berhasil ditambahkan.");
            }

            return "redirect:/";
        } catch (Exception e) {
            System.err.println("Error adding menu: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Terjadi kesalahan: " + e.getMessage());
            redirectAttributes.addFlashAttribute("addMenuModalOpen", true);
            return "redirect:/";
        }
    }

    // ============================================
    // UPDATE - Edit Menu
    // ============================================
    @PostMapping("/edit")
    public String postEditMenuItem(@Valid @ModelAttribute("menuItemForm") MenuItemForm menuItemForm,
            RedirectAttributes redirectAttributes,
            HttpSession session,
            Model model) {
        
        // Autentikasi user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if ((authentication instanceof AnonymousAuthenticationToken)) {
            return "redirect:/auth/logout";
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof User)) {
            return "redirect:/auth/logout";
        }

        User authUser = (User) principal;

        // Validasi form
        if (menuItemForm.getId() == null) {
            redirectAttributes.addFlashAttribute("error", "ID menu tidak valid");
            redirectAttributes.addFlashAttribute("editMenuModalOpen", true);
            return "redirect:/";
        }

        if (menuItemForm.getName() == null || menuItemForm.getName().isBlank()) {
            redirectAttributes.addFlashAttribute("error", "Nama menu tidak boleh kosong");
            redirectAttributes.addFlashAttribute("editMenuModalOpen", true);
            redirectAttributes.addFlashAttribute("editMenuModalId", menuItemForm.getId());
            return "redirect:/";
        }

        if (menuItemForm.getCategory() == null || menuItemForm.getCategory().isBlank()) {
            redirectAttributes.addFlashAttribute("error", "Kategori tidak boleh kosong");
            redirectAttributes.addFlashAttribute("editMenuModalOpen", true);
            redirectAttributes.addFlashAttribute("editMenuModalId", menuItemForm.getId());
            return "redirect:/";
        }

        if (menuItemForm.getPrice() == null || menuItemForm.getPrice() <= 0) {
            redirectAttributes.addFlashAttribute("error", "Harga harus lebih dari 0");
            redirectAttributes.addFlashAttribute("editMenuModalOpen", true);
            redirectAttributes.addFlashAttribute("editMenuModalId", menuItemForm.getId());
            return "redirect:/";
        }

        // Update menu item
        try {
            var updated = menuItemService.updateMenuItem(
                    authUser.getId(),
                    menuItemForm.getId(),
                    menuItemForm.getName(),
                    menuItemForm.getCategory(),
                    menuItemForm.getPrice(),
                    menuItemForm.getDescription(),
                    menuItemForm.getPreparationTime(),
                    menuItemForm.getSpicyLevel(),
                    menuItemForm.getCalories(),
                    menuItemForm.getIsAvailable());
            
            if (updated == null) {
                redirectAttributes.addFlashAttribute("error", "Gagal memperbarui menu");
                redirectAttributes.addFlashAttribute("editMenuModalOpen", true);
                redirectAttributes.addFlashAttribute("editMenuModalId", menuItemForm.getId());
                return "redirect:/";
            }

            // Redirect dengan pesan sukses
            redirectAttributes.addFlashAttribute("success", "Menu berhasil diperbarui.");
            return "redirect:/";
        } catch (Exception e) {
            System.err.println("Error updating menu: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Terjadi kesalahan: " + e.getMessage());
            redirectAttributes.addFlashAttribute("editMenuModalOpen", true);
            redirectAttributes.addFlashAttribute("editMenuModalId", menuItemForm.getId());
            return "redirect:/";
        }
    }

    // ============================================
    // DELETE - Hapus Menu
    // ============================================
    @PostMapping("/delete")
    public String postDeleteMenuItem(@Valid @ModelAttribute("menuItemForm") MenuItemForm menuItemForm,
            RedirectAttributes redirectAttributes,
            HttpSession session,
            Model model) {

        // Autentikasi user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if ((authentication instanceof AnonymousAuthenticationToken)) {
            return "redirect:/auth/logout";
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof User)) {
            return "redirect:/auth/logout";
        }

        User authUser = (User) principal;

        // Validasi form
        if (menuItemForm.getId() == null) {
            redirectAttributes.addFlashAttribute("error", "ID menu tidak valid");
            redirectAttributes.addFlashAttribute("deleteMenuModalOpen", true);
            return "redirect:/";
        }

        if (menuItemForm.getConfirmName() == null || menuItemForm.getConfirmName().isBlank()) {
            redirectAttributes.addFlashAttribute("error", "Konfirmasi nama tidak boleh kosong");
            redirectAttributes.addFlashAttribute("deleteMenuModalOpen", true);
            redirectAttributes.addFlashAttribute("deleteMenuModalId", menuItemForm.getId());
            return "redirect:/";
        }

        // Periksa apakah menu tersedia
        MenuItem existingMenuItem = menuItemService.getMenuItemById(authUser.getId(), menuItemForm.getId());
        if (existingMenuItem == null) {
            redirectAttributes.addFlashAttribute("error", "Menu tidak ditemukan");
            redirectAttributes.addFlashAttribute("deleteMenuModalOpen", true);
            redirectAttributes.addFlashAttribute("deleteMenuModalId", menuItemForm.getId());
            return "redirect:/";
        }

        if (!existingMenuItem.getName().equals(menuItemForm.getConfirmName())) {
            redirectAttributes.addFlashAttribute("error", "Konfirmasi nama tidak sesuai");
            redirectAttributes.addFlashAttribute("deleteMenuModalOpen", true);
            redirectAttributes.addFlashAttribute("deleteMenuModalId", menuItemForm.getId());
            return "redirect:/";
        }

        // Hapus menu item
        try {
            boolean deleted = menuItemService.deleteMenuItem(
                    authUser.getId(),
                    menuItemForm.getId());
            
            if (!deleted) {
                redirectAttributes.addFlashAttribute("error", "Gagal menghapus menu");
                redirectAttributes.addFlashAttribute("deleteMenuModalOpen", true);
                redirectAttributes.addFlashAttribute("deleteMenuModalId", menuItemForm.getId());
                return "redirect:/";
            }

            // Redirect dengan pesan sukses
            redirectAttributes.addFlashAttribute("success", "Menu berhasil dihapus.");
            return "redirect:/";
        } catch (Exception e) {
            System.err.println("Error deleting menu: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Terjadi kesalahan: " + e.getMessage());
            redirectAttributes.addFlashAttribute("deleteMenuModalOpen", true);
            redirectAttributes.addFlashAttribute("deleteMenuModalId", menuItemForm.getId());
            return "redirect:/";
        }
    }

    // ============================================
    // READ - Detail Menu
    // ============================================
    @GetMapping("/{menuItemId}")
    public String getDetailMenuItem(@PathVariable UUID menuItemId, 
                                    Model model,
                                    RedirectAttributes redirectAttributes) {
        try {
            System.out.println("=== GET DETAIL MENU ===");
            System.out.println("Menu ID: " + menuItemId);
            
            // Autentikasi user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if ((authentication instanceof AnonymousAuthenticationToken)) {
                System.out.println("User not authenticated - Anonymous");
                return "redirect:/auth/logout";
            }
            Object principal = authentication.getPrincipal();
            if (!(principal instanceof User)) {
                System.out.println("Principal is not User instance");
                return "redirect:/auth/logout";
            }
            User authUser = (User) principal;
            System.out.println("Auth User: " + authUser.getName());
            model.addAttribute("auth", authUser);

            // Ambil menu item
            MenuItem menuItem = menuItemService.getMenuItemById(authUser.getId(), menuItemId);
            System.out.println("MenuItem result: " + (menuItem != null ? menuItem.getName() : "NULL"));
            
            if (menuItem == null) {
                System.out.println("Menu item not found, redirecting to home");
                redirectAttributes.addFlashAttribute("error", "Menu tidak ditemukan");
                return "redirect:/";
            }
            
            model.addAttribute("menuItem", menuItem);

            // Menu Image Form
            MenuImageForm menuImageForm = new MenuImageForm();
            menuImageForm.setId(menuItemId);
            model.addAttribute("menuImageForm", menuImageForm);

            System.out.println("Returning template: " + ConstUtil.TEMPLATE_MODELS_MENU_ITEMS_DETAIL);
            return ConstUtil.TEMPLATE_MODELS_MENU_ITEMS_DETAIL;
            
        } catch (Exception e) {
            System.err.println("=== ERROR IN GET DETAIL MENU ===");
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Terjadi kesalahan saat memuat detail menu");
            return "redirect:/";
        }
    }

    // ============================================
    // UPDATE - Upload Gambar Menu
    // ============================================
    @PostMapping("/edit-image")
    public String postEditMenuImage(@Valid @ModelAttribute("menuImageForm") MenuImageForm menuImageForm,
            RedirectAttributes redirectAttributes,
            HttpSession session,
            Model model) {

        // Autentikasi user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if ((authentication instanceof AnonymousAuthenticationToken)) {
            return "redirect:/auth/logout";
        }
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof User)) {
            return "redirect:/auth/logout";
        }
        User authUser = (User) principal;

        if (menuImageForm.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "File gambar tidak boleh kosong");
            redirectAttributes.addFlashAttribute("editMenuImageModalOpen", true);
            return "redirect:/menu-items/" + menuImageForm.getId();
        }

        // Check if menu exists
        MenuItem menuItem = menuItemService.getMenuItemById(authUser.getId(), menuImageForm.getId());
        if (menuItem == null) {
            redirectAttributes.addFlashAttribute("error", "Menu tidak ditemukan");
            redirectAttributes.addFlashAttribute("editMenuImageModalOpen", true);
            return "redirect:/";
        }

        // Validasi manual file type
        if (!menuImageForm.isValidImage()) {
            redirectAttributes.addFlashAttribute("error", "Format file tidak didukung. Gunakan JPG, PNG, atau GIF");
            redirectAttributes.addFlashAttribute("editMenuImageModalOpen", true);
            return "redirect:/menu-items/" + menuImageForm.getId();
        }

        // Validasi file size (max 5MB)
        if (!menuImageForm.isSizeValid(5 * 1024 * 1024)) {
            redirectAttributes.addFlashAttribute("error", "Ukuran file terlalu besar. Maksimal 5MB");
            redirectAttributes.addFlashAttribute("editMenuImageModalOpen", true);
            return "redirect:/menu-items/" + menuImageForm.getId();
        }

        try {
            // Simpan file
            String fileName = fileStorageService.storeFile(menuImageForm.getImageFile(), menuImageForm.getId());

            // Update menu item dengan nama file gambar
            menuItemService.updateMenuImage(menuImageForm.getId(), fileName);

            redirectAttributes.addFlashAttribute("success", "Gambar menu berhasil diupload");
            return "redirect:/menu-items/" + menuImageForm.getId();
        } catch (IOException e) {
            System.err.println("Error uploading image: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Gagal mengupload gambar: " + e.getMessage());
            redirectAttributes.addFlashAttribute("editMenuImageModalOpen", true);
            return "redirect:/menu-items/" + menuImageForm.getId();
        }
    }

    // ============================================
    // GET - Gambar Menu
    // ============================================
    @GetMapping("/image/{filename:.+}")
    @ResponseBody
    public Resource getMenuImageByFilename(@PathVariable String filename) {
        try {
            Path file = fileStorageService.loadFile(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                System.err.println("Image not found or not readable: " + filename);
                return null;
            }
        } catch (Exception e) {
            System.err.println("Error loading image: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // ============================================
    // CHART - Statistik Menu
    // ============================================
    @GetMapping("/chart")
    public String getChartPage(Model model) {
        try {
            // Autentikasi user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if ((authentication instanceof AnonymousAuthenticationToken)) {
                return "redirect:/auth/logout";
            }
            Object principal = authentication.getPrincipal();
            if (!(principal instanceof User)) {
                return "redirect:/auth/logout";
            }
            User authUser = (User) principal;
            model.addAttribute("auth", authUser);

            // Ambil data untuk chart
            List<Object[]> categoryCountRaw = menuItemService.getMenuCountByCategory(authUser.getId());
            List<Object[]> averagePriceRaw = menuItemService.getAveragePriceByCategory(authUser.getId());
            List<Object[]> totalPriceRaw = menuItemService.getTotalPriceByCategory(authUser.getId());

            // Transform data untuk chart (as List of Maps)
            java.util.List<java.util.Map<String, Object>> categoryCountData = new java.util.ArrayList<>();
            for (Object[] row : categoryCountRaw) {
                java.util.Map<String, Object> item = new java.util.HashMap<>();
                item.put("category", row[0]);
                item.put("count", row[1]);
                categoryCountData.add(item);
            }

            java.util.List<java.util.Map<String, Object>> averagePriceData = new java.util.ArrayList<>();
            for (Object[] row : averagePriceRaw) {
                java.util.Map<String, Object> item = new java.util.HashMap<>();
                item.put("category", row[0]);
                item.put("averagePrice", row[1]);
                averagePriceData.add(item);
            }

            java.util.List<java.util.Map<String, Object>> totalPriceData = new java.util.ArrayList<>();
            for (Object[] row : totalPriceRaw) {
                java.util.Map<String, Object> item = new java.util.HashMap<>();
                item.put("category", row[0]);
                item.put("totalPrice", row[1]);
                totalPriceData.add(item);
            }

            model.addAttribute("categoryCountData", categoryCountData);
            model.addAttribute("averagePriceData", averagePriceData);
            model.addAttribute("totalPriceData", totalPriceData);

            // Summary statistics
            List<MenuItem> allMenuItems = menuItemService.getAllMenuItems(authUser.getId(), "");
            long totalMenus = allMenuItems.size();
            long availableMenus = allMenuItems.stream().filter(m -> m.getIsAvailable()).count();
            double averagePrice = allMenuItems.stream()
                    .mapToDouble(MenuItem::getPrice)
                    .average()
                    .orElse(0.0);
            long totalCategories = allMenuItems.stream()
                    .map(MenuItem::getCategory)
                    .distinct()
                    .count();

            model.addAttribute("totalMenus", totalMenus);
            model.addAttribute("availableMenus", availableMenus);
            model.addAttribute("averagePrice", averagePrice);
            model.addAttribute("totalCategories", totalCategories);

            return ConstUtil.TEMPLATE_PAGES_MENU_ITEMS_CHART;
        } catch (Exception e) {
            System.err.println("Error in chart page: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/";
        }
    }
}