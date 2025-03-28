package br.com.example.park_api.web.dto.mapper;

import br.com.example.park_api.entity.Client;
import br.com.example.park_api.web.dto.ClientCreateDto;
import br.com.example.park_api.web.dto.ClientResponseDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientMapper {
    public static Client toClient(ClientCreateDto createDto) {
        return new ModelMapper().map(createDto, Client.class);
    }

    public static ClientResponseDto toResponseDto(Client client) {
        return new ModelMapper().map(client, ClientResponseDto.class);
    }

    public static List<ClientResponseDto> toListResponseDto(List<Client> clients) {
        return clients.stream().map(ClientMapper::toResponseDto).collect(Collectors.toList());
    }
}
