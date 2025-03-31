package br.com.example.park_api.repository;

import br.com.example.park_api.entity.Client;
import br.com.example.park_api.repository.projection.ClientProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    @Query("select c from Client c")
    Page<ClientProjection> findAllPageable(Pageable pageable);

    Client findByUserId(Long id);

    Optional<Client> findByCpf(String cpf);
}
