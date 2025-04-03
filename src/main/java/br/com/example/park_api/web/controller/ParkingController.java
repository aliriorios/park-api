package br.com.example.park_api.web.controller;

import br.com.example.park_api.entity.ClientParkingSpot;
import br.com.example.park_api.repository.projection.ClientParkingSpotProjection;
import br.com.example.park_api.service.ClientParkingSpotService;
import br.com.example.park_api.service.ParkingService;
import br.com.example.park_api.web.dto.PageableDto;
import br.com.example.park_api.web.dto.ParkingCreateDto;
import br.com.example.park_api.web.dto.ParkingResponseDto;
import br.com.example.park_api.web.dto.mapper.ClientParkingSpotMapper;
import br.com.example.park_api.web.dto.mapper.PageableMapper;
import br.com.example.park_api.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.PATH;
import static io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY;

@RestController
@RequestMapping(value = "/api/v1/parking")
@RequiredArgsConstructor
@Tag(name = "Parking", description = "Registration operations for entering and exiting a vehicle from the parking lot")
public class ParkingController {
    private final ParkingService parkingService;
    private final ClientParkingSpotService clientParkingSpotService;

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

    @GetMapping(value = "/check-in/{receipt}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
    @Operation(
            summary = "Check-in operation", description = "Feature to find a check-in record- Request requires the use of a Bearer JWT Token. Restricted ADMIN and CLIENT access",
            security = @SecurityRequirement(name = "Security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully founded", content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ParkingResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Non-existent receipt number", content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
    public ResponseEntity<ParkingResponseDto> findByReceipt(@PathVariable String receipt) {
        ClientParkingSpot clientParkingSpot = clientParkingSpotService.findByReceipt(receipt);
        ParkingResponseDto dto = ClientParkingSpotMapper.toResponseDto(clientParkingSpot);

        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @PutMapping(value = "/check-out/{receipt}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(
            summary = "Check-out operation", description = "Feature to exit a vehicle from the parking lot - Request requires the use of a Bearer JWT Token. Restricted ADMIN access",
            security = @SecurityRequirement(name = "Security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully updated feature", content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ParkingResponseDto.class))),
                    @ApiResponse(responseCode = "403", description = "Resource restrict to ADMIN", content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "404", description = "Causes:<br/>" + "- Non-existent receipt number<br/>" + "- The vehicle has already passed check-out", content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
    public ResponseEntity<ParkingResponseDto> checkOut(@PathVariable String receipt) {
        ClientParkingSpot clientParkingSpot = parkingService.checkOut(receipt);

        ParkingResponseDto dto = ClientParkingSpotMapper.toResponseDto(clientParkingSpot);

        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @GetMapping(value = "/cpf/{cpf}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Find client parking records by CPF", description = "Find all client parking records by CPF - Request requires the use of a Bearer JWT Token. Restricted ADMIN access",
            security = @SecurityRequirement(name = "Security"),
            parameters = {
                    @Parameter(in = PATH, name = "cpf", description = "CPF number for the client to be consulted", required = true),
                    @Parameter(in = QUERY, name = "page", description = "Represents the page returned", content = @Content(schema = @Schema(type = "integer", defaultValue = "0"))),
                    @Parameter(in = QUERY, name = "size", description = "Represents the total number of elements per page", content = @Content(schema = @Schema(type = "integer", defaultValue = "5"))),
                    @Parameter(in = QUERY, name = "sort", description = "Default sorting field 'checkIn.asc'", array = @ArraySchema(schema = @Schema(type = "string", defaultValue = "checkIn.asc")), hidden = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Resource successfully found", content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = PageableDto.class))),
                    @ApiResponse(responseCode = "403", description = "Resource restrict to ADMIN", content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "404", description = "Client CPF does not exist in the system", content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
    public ResponseEntity<PageableDto> findAllParkingByCpf(@PathVariable String cpf, @Parameter(hidden = true) @PageableDefault(size = 5, sort = "checkIn", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<ClientParkingSpotProjection> projection = clientParkingSpotService.findAllByClientCpf(cpf, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(PageableMapper.toPageableDto(projection));
    }
}
