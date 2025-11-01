package com.ecom.services;

import com.ecom.model.Category;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.List;
@Service
public interface CategoryService {
    public Category saveCategory(Category category);

    public List<Category> getAllCategory();

     public Boolean existCategory(String name);

     public Boolean deleteCategory(int id);

     public Category getCategoryById(int id);

     public List<Category> getAllActiveCategory();

     public Page<Category> getAllCategoryPagination(Integer pageNo, Integer pageSize);

}
