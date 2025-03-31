package br.com.example.park_api.repository;

import br.com.example.park_api.entity.ParkingSpot;
import br.com.example.park_api.entity.enums.SpotStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParkingSpotRepository extends JpaRepository<ParkingSpot, Long> {

    Optional<ParkingSpot> findByCode(String code);

    Optional<ParkingSpot> findFirstByStatus(SpotStatus spotStatus);
}
