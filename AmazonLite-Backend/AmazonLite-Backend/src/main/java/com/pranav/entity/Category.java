package com.pranav.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "amazon_categories")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String categoryId;

    @NotBlank(message = "Category Title is required")
    @Size(min = 4, max = 100, message = "Category Description is required")
    @Column(unique = true , nullable = false)
    private String title;

    @Size(min = 10, max = 1000, message = "Category Description is required")
    @Column(length = 1000)
    @NotBlank(message = "Category Description is required")
    private String description;

    @Column(name = "cover_image_name", length = 200)
    private String coverImageName;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Product> products=new ArrayList<>();

}
