package com.pranav.controller;

import com.pranav.dto.CategoryDto;
import com.pranav.dto.ImageResponse;
import com.pranav.service.CategoryService;
import com.pranav.service.FileService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private FileService fileService;

    @Value("${category.cover.image.path}")
    private String imageUploadPath;

    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto categoryDto) {
        CategoryDto createdCategory = categoryService.createCategory(categoryDto);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(
            @PathVariable("categoryId") String categoryId,
            @RequestBody CategoryDto categoryDto
    ) {
        CategoryDto updatedCategory = categoryService.updateCategory(categoryId, categoryDto);
        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable("categoryId") String categoryId) {
        String response = categoryService.deleteCategory(categoryId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        List<CategoryDto> categories = categoryService.getAllCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable("categoryId") String categoryId) {
        CategoryDto category = categoryService.getCategoryById(categoryId);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @GetMapping("/title/{title}")
    public ResponseEntity<CategoryDto> getCategoryByTitle(@PathVariable("title") String title) {
        CategoryDto category = categoryService.getCategoryByName(title);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    // Upload category cover image
    @PostMapping("/cover-image/{categoryId}")
    public ResponseEntity<ImageResponse> uploadCoverImage(
            @RequestParam("coverImage") MultipartFile coverImage,
            @PathVariable String categoryId) throws IOException {

        String imageName = fileService.uploadFile(coverImage, imageUploadPath);

        // Update category cover image name in database
        CategoryDto category = categoryService.getCategoryById(categoryId);
        category.setCoverImageName(imageName);
        categoryService.updateCategory(categoryId, category);

        ImageResponse imageResponse = ImageResponse.builder()
                .imageName(imageName)
                .success(true)
                .status(HttpStatus.OK)
                .build();

        return new ResponseEntity<>(imageResponse, HttpStatus.CREATED);
    }

    // Serve category cover image
    @GetMapping("/cover-image/{categoryId}")
    public void getCoverImage(@PathVariable String categoryId, HttpServletResponse response) throws IOException {
        CategoryDto category = categoryService.getCategoryById(categoryId);
        InputStream resource = fileService.getResource(imageUploadPath, category.getCoverImageName());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());
    }
}