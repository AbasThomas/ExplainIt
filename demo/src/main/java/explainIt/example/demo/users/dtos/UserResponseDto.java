package explainIt.example.demo.users.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder // Allows: UserResponseDto.builder().id(1L).email("...").build()
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    private Long id;
    private String username;
    private String email;
    private String fullName;
    private String planName;
    private LocalDateTime createdAt;
    // No password! Never send passwords to clients
}
