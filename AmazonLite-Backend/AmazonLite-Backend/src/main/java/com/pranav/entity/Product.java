package com.pranav.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "amazon_product")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String productId;

    @NotBlank(message = "Product name must not be empty")
    @Column(name = "product_name", nullable = false)
    private String name;

    @Column(name = "product_image", nullable = false)
    private String image;

    @Column(name = "product_price", nullable = false)
    private double price;

    @Column(name = "product_description")
    private String description;

    @Column(name = "product_rating")
    private double rating;

    private int quantity;

    @Column(name = "product_added_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date addedDate;

    @Column(name = "product_updated_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;

    @Column(name = "product_live")
    private boolean live;

    @Column(name = "product_discounted_price")
    private int discountedPrice;

    @Column(name = "product_stock")
    private boolean stock;

    @Column(name = "product_brand")
    private String brand;

    @Column(name = "product_category")
    private String categoryString;

    @Column(name = "product_type")
    private String productType;

    @ManyToOne
    @JoinColumn(name = "category")
    private Category category;



}
