package com.pranav.controller;

import com.pranav.dto.ApiResponse;
import com.pranav.dto.UserDto;
import com.pranav.service.UserServiceI;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/amazon/users/")
public class AmazonUserController {

    private UserServiceI userService;

    // Constructor injection to inject UserServiceI
    @Autowired
    public AmazonUserController(UserServiceI userService)
    {
        this.userService = userService;
    }

    // Endpoint to create a new user
    @PostMapping()
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto)
    {
        return new ResponseEntity<>(userService.createUser(userDto), HttpStatus.CREATED);
    }

    // Endpoint to fetch the user based on the Id
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable String id)
    {
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }

    // Endpoint to fetch the user based on the email
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email)
    {
        return new ResponseEntity<>(userService.getUserByEmail(email), HttpStatus.OK);
    }

    // Endpoint to fetch all users
    @GetMapping()
    public ResponseEntity<List<UserDto>> getAllUsers()
    {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    // Endpoint to update the user
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable String id, @RequestBody UserDto userDto)
    {
        userDto.setUserId(id); // Set the user ID before updating the user.
        return new ResponseEntity<>(userService.updateUser(userDto), HttpStatus.OK);
    }

    // Endpoint to delete the user
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable String id)
    {
        String message = userService.deleteUser(id);
        ApiResponse apiResponse =ApiResponse.builder()
                .message(message)
                .success(true)
                .status(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    // Endpoint for the search results
    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<UserDto>> searchUser(@PathVariable String keyword)
    {
        return new ResponseEntity<>(userService.searchUser(keyword), HttpStatus.OK);
    }


}

/*
        ************* API URL : based on the Localhost ******************************************
        POST http://localhost:8931/amazon/users/	Create a new user
        GET	http://localhost:8931/amazon/users/{id}	Get user by ID
        GET	http://localhost:8931/amazon/users/email/{email}	Get user by email
        GET	http://localhost:8931/amazon/users/	Get all users
        PUT	http://localhost:8931/amazon/users/{id}	Update a user
        DELETE	http://localhost:8931/amazon/users/{id}	Delete a user
        Search http://localhost:8931/amazon/users/search/{keyword}

      ************ Sample API JSON ********************************************
      * {
    "name": "Evan Garcia",
    "email": "evan.garcia@example.com",
    "password": "evanTechPass99",
    "gender": "Male",
    "about": "Aspiring entrepreneur in the tech world.",
    "imageName": "evan_icon.png"
     }

 */
