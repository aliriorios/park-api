package br.com.example.park_api.web.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class UserUpdatePasswordDto {
    private String currentPassword;
    private String newPassword;
    private String confirmPassword;
}
