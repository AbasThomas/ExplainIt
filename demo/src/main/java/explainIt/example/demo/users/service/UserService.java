package ExplainIt.example.demo.users.service;

import ExplainIt.example.demo.users.dtos.UserRequestDto;
import ExplainIt.example.demo.users.dtos.UserResponseDto;
import ExplainIt.example.demo.users.dtos.UserUpdateDto;
import ExplainIt.example.demo.users.model.User;

import java.util.Optional;

public interface UserService {

    /**
     * Register a new user with the Free plan
     */
    UserResponseDto registerUser(UserRequestDto registrationDto);

    /**
     * Find user by email
     */
    Optional<User> findByEmail(String email);

    /**
     * Find user by ID
     */
    Optional<User> findById(Long id);

    /**
     * Check if email is already taken
     */
    boolean emailExists(String email);

    /**
     * Check if username is already taken
     */
    boolean usernameExists(String username);

    /**
     * Update user profile
     */
    UserResponseDto updateUser(Long userId, UserUpdateDto updateDto);

    /**
     * Change user's plan
     */
    UserResponseDto changePlan(Long userId, Long planId);

    /**
     * Delete user account
     */
    void deleteUser(Long userId);
}