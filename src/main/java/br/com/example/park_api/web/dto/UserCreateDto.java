package br.com.example.park_api.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class UserCreateDto {
    // Jakarta Bean Validation
    @NotBlank
    @Email(regexp = "^[A-Za-z0-9._%+-]+@[a-z0-9..-]+\\.[a-z]{2,}$", message = "Email format is invalid.")
    private String username;

    @NotBlank
    @Size(min = 6, max = 6, message = "Password must contain 6 characters")
    private String password;
}
