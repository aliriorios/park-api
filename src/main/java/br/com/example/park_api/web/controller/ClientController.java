package br.com.example.park_api.web.controller;

import br.com.example.park_api.entity.Client;
import br.com.example.park_api.jwt.JwtUserDetails;
import br.com.example.park_api.repository.projection.ClientProjection;
import br.com.example.park_api.service.ClientService;
import br.com.example.park_api.service.UserService;
import br.com.example.park_api.web.dto.ClientCreateDto;
import br.com.example.park_api.web.dto.ClientResponseDto;
import br.com.example.park_api.web.dto.PageableDto;
import br.com.example.park_api.web.dto.mapper.ClientMapper;
import br.com.example.park_api.web.dto.mapper.PageableMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.spi.ErrorMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/clients")
@RequiredArgsConstructor
@Tag(name = "Clients", description = "Contains all operations related to a client's resource")
public class ClientController {
    private final ClientService clientService;
    private final UserService userService;

    @PostMapping(value = "/save")
    @Operation(
            summary = "Create a new client", description = "Feature to create a new client linked to a registered user - Requisition requires Bearer Token. Restricted access to CLIENT",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Successfully created resource", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClientResponseDto.class))),
                    @ApiResponse(responseCode = "403", description = "Resource restricted to CLIENT", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "409", description = "CPF already registered in the system", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Resource not processed for invalid input data", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<ClientResponseDto> saveClient (@Valid @RequestBody ClientCreateDto dto, @AuthenticationPrincipal JwtUserDetails userDetails) {
        Client client = ClientMapper.toClient(dto);
        client.setUser(userService.findById(userDetails.getId())); // Entering the OneToOne Relationship

        clientService.save(client);
        return ResponseEntity.status(HttpStatus.CREATED).body(ClientMapper.toResponseDto(client));
    }

    @GetMapping(value = "/{id}")
    @Operation(
            summary = "Find a client by id", description = "Feature to find an existing client by id - Requisition requires a Bearer Token. Restricted access to ADMIN",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Client found successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClientResponseDto.class))),
                    @ApiResponse(responseCode = "403", description = "Resource restricted to ADMIN", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "404", description = "Client not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClientResponseDto> findById (@PathVariable Long id) {
        Client client = clientService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(ClientMapper.toResponseDto(client));
    }

    @GetMapping
    @Operation(
            summary = "Search for all clients", description = "Listing all system clients  - Requisition requires a Bearer Token. Restricted access to ADMIN",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Client found successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClientResponseDto.class))),
                    @ApiResponse(responseCode = "403", description = "Resource restricted to ADMIN", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageableDto> findAll(Pageable pageable) { // "Pageable" and "Page"   -> Pagination
        Page<ClientProjection> clientList = clientService.findAll(pageable);

        return ResponseEntity.status(HttpStatus.OK).body(PageableMapper.toPageableDto(clientList));
    }
}
