package com.ecom.services;

import com.ecom.model.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    public Product saveproduct(Product product);
    public List<Product> getAllProduct();

    public Boolean deleteProduct(Integer id);

    public  Product getProductById(Integer id);

   public Product updateProduct(Product product, MultipartFile image);

    public List<Product> getAllActiveProducts(String category);
    public List<Product > searchProduct(String ch);
}
