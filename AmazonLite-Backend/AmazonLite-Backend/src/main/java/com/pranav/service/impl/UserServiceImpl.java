package com.pranav.service.impl;

import com.pranav.dto.UserDto;
import com.pranav.entity.User;
import com.pranav.repositorty.UserRepository;
import com.pranav.service.UserServiceI;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

public class UserServiceImpl implements UserServiceI {

    // inject UserRepository using Constructor Injection
    private UserRepository userRepository;

    // Constructor Injection
    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto createUser(UserDto user) {
        // generate unique id in String format using UUID
        user.setUserId(UUID.randomUUID().toString());
        User savedUser = convertToUser(user);
        userRepository.save(savedUser);
        return convertToUserDto(savedUser);
    }

    @Override
    public UserDto updateUser(UserDto user) {
        return null;
    }

    @Override
    public void deleteUser(String userId) {

    }

    @Override
    public List<UserDto> getAllUsers() {
        return null;
    }

    @Override
    public UserDto getUserById(String userId) {
        return null;
    }

    @Override
    public UserDto getUserByEmail(String email) {
        return null;
    }

    @Override
    public List<UserDto> searchUser(String keyword) {
        return null;
    }

    /**
     * Converts a UserDto object to a User entity.
     *
     * @param userDto the UserDto object to be converted
     * @return the converted User entity
     */
    private User convertToUser(UserDto userDto) {
        if (userDto == null) {
            return null;
        }
        User user = new User();
        user.setUserId(userDto.getUserId());
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setGender(userDto.getGender());
        user.setAbout(userDto.getAbout());
        user.setImageName(userDto.getImageName());

        return user;
    }

    /**
     * Converts a User entity to a UserDto object.
     *
     * @param user the User entity to be converted
     * @return the converted UserDto object
     */
    private UserDto convertToUserDto(User user) {
        if (user == null) {
            return null;
        }
        return UserDto.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .gender(user.getGender())
                .about(user.getAbout())
                .imageName(user.getImageName())
                .build();
    }
}
