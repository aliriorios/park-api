package br.com.example.park_api.web.controller;

import br.com.example.park_api.entity.ParkingSpot;
import br.com.example.park_api.service.ParkingSpotService;
import br.com.example.park_api.web.dto.ParkingSpotCreateDto;
import br.com.example.park_api.web.dto.ParkingSpotResponseDto;
import br.com.example.park_api.web.dto.mapper.ParkingSpotMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/api/v1/parking-spot")
@RequiredArgsConstructor
public class ParkingSpotController {
    private final ParkingSpotService parkingSpotService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> save (@Valid @RequestBody ParkingSpotCreateDto createDto) {
        ParkingSpot parkingSpot = ParkingSpotMapper.toParkingSpot(createDto);
        parkingSpotService.save(parkingSpot);

        // URI to access the created resource
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri().path("/{code}")
                .buildAndExpand(parkingSpot.getCode())
                .toUri();

        // Generates in the HEADER the URI to access the created resource
        return ResponseEntity.status(HttpStatus.CREATED)
                .header(HttpHeaders.LOCATION, location.toString())
                .build();
    }

    // Get by URI with code
    @GetMapping(value = "/{code}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ParkingSpotResponseDto> findByCode (@PathVariable String code) {
        ParkingSpot parkingSpot = parkingSpotService.findByCode(code);

        return ResponseEntity.status(HttpStatus.OK).body(ParkingSpotMapper.toResponseDto(parkingSpot));
    }
}
