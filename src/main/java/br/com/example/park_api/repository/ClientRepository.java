package br.com.example.park_api.repository;

import br.com.example.park_api.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
