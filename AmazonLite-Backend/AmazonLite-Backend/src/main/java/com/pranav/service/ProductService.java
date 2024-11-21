package com.pranav.service;

import com.pranav.dto.ProductDto;
import com.pranav.entity.Category;

import java.util.List;

public interface ProductService {

    // Define methods for CRUD operations and searching products

    public ProductDto createProduct(ProductDto product);

    public ProductDto updateProduct(ProductDto product, String productId);

    public String deleteProduct(String productId);

    public ProductDto getProductById(String productId);

    public List<ProductDto> getAllProducts();

    public List<ProductDto> searchProductsByCategory(String category);

    public List<ProductDto> searchProductsByKeyword(String keyword);

    // create the Product with the Category
    ProductDto createProductByCategory(ProductDto product, String id);


}
