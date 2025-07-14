package com.ecom.controller;

import com.ecom.model.Category;
import com.ecom.model.Product;
import com.ecom.services.CategoryService;
import com.ecom.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;


@Controller
public class HomeController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;
    @GetMapping("/index")
    public String ShowIndex() {
        return "index";
    }
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    @GetMapping("/register")
    public String register() {
        return "register";
    }
    @GetMapping("/base")
    public String base() {
        return "base";
    }

    @GetMapping("/products")
    public String products(Model m, @RequestParam(value = "category", defaultValue="") String category) {
        System.out.println("category= "+category);
        List<Category> categories=categoryService.getAllCategory();
        List<Product> products=productService.getAllActiveProducts(category);

        m.addAttribute("categories",categories);
        m.addAttribute("products",products);
        m.addAttribute("paramValue",category);

        return "product";
    }
    @GetMapping("/product/{id}")
    public String product(@PathVariable int id, Model m) {
        Product productById = productService.getProductById(id);
        m.addAttribute("product", productById);
        return "view_product";
    }
}
