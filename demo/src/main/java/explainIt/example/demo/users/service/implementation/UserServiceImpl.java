package explainIt.example.demo.users.service.implementation;

import explainIt.example.demo.common.ResourceNotFoundException;
import explainIt.example.demo.common.UserAlreadyExistsException;
import explainIt.example.demo.plans.model.Plan;
import explainIt.example.demo.plans.repository.PlanRepository;
import explainIt.example.demo.users.dtos.UserRequestDto;
import explainIt.example.demo.users.dtos.UserResponseDto;
import explainIt.example.demo.users.dtos.UserUpdateDto;
import explainIt.example.demo.users.model.User;
import explainIt.example.demo.users.repositories.UserRepository;
import explainIt.example.demo.users.service.UserService;
import explainIt.example.demo.users.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service // Marks this as a Spring service bean
@RequiredArgsConstructor // Lombok: generates constructor for final fields
@Slf4j // Lombok: creates a logger instance
@Transactional // All methods run in a transaction by default
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PlanRepository planRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper; // ← Your MapStruct mapper

    @Override
    public UserResponseDto registerUser(UserRequestDto requestDto) {
        log.info("Registering new user with email: {}", requestDto.getEmail());

        // Validation
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new UserAlreadyExistsException("Email already registered");
        }

        if (userRepository.existsByUsername(requestDto.getUsername())) {
            throw new UserAlreadyExistsException("Username already taken");
        }

        // Get Free plan
        Plan freePlan = planRepository.findByName("Free")
                .orElseThrow(() -> new ResourceNotFoundException("Free plan not found"));

        // Map DTO to entity (password is ignored by mapper)
        User user = userMapper.toEntity(requestDto);

        // Set fields that mapper ignores
        user.setPassword(passwordEncoder.encode(requestDto.getPassword())); // Hash password
        user.setPlan(freePlan);

        // Save
        User savedUser = userRepository.save(user);

        log.info("User registered successfully with ID: {}", savedUser.getId());

        // Map entity to response DTO
        return userMapper.toResponse(savedUser);
    }

    @Override
    public UserResponseDto updateUser(Long userId, UserUpdateDto updateDto) {
        log.info("Updating user with ID: {}", userId);

        // Find user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Validate username uniqueness if changing
        if (updateDto.getUsername() != null &&
                !updateDto.getUsername().equals(user.getUsername()) &&
                userRepository.existsByUsername(updateDto.getUsername())) {
            throw new UserAlreadyExistsException("Username already taken");
        }

        // Update using mapper (only non-null fields are updated)
        userMapper.updateEntityFromDto(updateDto, user);

        // Save
        User updatedUser = userRepository.save(user);

        log.info("User updated successfully");

        return userMapper.toResponse(updatedUser);
    }
    @Override
    @Transactional(readOnly = true) // Optimization for read-only operations
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean usernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public UserResponseDto changePlan(Long userId, Long planId) {
        log.info("Changing plan for user ID: {} to plan ID: {}", userId, planId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Plan newPlan = planRepository.findById(planId)
                .orElseThrow(() -> new ResourceNotFoundException("Plan not found"));

        user.setPlan(newPlan);
        User updatedUser = userRepository.save(user);

        log.info("Plan changed successfully");

        return userMapper.toResponse(updatedUser);
    }

    @Override
    public void deleteUser(Long userId) {
        log.info("Deleting user with ID: {}", userId);

        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found");
        }

        userRepository.deleteById(userId);

        log.info("User deleted successfully");
    }
}
