package com.ecom.service.impl;

import com.ecom.model.Product;
import com.ecom.repository.ProductRepository;
import com.ecom.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Product saveproduct(Product product) {
        return productRepository.save(product);
    }
}
