package br.com.example.park_api.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.br.CPF;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class ClientCreateDto {
    @NotBlank
    @Size(min = 5, max = 100, message = "Name must be longer between 5 and 100 characters")
    private String name;

    @NotBlank
    @Size(min = 11, max = 11, message = "CPF must contain 11 characters")
    @CPF
    private String cpf;
}
