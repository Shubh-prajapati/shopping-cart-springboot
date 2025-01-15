package com.ecom.repository;

import com.ecom.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;


public class CategoryRepository implements JpaRepository<Category, Integer> {

    public Boolean existsByName(String name) ;

