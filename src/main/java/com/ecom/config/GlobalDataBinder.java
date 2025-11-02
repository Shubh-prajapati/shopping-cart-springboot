package com.ecom.config;

import com.ecom.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;

@ControllerAdvice
public class GlobalDataBinder {

    @Autowired
    private CategoryService categoryService;

    @ModelAttribute
    public void globalAttributes(Model model) {
        // Only active categories will be shown in navbar
        model.addAttribute("categorys", categoryService.getAllActiveCategory());

        // Optional: debug log
        System.out.println("Loaded active categories: " + categoryService.getAllActiveCategory().size());
    }
}
