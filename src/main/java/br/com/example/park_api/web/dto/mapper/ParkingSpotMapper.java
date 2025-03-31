package br.com.example.park_api.web.dto.mapper;

import br.com.example.park_api.entity.ParkingSpot;
import br.com.example.park_api.web.dto.ParkingSpotCreateDto;
import br.com.example.park_api.web.dto.ParkingSpotResponseDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ParkingSpotMapper {
    public static ParkingSpot toParkingSpot(ParkingSpotCreateDto createDto) {
        return new ModelMapper().map(createDto, ParkingSpot.class);
    }

    public static ParkingSpotResponseDto toResponseDto(ParkingSpot parkingSpot) {
        return new ModelMapper().map(parkingSpot, ParkingSpotResponseDto.class);
    }
}
