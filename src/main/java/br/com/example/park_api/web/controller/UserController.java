package br.com.example.park_api.web.controller;

import br.com.example.park_api.entity.User;
import br.com.example.park_api.service.UserService;
import br.com.example.park_api.web.dto.UserCreateDto;
import br.com.example.park_api.web.dto.UserResponseDto;
import br.com.example.park_api.web.dto.mapper.UserMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/v1/users")
@RequiredArgsConstructor
public class UserController {
    @NonNull
    private final UserService userService;

    @PostMapping(value = "/save")
    public ResponseEntity<UserResponseDto> saveUser (@RequestBody UserCreateDto createDto) {
        User response = userService.save(UserMapper.toUser(createDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.toResponseDto(response));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<User> findById (@PathVariable Long id) {
        User response = userService.findById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<User>> findAll () {
        List<User> usersList = userService.findAll();
        return ResponseEntity.ok(usersList);
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<User> updatePassword (@PathVariable Long id, @RequestBody User user) {
        User response = userService.updatePassword(id, user.getPassword());
        return ResponseEntity.ok(response);
    }
}
