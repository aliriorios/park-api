package br.com.example.park_api.service;

import br.com.example.park_api.entity.ClientParkingSpot;
import br.com.example.park_api.repository.ClientParkingSpotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClientParkingSpotService {
    private final ClientParkingSpotRepository repository;

    @Transactional
    public ClientParkingSpot save (ClientParkingSpot clientParkingSpot) {
        return repository.save(clientParkingSpot);
    }
}
