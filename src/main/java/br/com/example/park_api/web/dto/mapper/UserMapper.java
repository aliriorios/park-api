package br.com.example.park_api.web.dto.mapper;

import br.com.example.park_api.entity.User;
import br.com.example.park_api.web.dto.UserCreateDto;
import br.com.example.park_api.web.dto.UserResponseDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {

    public static User toUser(UserCreateDto createDto) {
        return new ModelMapper().map(createDto, User.class);
    }

    public static UserResponseDto toResponseDto(User user) {
        String role = user.getRole().name().substring("ROLE_".length());
        
        ModelMapper mapperMain = new ModelMapper();
        TypeMap<User, UserResponseDto> propertyMapper = mapperMain.createTypeMap(User.class, UserResponseDto.class);

        propertyMapper.addMappings(
                mapper -> mapper.map(src -> role, UserResponseDto::setRole)
        );

        return mapperMain.map(user, UserResponseDto.class);
    }

    public static List<UserResponseDto> toListResponseDto(List<User> users) {
        return users.stream().map(UserMapper::toResponseDto).collect(Collectors.toList());
    }
}
