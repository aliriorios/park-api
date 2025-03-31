package br.com.example.park_api.repository;

import br.com.example.park_api.entity.ClientParkingSpot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientParkingSpotRepository extends JpaRepository<ClientParkingSpot, Long> {
}
