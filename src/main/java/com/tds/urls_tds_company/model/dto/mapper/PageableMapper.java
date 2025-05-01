package com.tds.urls_tds_company.model.dto.mapper;

import lombok.NoArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import com.tds.urls_tds_company.model.dto.PageableDto;

import lombok.AccessLevel;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PageableMapper {

    public static PageableDto toDto(Page page){
        return new ModelMapper().map(page, PageableDto.class);
    }
}
