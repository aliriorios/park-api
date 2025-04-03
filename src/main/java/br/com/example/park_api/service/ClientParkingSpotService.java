package br.com.example.park_api.service;

import br.com.example.park_api.entity.ClientParkingSpot;
import br.com.example.park_api.exception.EntityNotFoundException;
import br.com.example.park_api.repository.ClientParkingSpotRepository;
import br.com.example.park_api.repository.projection.ClientParkingSpotProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClientParkingSpotService {
    private final ClientService clientService;
    private final ClientParkingSpotRepository repository;

    @Transactional
    public ClientParkingSpot save (ClientParkingSpot clientParkingSpot) {
        return repository.save(clientParkingSpot);
    }

    @Transactional(readOnly = true)
    public ClientParkingSpot findByReceipt(String receipt) {
        return repository.findByReceiptAndCheckOutIsNull(receipt).orElseThrow(
                () -> new EntityNotFoundException(String.format("Receipt {%s} not found in the system, or has already been checked out", receipt))
        );
    }

    @Transactional
    public long getTotalCountCompleteParking(String cpf) {
        return repository.countByClientCpfAndCheckOutIsNotNull(cpf);
    }

    @Transactional(readOnly = true)
    public Page<ClientParkingSpotProjection> findAllByClientCpf(String cpf, Pageable pageable) {
        return repository.findAllByClientCpf(clientService.findByCpf(cpf).getCpf(), pageable);
    }
}
