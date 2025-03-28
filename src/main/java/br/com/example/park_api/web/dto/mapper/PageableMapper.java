package br.com.example.park_api.web.dto.mapper;

import br.com.example.park_api.web.dto.PageableDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PageableMapper {
    public static PageableDto toPageableDto (Page page) {
        return new ModelMapper().map(page, PageableDto.class);
    }
}
