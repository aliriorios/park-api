package br.com.example.park_api.service;

import br.com.example.park_api.entity.User;
import br.com.example.park_api.entity.enums.Role;
import br.com.example.park_api.exception.EntityNotFoundException;
import br.com.example.park_api.exception.PasswordInvalidException;
import br.com.example.park_api.exception.UsernameUniqueViolationException;
import br.com.example.park_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User save(User user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword())); // Encrypting the password
            return userRepository.save(user);

        } catch (DataIntegrityViolationException e) {
            throw new UsernameUniqueViolationException(String.format("Username {%s} already registered.", user.getUsername()));
        }
    }

    @Transactional(readOnly = true)
    public User findById (Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("User {id=%s} not founded.", id))
        );
    }

    @Transactional(readOnly = true)
    public User findByUsername (String username) {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new EntityNotFoundException(String.format("User {username=%s} not founded.", username))
        );
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional
    public User updatePassword(Long id, String currentPassword, String newPassword, String confirmPassword) {
        if (!newPassword.equals(confirmPassword)) {
            throw new PasswordInvalidException("New password does not match password confirmation.");
        }

        User user = findById(id);
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new PasswordInvalidException("The password entered does not match the user's.");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        return user;

        /*
        * userRepository.save(user);
        * This is functional but unnecessary, Hibernate updates
        * */
    }

    @Transactional(readOnly = true)
    public Role findRoleByUsername(String username) {
        return userRepository.findRoleByUsername(username);
    }
}