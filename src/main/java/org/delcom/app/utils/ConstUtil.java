package org.delcom.app.utils;

public class ConstUtil {
    
    // ==========================================
    // TEMPLATE PATHS - MODELS (Main Pages)
    // ==========================================
    
    // Home / Main Page
    public static final String TEMPLATE_MODELS_HOME = "models/home";
    
    // Menu Items Main Pages
    public static final String TEMPLATE_MODELS_MENU_ITEMS_DETAIL = "models/menu-items/detail";
    
    // ==========================================
    // TEMPLATE PATHS - PAGES (Modals & Features)
    // ==========================================
    
    // Menu Items Pages (Modals & Chart)
    public static final String TEMPLATE_PAGES_MENU_ITEMS_ADD = "pages/menu-items/add";
    public static final String TEMPLATE_PAGES_MENU_ITEMS_EDIT = "pages/menu-items/edit";
    public static final String TEMPLATE_PAGES_MENU_ITEMS_DELETE = "pages/menu-items/delete";
    public static final String TEMPLATE_PAGES_MENU_ITEMS_EDIT_IMAGE = "pages/menu-items/edit-image";
    public static final String TEMPLATE_PAGES_MENU_ITEMS_CHART = "pages/menu-items/chart";
    
    
    // Auth Pages (jika ada)
    public static final String TEMPLATE_PAGES_AUTH_LOGIN = "pages/auth/login";
    public static final String TEMPLATE_PAGES_AUTH_REGISTER = "pages/auth/register";
    
    // ==========================================
    // FILE STORAGE
    // ==========================================
    
    public static final String UPLOAD_DIR = "uploads";
    public static final String MENU_IMAGES_DIR = "menu-images";
    
    // ==========================================
    // CATEGORIES
    // ==========================================
    
    public static final String CATEGORY_APPETIZER = "Appetizer";
    public static final String CATEGORY_MAIN_COURSE = "Main Course";
    public static final String CATEGORY_DESSERT = "Dessert";
    public static final String CATEGORY_BEVERAGE = "Beverage";
    
    // ==========================================
    // MESSAGES
    // ==========================================
    
    // Success Messages
    public static final String MSG_SUCCESS_CREATE = "Data berhasil ditambahkan";
    public static final String MSG_SUCCESS_UPDATE = "Data berhasil diperbarui";
    public static final String MSG_SUCCESS_DELETE = "Data berhasil dihapus";
    public static final String MSG_SUCCESS_UPLOAD = "File berhasil diupload";
    
    // Error Messages
    public static final String MSG_ERROR_CREATE = "Gagal menambahkan data";
    public static final String MSG_ERROR_UPDATE = "Gagal memperbarui data";
    public static final String MSG_ERROR_DELETE = "Gagal menghapus data";
    public static final String MSG_ERROR_UPLOAD = "Gagal mengupload file";
    public static final String MSG_ERROR_NOT_FOUND = "Data tidak ditemukan";
    public static final String MSG_ERROR_UNAUTHORIZED = "User tidak terautentikasi";
    
    // Validation Messages
    public static final String MSG_VALIDATION_REQUIRED = "Field ini wajib diisi";
    public static final String MSG_VALIDATION_INVALID = "Data tidak valid";
    public static final String MSG_VALIDATION_FILE_SIZE = "Ukuran file terlalu besar";
    public static final String MSG_VALIDATION_FILE_TYPE = "Tipe file tidak didukung";
    
    // ==========================================
    // API RESPONSE STATUS
    // ==========================================
    
    public static final String STATUS_SUCCESS = "success";
    public static final String STATUS_FAIL = "fail";
    public static final String STATUS_ERROR = "error";
    
    // ==========================================
    // SPICY LEVEL LABELS
    // ==========================================
    
    public static final String[] SPICY_LABELS = {
        "Tidak Pedas",     // Level 0
        "Sedikit Pedas",   // Level 1
        "Pedas",           // Level 2
        "Sangat Pedas",    // Level 3
        "Extra Pedas",     // Level 4
        "Brutal Pedas"     // Level 5
    };
    
    public static String getSpicyLabel(int level) {
        if (level >= 0 && level < SPICY_LABELS.length) {
            return SPICY_LABELS[level];
        }
        return "Unknown";
    }
}