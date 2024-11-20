package com.pranav.service;

import com.pranav.dto.UserDto;
import com.pranav.entity.User;

import java.util.List;

/**
 * This interface defines the contract for the User Service.
 * It provides methods for CRUD operations and searching users.
 */
public interface UserServiceI {

    /**
     * Creates a new user.
     *
     * @param user The user object to be created.
     * @return The created user object with the generated ID.
     */
    public UserDto createUser(UserDto user);

    /**
     * Updates an existing user.
     *
     * @param user The user object with updated information.
     * @return The updated user object.
     */
    public UserDto updateUser(UserDto user);

    /**
     * Deletes a user by their ID.
     *
     * @param userId The ID of the user to be deleted.
     */
    public String deleteUser(String userId);

    /**
     * Retrieves all users.
     *
     * @return A list of all user objects.
     */
    public List<UserDto> getAllUsers();

    /**
     * Retrieves a user by their ID.
     *
     * @param userId The ID of the user to be retrieved.
     * @return The user object with the specified ID.
     */
    public UserDto getUserById(String userId);

    /**
     * Retrieves a user by their email.
     *
     * @param email The email of the user to be retrieved.
     * @return The user object with the specified email.
     */
    public UserDto getUserByEmail(String email);

    /**
     * Searches for users based on a keyword.
     *
     * @param keyword The keyword to search for.
     * @return A list of user objects that match the search criteria.
     */
    public List<UserDto> searchUser(String keyword);

    List<UserDto> getAllUsersPagination(int pageNumber, int pageSize);
}
