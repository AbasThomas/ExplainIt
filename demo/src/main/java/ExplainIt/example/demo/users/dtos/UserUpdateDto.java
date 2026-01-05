package ExplainIt.example.demo.users.dtos;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserUpdateDto {

    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    @Size(max = 100, message = "Full name must not exceed 100 characters")
    private String fullName;

    // Users can't update email directly (requires verification)
    // Users can't update password here (separate endpoint)
}
