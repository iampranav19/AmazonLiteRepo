package com.pranav.service.impl;

import com.pranav.dto.UserDto;
import com.pranav.entity.User;
import com.pranav.exception.ResourceNotFound;
import com.pranav.repositorty.UserRepository;
import com.pranav.service.UserServiceI;
import com.pranav.utility.AmazonJpaUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserServiceI {

    // inject UserRepository using Constructor Injection
    private UserRepository userRepository;

    // Constructor Injection
    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    private AmazonJpaUtils jpaUtils;

    @Autowired
    private ModelMapper mapper ;

    @Override
    public UserDto createUser(UserDto user) {
        // generate unique id in String format using UUID
        user.setUserId(jpaUtils.createId());
        User savedUser = convertToUser(user);
        userRepository.save(savedUser);
        return convertToUserDto(savedUser);
    }


    @Override
    public UserDto updateUser(UserDto user)
    {
        // update the user in the database using UserRepository
        User existingUser = userRepository.findById(user.getUserId()).orElseThrow(()-> new ResourceNotFound("User not found for id " + user.getUserId()));
        if (existingUser!= null) {
            existingUser.setName(user.getName());
            existingUser.setEmail(user.getEmail());
            existingUser.setPassword(user.getPassword());
            existingUser.setGender(user.getGender());
            existingUser.setAbout(user.getAbout());
            existingUser.setImageName(user.getImageName());
            userRepository.save(existingUser);
            return convertToUserDto(existingUser);
        }
        return null;
    }

    @Override
    public String deleteUser(String userId) {
        // Delete the user in the database using UserRepository
        User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFound("User not found for id "+userId));
        if(user !=null) {
            userRepository.deleteById(userId);
        }
        return "User deleted successfully for id "+userId;

    }

    @Override
    public List<UserDto> getAllUsers() {
        // Retrieve all users from the database using UserRepository
        List<User> users = userRepository.findAll();
        if (!users.isEmpty()) {
            return users.stream().map(this::convertToUserDto).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public UserDto getUserById(String userId) {
        return convertToUserDto(userRepository.findById(userId).orElseThrow(()-> new ResourceNotFound("User not found for id "+userId)));
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if(user == null) {
            throw new ResourceNotFound("User not found for email " + email);
        }
        return convertToUserDto(user);
    }

    @Override
    public List<UserDto> searchUser(String keyword) {
        // Search for users based on the keyword in the database using UserRepository
        List<User> users = userRepository.findByNameContaining(keyword);
        if (!users.isEmpty()) {
            return users.stream().map(this::convertToUserDto).collect(Collectors.toList());
        }
        // If no users found for the keyword, return null
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

        mapper.map(user, UserDto.class);

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
