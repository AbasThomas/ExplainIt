package explainIt.example.demo.users.mapper;

import explainIt.example.demo.users.dtos.UserRequestDto;
import explainIt.example.demo.users.dtos.UserResponseDto;
import explainIt.example.demo.users.dtos.UserUpdateDto;
import explainIt.example.demo.users.model.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Map UserRequestDto to User entity (for registration)
     * Ignore fields that shouldn't be set from request
     */
    @Mapping(target = "id", ignore = true)              // DB generates this
    @Mapping(target = "password", ignore = true)        // Handled separately (needs hashing)
    @Mapping(target = "plan", ignore = true)            // Set in service layer
    @Mapping(target = "createdAt", ignore = true)       // Auto-generated
    @Mapping(target = "updatedAt", ignore = true)       // Auto-generated
    User toEntity(UserRequestDto dto);

    /**
     * Map User entity to UserResponseDto
     * Custom mapping for nested plan name
     */
    @Mapping(source = "plan.name", target = "planName") // Map nested field
    UserResponseDto toResponse(User user);

    /**
     * Update existing User entity from UserUpdateDto
     * Only update non-null fields (partial update)
     */
    @Mapping(target = "id", ignore = true)              // Never change ID
    @Mapping(target = "email", ignore = true)           // Email changes require verification
    @Mapping(target = "password", ignore = true)        // Password changes need separate endpoint
    @Mapping(target = "plan", ignore = true)            // Plan changes need separate endpoint
    @Mapping(target = "createdAt", ignore = true)       // Never change creation date
    @Mapping(target = "updatedAt", ignore = true)       // Handled by @PreUpdate
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(UserUpdateDto dto, @MappingTarget User user);
}
