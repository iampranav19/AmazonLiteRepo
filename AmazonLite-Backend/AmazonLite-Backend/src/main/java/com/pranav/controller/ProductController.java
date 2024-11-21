package com.pranav.controller;

import com.pranav.dto.ProductDto;
import com.pranav.service.FileService;
import com.pranav.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.pranav.dto.ImageResponse;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    private FileService fileService;

    @Value("${product.image.path}")
    private String imageUploadPath;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // Create a new product
    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto) {
        ProductDto createdProduct = productService.createProduct(productDto);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    // Update an existing product
    @PutMapping("/{productId}")
    public ResponseEntity<ProductDto> updateProduct(
            @RequestBody ProductDto productDto,
            @PathVariable String productId) {
        ProductDto updatedProduct = productService.updateProduct(productDto, productId);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    // Delete a product
    @DeleteMapping("/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable String productId) {
        String deletedProduct = productService.deleteProduct(productId);
        return new ResponseEntity<>(deletedProduct, HttpStatus.OK);
    }

    // Get a product by ID
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable String productId) {
        ProductDto product = productService.getProductById(productId);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    // Get all products
    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<ProductDto> products = productService.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    // Search products by category
    @GetMapping("/category/{category}")
    public ResponseEntity<List<ProductDto>> searchProductsByCategory(@PathVariable String category) {
        List<ProductDto> products = productService.searchProductsByCategory(category);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    // Search products by keyword
    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<ProductDto>> searchProductsByKeyword(@PathVariable String keyword) {
        List<ProductDto> products = productService.searchProductsByKeyword(keyword);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    // Upload product image
    @PostMapping("/image/{productId}")
    public ResponseEntity<ImageResponse> uploadProductImage(
            @RequestParam("productImage") MultipartFile productImage,
            @PathVariable String productId) throws IOException {

        String imageName = fileService.uploadFile(productImage, imageUploadPath);

        // Update product image name in database
        ProductDto product = productService.getProductById(productId);
        product.setImage(imageName);
        productService.updateProduct(product, productId);

        ImageResponse imageResponse = ImageResponse.builder()
                .imageName(imageName)
                .success(true)
                .status(HttpStatus.OK)
                .build();

        return new ResponseEntity<>(imageResponse, HttpStatus.CREATED);
    }

    // Serve product image
    @GetMapping("/image/{productId}")
    public void getProductImage(
            @PathVariable String productId,
            HttpServletResponse response) throws IOException {
        ProductDto product = productService.getProductById(productId);
        InputStream resource = fileService.getResource(imageUploadPath, product.getImage());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());
    }
}
