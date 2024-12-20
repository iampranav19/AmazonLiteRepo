package com.pranav.service.impl;

import com.pranav.dto.PageableResponse;
import com.pranav.dto.UserDto;
import com.pranav.entity.User;
import com.pranav.exception.ResourceNotFound;
import com.pranav.repositorty.UserRepository;
import com.pranav.service.UserServiceI;
import com.pranav.utility.AmazonJpaUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
@Service
public class UserServiceImpl implements UserServiceI {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

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

    @Value("${user.profile.image.path}")
    private String imageUpdloadPath;

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
    public String deleteUser(String userId) throws IOException {
        // Delete the user in the database using UserRepository
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFound("User not found for id " + userId));

        // Delete the user's image if it exists
        if (user.getImageName() != null && !user.getImageName().isEmpty()) {
            try {
                imageUpdloadPath=imageUpdloadPath+"/";
                String fullPath = imageUpdloadPath + user.getImageName();
                Path path = Paths.get(fullPath);
                Files.deleteIfExists(path); // Use deleteIfExists instead of delete
            } catch (IOException e) {
                // Log the error but continue with user deletion
                log.error("Failed to delete image file for user with id {}: {}", userId, e.getMessage());
            }
        }

        // Delete the user from database
        userRepository.deleteById(userId);
        return "User deleted successfully for id " + userId;
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

    @Override
    public PageableResponse<UserDto> getAllUsersPagination(int pageNumber, int pageSize, String sortBy, String sortDirection) {
        Sort sort=(sortDirection.equalsIgnoreCase("asc") ? (Sort.by(sortBy).ascending()) :(Sort.by(sortBy).descending()));
        Pageable pageable= PageRequest.of(pageNumber, pageSize,sort);
        Page<User> page = userRepository.findAll(pageable);
        List<User> content = page.getContent();
        // Creating custom response based on the content
        PageableResponse<UserDto> pageableResponse = new PageableResponse<>();
        pageableResponse.setContent(content.stream().map(this::convertToUserDto).collect(Collectors.toList()));
        pageableResponse.setPageNumber(pageable.getPageNumber());
        pageableResponse.setPageSize(pageable.getPageSize());
        pageableResponse.setTotalElements(page.getTotalElements());
        pageableResponse.setTotalPages(page.getTotalPages());
        pageableResponse.setLastPage(page.isLast());
        return pageableResponse;

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
