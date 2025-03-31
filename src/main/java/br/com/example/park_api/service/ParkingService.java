package br.com.example.park_api.service;

import br.com.example.park_api.entity.Client;
import br.com.example.park_api.entity.ClientParkingSpot;
import br.com.example.park_api.entity.ParkingSpot;
import br.com.example.park_api.util.ParkingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static br.com.example.park_api.entity.enums.SpotStatus.OCCUPIED;

@Service
@RequiredArgsConstructor
public class ParkingService {
    private final ClientService clientService;
    private final ParkingSpotService parkingSpotService;
    private final ClientParkingSpotService clientParkingSpotService;

    @Transactional
    public ClientParkingSpot checkIn(ClientParkingSpot clientParkingSpot) {
        Client client = clientService.findByCpf(clientParkingSpot.getClient().getCpf()); // Retrieves the complete object from the Client through the one obtained by the CPF
        clientParkingSpot.setClient(client); // Before it only contained the CPF, now it has all the data

        ParkingSpot parkingSpot = parkingSpotService.findByFreeSpot();
        parkingSpot.setStatus(OCCUPIED);
        clientParkingSpot.setParkingSpot(parkingSpot);

        clientParkingSpot.setCheckIn(LocalDateTime.now());

        clientParkingSpot.setReceipt(ParkingUtils.generateReceipt());

        return clientParkingSpotService.save(clientParkingSpot);
    }
}
