package com.pranav.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

    @NotBlank(message = "Product name must not be empty")
    private String name;

    private String image;

    private double price;

    private String description;

    private double rating;

    private int quantity;

    private Date addedDate;

    private Date updatedDate;

    private boolean live;

    private int discountedPrice;

    private boolean stock;

    private String brand;

    private String category;

    private String productType;
}
