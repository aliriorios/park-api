package br.com.example.park_api.repository;

import br.com.example.park_api.entity.ClientParkingSpot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientParkingSpotRepository extends JpaRepository<ClientParkingSpot, Long> {
    Optional<ClientParkingSpot> findByReceiptAndCheckOutIsNull(String receipt);

    long countByClientCpfAndCheckOutIsNotNull(String cpf);
}
