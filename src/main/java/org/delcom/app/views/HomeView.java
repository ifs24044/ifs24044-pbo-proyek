package org.delcom.app.views;

import org.delcom.app.dto.MenuItemForm;
import org.delcom.app.entities.MenuItem;
import org.delcom.app.entities.User;
import org.delcom.app.services.MenuItemService;
import org.delcom.app.utils.ConstUtil;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomeView {

    private final MenuItemService menuItemService;

    public HomeView(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }

    @GetMapping
    public String home(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String search,
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
        model.addAttribute("auth", authUser);

        // Ambil menu items dengan filter kategori atau search
        List<MenuItem> menuItems;
        if (category != null && !category.isEmpty()) {
            menuItems = menuItemService.getMenuItemsByCategory(authUser.getId(), category);
            model.addAttribute("selectedCategory", category);
        } else if (search != null && !search.isEmpty()) {
            menuItems = menuItemService.getAllMenuItems(authUser.getId(), search);
            model.addAttribute("searchKeyword", search);
        } else {
            menuItems = menuItemService.getAllMenuItems(authUser.getId(), "");
        }
        
        model.addAttribute("menuItems", menuItems);

        // Menu Item Form untuk modal
        model.addAttribute("menuItemForm", new MenuItemForm());

        return ConstUtil.TEMPLATE_MODELS_HOME;
    }
}