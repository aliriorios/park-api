package br.com.example.park_api.service;

import br.com.example.park_api.entity.User;
import br.com.example.park_api.exception.UsernameUniqueViolationException;
import br.com.example.park_api.repository.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    @NonNull
    private final UserRepository userRepository;

    @Transactional
    public User save(User user) {
        try {
            return userRepository.save(user);

        } catch (DataIntegrityViolationException e) {
            throw new UsernameUniqueViolationException(String.format("Username {%s} already registered.", user.getUsername()));
        }
    }

    @Transactional(readOnly = true)
    public User findById (Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new RuntimeException("User not founded.")
        );
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional
    public User updatePassword(Long id, String currentPassword, String newPassword, String confirmPassword) {
        if (!newPassword.equals(confirmPassword)) {
            throw new RuntimeException("New password does not match password confirmation.");
        }

        User user = findById(id);
        if (!user.getPassword().equals(currentPassword)) {
            throw new RuntimeException("The password entered does not match the user's.");
        }

        user.setPassword(newPassword);
        return user;

        /*
        * userRepository.save(user);
        * This is functional but unnecessary, Hibernate updates
        * */
    }
}