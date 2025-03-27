package br.com.example.park_api.web.controller;

import br.com.example.park_api.jwt.JwtToken;
import br.com.example.park_api.jwt.JwtUserDetailsService;
import br.com.example.park_api.web.dto.UserLoginDto;
import br.com.example.park_api.web.dto.UserResponseDto;
import br.com.example.park_api.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Authentication", description = "Feature to proceed with API authentication")
public class AuthenticationController {
    private final JwtUserDetailsService detailsService;
    private final AuthenticationManager authenticationManager;

    @PostMapping(value = "/auth")
    @Operation(
            summary = "Authentication", description = "Authentication feature in the API",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Authentication done successfully, and return of a Bearer JWT Token", content = @Content(mediaType = "application/json", schema = @Schema(implementation = JwtToken.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid credentials", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Invalid fields", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
    public ResponseEntity<?> authenticate(@Valid @RequestBody UserLoginDto dto, HttpServletRequest request){
        log.info(String.format("Login authentication process {%s}", dto.getUsername()));

        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword());

            authenticationManager.authenticate(authenticationToken);

            JwtToken token = detailsService.getTokenAuthenticated(dto.getUsername());
            return ResponseEntity.ok(token);

        } catch (AuthenticationException e) {
            log.warn(String.format("Bad credentials from username {%s}", dto.getUsername()));
        }
        return ResponseEntity
                .badRequest()
                .body(new ErrorMessage(request, HttpStatus.BAD_REQUEST, "Invalid credentials."));
    }
}
