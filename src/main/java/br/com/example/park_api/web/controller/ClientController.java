package br.com.example.park_api.web.controller;

import br.com.example.park_api.entity.Client;
import br.com.example.park_api.jwt.JwtUserDetails;
import br.com.example.park_api.service.ClientService;
import br.com.example.park_api.service.UserService;
import br.com.example.park_api.web.dto.ClientCreateDto;
import br.com.example.park_api.web.dto.ClientResponseDto;
import br.com.example.park_api.web.dto.mapper.ClientMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/clients")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;
    private final UserService userService;

    @PostMapping(value = "/save")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<ClientResponseDto> saveClient (@Valid @RequestBody ClientCreateDto dto, @AuthenticationPrincipal JwtUserDetails userDetails) {
        Client client = ClientMapper.toClient(dto);
        client.setUser(userService.findById(userDetails.getId())); // Entering the OneToOne Relationship

        clientService.save(client);
        return ResponseEntity.status(HttpStatus.CREATED).body(ClientMapper.toResponseDto(client));
    }
}
