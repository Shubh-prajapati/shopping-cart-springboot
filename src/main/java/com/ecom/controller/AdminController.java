package com.ecom.controller;


import com.ecom.model.Category;
import com.ecom.services.CategoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


    @Controller
    @RequestMapping("/admin")
    public class AdminController {

        @Autowired
        private CategoryService categoryService;

        @GetMapping("/")
        public String index()
        {
            return "admin/index";
        }

        @GetMapping("/loadAddProduct")
        public String loadAddProduct()
        {
            return "admin/add_product";
        }

        @GetMapping("/category")
        public String category()
        {
            return "admin/category";
        }

        @PostMapping("/saveCategory")
        public String saveCategory(@ModelAttribute Category category, HttpSession session){

           Boolean existCategory = categoryService.exitsCategory(category.getName());
            if (existCategory)
            {
                session.setAttribute("errorMsg", "Category Name already exits");
            }else {
                Category saveCategory = categoryService.saveCategory(category);
                if(!ObjectUtils.isEmpty(saveCategory)){
                    session.setAttribute("errorMsg","Not saved ! internal server error");
                }else{
                    session.setAttribute("successMsg","saved successfully");

                }

            }


            return "redirect/category";
        }


    }

