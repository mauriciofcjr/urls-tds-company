package com.tds.urls_tds_company.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tds.urls_tds_company.model.Url;
import com.tds.urls_tds_company.model.dto.UrlCreateDto;
import com.tds.urls_tds_company.model.dto.UrlResponseDto;
import com.tds.urls_tds_company.model.dto.mapper.UrlMapper;
import com.tds.urls_tds_company.service.UrlService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/url")
public class UrlController {

    private final UrlService urlService;

    @PostMapping
    public ResponseEntity<UrlResponseDto> createUrl(@Valid @RequestBody UrlCreateDto urlCreateDto) {
        Url url = urlService.createUrl(UrlMapper.toUrl(urlCreateDto));
        UrlResponseDto urlResponseDto = UrlMapper.toDto(url);
        return ResponseEntity.status(HttpStatus.CREATED).body(urlResponseDto);
    }

}
