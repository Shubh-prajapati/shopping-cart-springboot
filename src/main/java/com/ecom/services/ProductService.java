package com.ecom.services;

import com.ecom.model.Product;

import java.util.List;

public interface ProductService {
    public Product saveproduct(Product product);
    public List<Product> getAllProduct();

    public Boolean deleteProduct(Integer id);



}
