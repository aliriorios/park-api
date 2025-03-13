package br.com.example.park_api.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class UserUpdatePasswordDto {
    @NotBlank
    @Size(min = 6, max = 6, message = "The size should be between 6 and 6")
    private String currentPassword;

    @NotBlank
    @Size(min = 6, max = 6, message = "The size should be between 6 and 6")
    private String newPassword;

    @NotBlank
    @Size(min = 6, max = 6, message = "The size should be between 6 and 6")
    private String confirmPassword;
}
