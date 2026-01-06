package explainIt.example.demo.users.controllers;

import explainIt.example.demo.users.dtos.UserRequestDto;
import explainIt.example.demo.users.dtos.UserResponseDto;
import explainIt.example.demo.users.dtos.UserUpdateDto;
import explainIt.example.demo.users.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    /**
     * Register a new user
     * POST /api/v1/users
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // Returns 201 instead of 200
    public UserResponseDto registerUser(@Valid @RequestBody UserRequestDto requestDto) {
        log.info("POST /api/v1/users - Registering user with email: {}", requestDto.getEmail());
        return userService.registerUser(requestDto);
    }

    /**
     * Get user by ID
     * GET /api/v1/users/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        log.info("GET /api/v1/users/{} - Fetching user", id);

        return userService.findById(id)
                .map(user -> {
                    UserResponseDto response = UserResponseDto.builder()
                            .id(user.getId())
                            .username(user.getUsername())
                            .email(user.getEmail())
                            .fullName(user.getFullName())
                            .planName(user.getPlan() != null ? user.getPlan().getName() : null)
                            .createdAt(user.getCreatedAt())
                            .build();
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Update user profile
     * PUT /api/v1/users/{id}
     */
    @PutMapping("/{id}")
    public UserResponseDto updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateDto updateDto) {
        log.info("PUT /api/v1/users/{} - Updating user", id);
        return userService.updateUser(id, updateDto);
    }

    /**
     * Delete user
     * DELETE /api/v1/users/{id}
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Returns 204 (no content)
    public void deleteUser(@PathVariable Long id) {
        log.info("DELETE /api/v1/users/{} - Deleting user", id);
        userService.deleteUser(id);
    }

    /**
     * Check if email exists
     * GET /api/v1/users/check-email?email=test@example.com
     */
    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmailExists(@RequestParam String email) {
        log.info("GET /api/v1/users/check-email - Checking email: {}", email);
        boolean exists = userService.emailExists(email);
        return ResponseEntity.ok(exists);
    }

    /**
     * Check if username exists
     * GET /api/v1/users/check-username?username=testuser
     */
    @GetMapping("/check-username")
    public ResponseEntity<Boolean> checkUsernameExists(@RequestParam String username) {
        log.info("GET /api/v1/users/check-username - Checking username: {}", username);
        boolean exists = userService.usernameExists(username);
        return ResponseEntity.ok(exists);
    }

    /**
     * Change user's plan
     * PUT /api/v1/users/{id}/plan
     */
    @PutMapping("/{id}/plan")
    public UserResponseDto changePlan(
            @PathVariable Long id,
            @RequestParam Long planId) {
        log.info("PUT /api/v1/users/{}/plan - Changing to plan ID: {}", id, planId);
        return userService.changePlan(id, planId);
    }
}
