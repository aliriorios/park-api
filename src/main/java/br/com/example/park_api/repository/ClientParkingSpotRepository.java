package br.com.example.park_api.repository;

import br.com.example.park_api.entity.ClientParkingSpot;
import br.com.example.park_api.repository.projection.ClientParkingSpotProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientParkingSpotRepository extends JpaRepository<ClientParkingSpot, Long> {
    Optional<ClientParkingSpot> findByReceiptAndCheckOutIsNull(String receipt);

    long countByClientCpfAndCheckOutIsNotNull(String cpf);

    Page<ClientParkingSpotProjection> findAllByClientCpf(String cpf, Pageable pageable);
}
