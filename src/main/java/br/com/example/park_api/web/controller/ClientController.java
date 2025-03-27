package br.com.example.park_api.web.controller;

import br.com.example.park_api.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/clients")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;
}
