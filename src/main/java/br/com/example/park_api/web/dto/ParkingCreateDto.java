package br.com.example.park_api.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ParkingCreateDto {
    // Car info -------------------------------------------
    @NotBlank
    @Size(min = 8, max = 8)
    @Pattern(regexp = "[A-Z]{3}-[0-9]{4}", message = "The vehicle's license plate must follow the standard: 'XXX-0000'")
    private String licencePlate;

    @NotBlank
    private String manufacturer;

    @NotBlank
    private String model;

    @NotBlank
    private String color;

    // Client info ----------------------------------------
    @NotBlank
    @Size(min = 11, max = 11, message = "CPF must contain 11 characters")
    @CPF
    private String clientCpf;
}
