package br.com.example.park_api.service;

import br.com.example.park_api.entity.User;
import br.com.example.park_api.repository.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
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
        return userRepository.save(user);
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
    public User updatePassword(Long id, String password) {
        User user = findById(id);
        user.setPassword(password);
        return user;

        /*
        * userRepository.save(user);
        * This is functional but unnecessary, Hibernate updates
        * */
    }
}