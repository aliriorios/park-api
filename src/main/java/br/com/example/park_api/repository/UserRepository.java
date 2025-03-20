package br.com.example.park_api.repository;

import br.com.example.park_api.entity.User;
import br.com.example.park_api.entity.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    // JPQL from JPA custom method
    // JPQL != SQL query
    // SQL: SELECT role FROM users WHERE username LIKE 'ana@gmail.com';
    @Query("select u.role from User u where u.username like :username")
    Role findRoleByUsername(String username);
}