package com.tds.urls_tds_company.model.dto.mapper;

import org.modelmapper.ModelMapper;

import com.tds.urls_tds_company.model.Url;
import com.tds.urls_tds_company.model.dto.UrlCreateDto;
import com.tds.urls_tds_company.model.dto.UrlResponseDto;


public class UrlMapper {

    public static UrlResponseDto toDto(Url url) {        
        ModelMapper mapper = new ModelMapper();
        return mapper.map(url, UrlResponseDto.class);
    }

    public static Url toUrl(UrlCreateDto urlCreateDto) {
        ModelMapper mapper = new ModelMapper();
        return mapper.map(urlCreateDto, Url.class);
    }

}
