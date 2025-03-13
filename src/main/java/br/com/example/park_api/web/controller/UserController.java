package br.com.example.park_api.web.controller;

import br.com.example.park_api.entity.User;
import br.com.example.park_api.service.UserService;
import br.com.example.park_api.web.dto.UserCreateDto;
import br.com.example.park_api.web.dto.UserResponseDto;
import br.com.example.park_api.web.dto.UserUpdatePasswordDto;
import br.com.example.park_api.web.dto.mapper.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.spi.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Contains all operations related to the resources for registering, editing and reading a user.")
public class UserController {
    @NonNull
    private final UserService userService;

    @PostMapping(value = "/save")
    @Operation(
            summary = "Create a new user", description = "Feature to create a new user",
            responses = {
                @ApiResponse(responseCode = "201", description = "Successfully created resource", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
                @ApiResponse(responseCode = "409", description = "User e-mail already registered in the system", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                @ApiResponse(responseCode = "422", description = "Resource not processed for invalid input data", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
    public ResponseEntity<UserResponseDto> saveUser (@Valid @RequestBody UserCreateDto dto) {
        User response = userService.save(UserMapper.toUser(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.toResponseDto(response));
    }

    @GetMapping(value = "/{id}")
    @Operation(
            summary = "Find a user by id", description = "Feature to find an existing user via id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User found successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "User not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
    public ResponseEntity<UserResponseDto> findById (@PathVariable Long id) {
        User response = userService.findById(id);
        return ResponseEntity.ok(UserMapper.toResponseDto(response));
    }

    @GetMapping
    @Operation(
            summary = "Search for all users", description = "Listing all system users",
            responses = {
                    @ApiResponse(responseCode = "200", description = "All users were found",
                            content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UserResponseDto.class))))
            }
    )
    public ResponseEntity<List<UserResponseDto>> findAll () {
        List<User> usersList = userService.findAll();

        return ResponseEntity.ok(UserMapper.toListResponseDto(usersList));
    }

    @PatchMapping(value = "/{id}")
    @Operation(
            summary = "Update password", description = "Update a user's password",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Password successfully updated", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))),
                    @ApiResponse(responseCode = "401", description = "Password entered does not match the database or confirmation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "404", description = "User not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Resource not processed for invalid input data", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
    public ResponseEntity<Void> updatePassword (@PathVariable Long id, @Valid @RequestBody UserUpdatePasswordDto dto) {
        User response = userService.updatePassword(id, dto.getCurrentPassword(), dto.getNewPassword(), dto.getConfirmPassword());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
