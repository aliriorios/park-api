package br.com.example.park_api.service;

import br.com.example.park_api.entity.ParkingSpot;
import br.com.example.park_api.exception.EntityNotFoundException;
import br.com.example.park_api.exception.ParkingSpotCodeUniqueViolationException;
import br.com.example.park_api.repository.ParkingSpotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ParkingSpotService {
    private final ParkingSpotRepository parkingSpotRepository;

    @Transactional
    public ParkingSpot save(ParkingSpot parkingSpot) {
        try {
            return parkingSpotRepository.save(parkingSpot);

        } catch (DataIntegrityViolationException e) {
            throw new ParkingSpotCodeUniqueViolationException(String.format("Parking Spot Code {%s} already registered", parkingSpot.getCode()));
        }
    }

    @Transactional(readOnly = true)
    public ParkingSpot findByCode(String code) {
        return parkingSpotRepository.findByCode(code).orElseThrow(
                () -> new EntityNotFoundException(String.format("Parking Spot {%s} not founded", code))
        );
    }
}
