package com.ecom.services;

import com.ecom.model.Category;
import org.springframework.stereotype.Service;


import java.util.List;
@Service
public interface CategoryService {
    public Category saveCategory(Category category);
     public Boolean exitsCategory(String name);
    public List<Category> getAllCategory();
}
