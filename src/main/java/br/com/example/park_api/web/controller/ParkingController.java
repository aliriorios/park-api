package br.com.example.park_api.web.controller;

import br.com.example.park_api.entity.ClientParkingSpot;
import br.com.example.park_api.service.ParkingService;
import br.com.example.park_api.web.dto.ParkingCreateDto;
import br.com.example.park_api.web.dto.ParkingResponseDto;
import br.com.example.park_api.web.dto.mapper.ClientParkingSpotMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/api/v1/parking")
@RequiredArgsConstructor
public class ParkingController {
    private final ParkingService parkingService;

    @PostMapping(value = "/check-in")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ParkingResponseDto> checkIn(@Valid @RequestBody ParkingCreateDto createDto) {
        ClientParkingSpot clientParkingSpot = ClientParkingSpotMapper.toClientParkingSpot(createDto);
        parkingService.checkIn(clientParkingSpot);

        ParkingResponseDto responseDto = ClientParkingSpotMapper.toResponseDto(clientParkingSpot);

        // Returns both the header with the URI to access the created resource, and the created resource itself
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri().path("/{receipt}")
                .buildAndExpand(clientParkingSpot.getReceipt())
                .toUri();

        return ResponseEntity.status(HttpStatus.CREATED)
                .header(HttpHeaders.LOCATION, location.toString())
                .body(responseDto);
    }
}
