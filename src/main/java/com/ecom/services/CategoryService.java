package com.ecom.services;

import com.ecom.model.Category;

import java.util.List;

public interface CategoryService {
    public Category saveCategory(Category category);
     public Boolean exitsCategory(String name);
    public List<Category> getAllCategory();
}
