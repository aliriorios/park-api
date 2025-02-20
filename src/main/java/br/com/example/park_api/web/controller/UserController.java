package br.com.example.park_api.web.controller;

import br.com.example.park_api.entity.User;
import br.com.example.park_api.service.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/v1/users")
@RequiredArgsConstructor
public class UserController {
    @NonNull
    private final UserService userService;

    @PostMapping(value = "/save")
    public ResponseEntity<User> saveUser (@RequestBody User user) {
        User response = userService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<User> findById (@PathVariable Long id) {
        User response = userService.findById(id);
        return ResponseEntity.ok(response);
    }
}
