package br.com.example.park_api.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ParkingResponseDto {
    // Car info -------------------------------------------
    private String receipt;
    private String licencePlate;
    private String manufacturer;
    private String model;
    private String color;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private BigDecimal value;
    private BigDecimal discount;

    // Client info ----------------------------------------
    private String clientCpf;

    // Parking Spot info ----------------------------------
    private String parkingSpotCode;
}
