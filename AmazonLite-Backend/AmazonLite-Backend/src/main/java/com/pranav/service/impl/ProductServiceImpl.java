package com.pranav.service.impl;

import com.pranav.dto.CategoryDto;
import com.pranav.dto.ProductDto;
import com.pranav.entity.Product;
import com.pranav.exception.ResourceNotFound;
import com.pranav.repositorty.ProductRepository;
import com.pranav.service.CategoryService;
import com.pranav.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    @Autowired
    private CategoryService categoryService;

    @Value("${product.image.path}")
    private String imageUploadPath;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        Product product = modelMapper.map(productDto, Product.class);
        Product savedProduct = productRepository.save(product);
        return modelMapper.map(savedProduct, ProductDto.class);
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto,String productId) {
        Product existingProduct = productRepository.findById(productId).orElseThrow(()->new ResourceNotFound("Product not found  for productId: " + productId));

        // Update fields
        existingProduct.setName(productDto.getName());
        existingProduct.setImage(productDto.getImage());
        existingProduct.setPrice(productDto.getPrice());
        existingProduct.setDescription(productDto.getDescription());
        existingProduct.setRating(productDto.getRating());
        existingProduct.setQuantity(productDto.getQuantity());
        existingProduct.setAddedDate(productDto.getAddedDate());
        existingProduct.setUpdatedDate(productDto.getUpdatedDate());
        existingProduct.setLive(productDto.isLive());
        existingProduct.setDiscountedPrice(productDto.getDiscountedPrice());
        existingProduct.setStock(productDto.isStock());
        existingProduct.setBrand(productDto.getBrand());
        existingProduct.setCategoryString(productDto.getCategory());
        existingProduct.setProductType(productDto.getProductType());

        Product updatedProduct = productRepository.save(existingProduct);
        return modelMapper.map(updatedProduct, ProductDto.class);
    }

    @Override
    public String deleteProduct(String productId)  {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFound("Product not found for ID: " + productId));

        // Delete the product's image if it exists
        if (product.getImage() != null && !product.getImage().isEmpty()) {
            try {
                imageUploadPath = imageUploadPath + "/";
                String fullPath = imageUploadPath + product.getImage();
                Path path = Paths.get(fullPath);
                Files.deleteIfExists(path);
            } catch (IOException e) {
                // Log the error but continue with product deletion
                throw new RuntimeException("Product deletion failed", e);
            }
        }

        productRepository.delete(product);
        return "Product deleted successfully with id: " + productId;
    }

    @Override
    public ProductDto getProductById(String productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found for ID: " + productId));
        return modelMapper.map(product, ProductDto.class);
    }

    @Override
    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(product -> modelMapper.map(product, ProductDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDto> searchProductsByCategory(String category) {
        List<Product> products = productRepository.findByCategoryString(category).get();

        if (products.isEmpty()) {
            throw new RuntimeException("Products not found for category: " + category);
        }

        return products.stream()
                .map(product -> modelMapper.map(product, ProductDto.class))
                .collect(Collectors.toList());
    }


    @Override
    public List<ProductDto> searchProductsByKeyword(String keyword) {
        List<Product> products = productRepository.findByNameContaining(keyword).orElseThrow(() -> new ResourceNotFound("Product not found for keyword: " + keyword));
        return products.stream()
                .map(product -> modelMapper.map(product, ProductDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public ProductDto createProductByCategory(ProductDto product, String id) {
        CategoryDto categoryById = categoryService.getCategoryById(id);
        if(categoryById!=null)
        {
            product.setCategory(categoryById.getTitle());
            return createProduct(product);
        }
        return null;
    }
}
