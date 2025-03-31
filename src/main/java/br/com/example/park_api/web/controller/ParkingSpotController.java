package br.com.example.park_api.web.controller;

import br.com.example.park_api.entity.ParkingSpot;
import br.com.example.park_api.service.ParkingSpotService;
import br.com.example.park_api.web.dto.ClientResponseDto;
import br.com.example.park_api.web.dto.ParkingSpotCreateDto;
import br.com.example.park_api.web.dto.ParkingSpotResponseDto;
import br.com.example.park_api.web.dto.mapper.ParkingSpotMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.spi.ErrorMessage;
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
@Tag(name = "ParkingSpots", description = "Contains all operations related to the resource of a vacancy")
public class ParkingSpotController {
    private final ParkingSpotService parkingSpotService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Create a new parking spot", description = "Feature to create a new parking spot - Request requires the use of a Bearer JWT Token. Restricted ADMIN access",
            security = @SecurityRequirement(name = "Security"),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Successfully created resource", headers = @Header(name = HttpHeaders.LOCATION, description = "URL of the resource created")), // Header; Because of the return of the "save"
                    @ApiResponse(responseCode = "409", description = "Spot already registered", content = @Content(mediaType = "application/json;charset-UTF-8", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Resource not processed due to lack of data or invalid data", content = @Content(mediaType = "application/json;charset-UTF-8", schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
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
    @Operation(
            summary = "Find a parking spot", description = "Feature to return a parking spot by your code",
            security = @SecurityRequirement(name = "Security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully created resource", content = @Content(mediaType = "application/json;charset-UTF-8", schema = @Schema(implementation = ParkingSpotResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Parking spot not founded", content = @Content(mediaType = "application/json;charset-UTF-8", schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
    public ResponseEntity<ParkingSpotResponseDto> findByCode (@PathVariable String code) {
        ParkingSpot parkingSpot = parkingSpotService.findByCode(code);

        return ResponseEntity.status(HttpStatus.OK).body(ParkingSpotMapper.toResponseDto(parkingSpot));
    }
}
