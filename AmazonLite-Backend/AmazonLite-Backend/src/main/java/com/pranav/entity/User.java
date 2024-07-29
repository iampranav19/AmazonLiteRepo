package com.pranav.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a user in the Amazon application.
 * This class is annotated with JPA annotations to map it to a database table.
 *
 * @author YourName
 * @version 1.0
 */
@Entity
@Table(name = "amazon_users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    /**
     * Unique identifier for the user.
     */
    @Id
    private String userId;

    /**
     * Full name of the user.
     */
    @Column(name = "user_name")
    private String name;

    /**
     * Email address of the user. Must be unique.
     */
    @Column(name = "user_email", unique = true)
    private String email;

    /**
     * Password for the user. Maximum length is 20 characters.
     */
    @Column(name = "user_password", length = 20)
    private String password;

    /**
     * Gender of the user.
     */
    @Column(name = "user_gender")
    private String gender;

    /**
     * Brief description about the user.
     */
    @Column(name = "user_about")
    private String about;

    /**
     * Name of the image associated with the user.
     */
    @Column(name = "user_imageName")
    private String imageName;

}
