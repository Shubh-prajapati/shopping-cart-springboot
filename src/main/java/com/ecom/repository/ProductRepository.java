package com.ecom.repository;

import com.ecom.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {



    List<Product> findByCategory(String category);

    List<Product> findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(String ch , String ch2);

    Page<Product> findByIsActiveTrue(Pageable pageable);

    List<Product> findByIsActiveTrue();


    Page<Product> findByCategory(Pageable pageable,String category);
    Page<Product> findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(String ch, String ch1, Pageable pageable);
}
