package br.com.example.park_api.service;

import br.com.example.park_api.entity.Client;
import br.com.example.park_api.exception.CpfUniqueViolationException;
import br.com.example.park_api.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;

    @Transactional
    public Client save (Client client) {
        try {
            return clientRepository.save(client);

        } catch (DataIntegrityViolationException e) {
            throw new CpfUniqueViolationException(String.format("CPF {%s} is already registered in the system", client.getCpf()));
        }
    }
}
