package br.com.example.park_api.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ParkingSpotResponseDto {
    private Long id;
    private String code;
    private String status;
}
