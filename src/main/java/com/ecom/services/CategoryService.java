package com.ecom.services;

import com.ecom.model.Category;

import java.util.List;

public interface CategoryService {
    public Category saveCategory(Category category);
    public List<Category> getAllCategory();
}
