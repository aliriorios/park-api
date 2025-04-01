package br.com.example.park_api.web.controller;

import br.com.example.park_api.entity.ClientParkingSpot;
import br.com.example.park_api.service.ParkingService;
import br.com.example.park_api.web.dto.ParkingCreateDto;
import br.com.example.park_api.web.dto.ParkingResponseDto;
import br.com.example.park_api.web.dto.mapper.ClientParkingSpotMapper;
import br.com.example.park_api.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
@Tag(name = "Parking", description = "Registration operations for entering and exiting a vehicle from the parking lot")
public class ParkingController {
    private final ParkingService parkingService;

    @PostMapping(value = "/check-in")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Check-in operation", description = "Feature to enter a vehicle in the parking lot - Request requires the use of a Bearer JWT Token. Restricted ADMIN access",
            security = @SecurityRequirement(name = "Security"),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Successfully created resource", headers = @Header(name = HttpHeaders.LOCATION, description = "Access URL to the created resource"), content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ParkingResponseDto.class))),
                    @ApiResponse(responseCode = "403", description = "Resource restrict to ADMIN", content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "404", description = "Causes:<br/>" + "- Client CPF not registered in the system<br/>" + "- No free parking spot were located", content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Resource not processed due to lack of data or invalid data", content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
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
