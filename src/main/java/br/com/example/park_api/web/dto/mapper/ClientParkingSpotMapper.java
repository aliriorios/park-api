package br.com.example.park_api.web.dto.mapper;

import br.com.example.park_api.entity.ClientParkingSpot;
import br.com.example.park_api.web.dto.ParkingCreateDto;
import br.com.example.park_api.web.dto.ParkingResponseDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientParkingSpotMapper {
    public static ClientParkingSpot toClientParkingSpot(ParkingCreateDto createDto) {
        return new ModelMapper().map(createDto, ClientParkingSpot.class);
    }

    public static ParkingResponseDto toResponseDto(ClientParkingSpot clientParkingSpot) {
        return new ModelMapper().map(clientParkingSpot, ParkingResponseDto.class);
    }
}
