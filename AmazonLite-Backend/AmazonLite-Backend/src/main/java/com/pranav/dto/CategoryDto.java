package com.pranav.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {

    @NotBlank(message = "Category Title is required")
    @Size(min = 4, max = 100, message = "Category Title must be between 4 and 100 characters")
    private String title;

    @NotBlank(message = "Category Description is required")
    @Size(min = 10, max = 1000, message = "Category Description must be between 10 and 1000 characters")
    private String description;

    private String coverImageName;
}
