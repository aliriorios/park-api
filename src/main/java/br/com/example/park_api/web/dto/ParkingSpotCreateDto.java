package br.com.example.park_api.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ParkingSpotCreateDto {
    @NotBlank(message = "{NotBlank.parkingSpotCreateDto.code}")
    @Size(min = 4, max = 4, message = "{Size.parkingSpotCreateDto.code}")
    private String code;

    @NotBlank(message = "{NotBlank.parkingSpotCreateDto.status}")
    @Pattern(regexp = "FREE|OCCUPIED", message = "{Pattern.parkingSpotCreateDto.status}")
    private String status;
}
