package br.com.example.park_api.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // Hides fields without data
public class ParkingResponseDto {
    // Car info -------------------------------------------
    private String receipt;
    private String licencePlate;
    private String manufacturer;
    private String model;
    private String color;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss") // Format or LocalDateTime
    private LocalDateTime checkIn;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime checkOut;
    private BigDecimal value;
    private BigDecimal discount;

    // Client info ----------------------------------------
    private String clientCpf;

    // Parking Spot info ----------------------------------
    private String parkingSpotCode;
}
