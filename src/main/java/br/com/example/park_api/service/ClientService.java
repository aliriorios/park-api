package br.com.example.park_api.service;

import br.com.example.park_api.entity.Client;
import br.com.example.park_api.exception.CpfUniqueViolationException;
import br.com.example.park_api.exception.EntityNotFoundException;
import br.com.example.park_api.repository.ClientRepository;
import br.com.example.park_api.repository.projection.ClientProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Transactional(readOnly = true)
    public Client findById(Long id) {
        return clientRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Client {id=%s} not founded.", id))
        );
    }

    @Transactional(readOnly = true)
    public Page<ClientProjection> findAll(Pageable pageable) {
        return clientRepository.findAllPageable(pageable);
    }

    @Transactional(readOnly = true)
    public Client findByUserId(Long id) {
        return clientRepository.findByUserId(id);
    }
}
