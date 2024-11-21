package com.pranav.service;

import com.pranav.dto.CategoryDto;
import com.pranav.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface CategoryService {


    // Create the category
    CategoryDto createCategory(CategoryDto categoryDto);

    // Update the category
    CategoryDto updateCategory(String id, CategoryDto category);

    // Delete the category
    String deleteCategory(String id);

    // Get all categories
    List<CategoryDto> getAllCategories();

    // Get category by ID
    CategoryDto getCategoryById(String id);

    // Get category by name
    CategoryDto getCategoryByName(String name);


}
