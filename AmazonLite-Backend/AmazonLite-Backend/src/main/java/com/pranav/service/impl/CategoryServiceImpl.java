package com.pranav.service.impl;

import com.pranav.dto.CategoryDto;
import com.pranav.entity.Category;
import com.pranav.exception.ResourceNotFound;
import com.pranav.repositorty.CategoryRepository;
import com.pranav.service.CategoryService;
import com.pranav.utility.AmazonJpaUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private static final Logger log = LoggerFactory.getLogger(CategoryServiceImpl.class);
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AmazonJpaUtils jpaUtils;
    @Autowired
    private ModelMapper modelMapper;

    @Value("${category.cover.image.path}")
    private String imageUploadPath;

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {

        Category category=mapToEntity(categoryDto);
        category.setCategoryId(jpaUtils.createId());
        Category savedCategory = categoryRepository.save(category);
        return modelMapper.map(savedCategory, CategoryDto.class);
    }

    @Override
    public CategoryDto updateCategory(String id, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        category.setTitle(categoryDto.getTitle());
        category.setDescription(categoryDto.getDescription());
        category.setCoverImageName(categoryDto.getCoverImageName());

        Category updatedCategory = categoryRepository.save(category);
        return modelMapper.map(updatedCategory, CategoryDto.class);
    }

    @Override
    public String deleteCategory(String id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Category not found with id: " + id));

        // Delete the category's cover image if it exists
        if (category.getCoverImageName() != null && !category.getCoverImageName().isEmpty()) {
            try {
                imageUploadPath = imageUploadPath + "/";
                String fullPath = imageUploadPath + category.getCoverImageName();
                Path path = Paths.get(fullPath);
                Files.deleteIfExists(path);
            } catch (IOException e) {
                // Log the error but continue with category deletion
                log.error("Failed to delete cover image for category with id {}: {}", id, e.getMessage());
            }
        }

        categoryRepository.delete(category);
        return "Category deleted successfully";
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(category -> modelMapper.map(category, CategoryDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategoryById(String id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        return modelMapper.map(category, CategoryDto.class);
    }

    @Override
    public CategoryDto getCategoryByName(String name) {

        return mapToDto(categoryRepository.findByTitle(name).orElseThrow(() -> new RuntimeException(" Category not found with name: " + name )));

    }

    private Category mapToEntity(CategoryDto categoryDto)
    {
        return modelMapper.map(categoryDto, Category.class);
    }

    private CategoryDto mapToDto(Category category)
    {
        return modelMapper.map(category, CategoryDto.class);
    }
}