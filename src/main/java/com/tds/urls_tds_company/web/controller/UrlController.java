package com.tds.urls_tds_company.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tds.urls_tds_company.model.dto.UrlCreateDto;
import com.tds.urls_tds_company.model.dto.UrlResponseDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/url")
public class UrlController {

    @PostMapping
    public ResponseEntity<UrlResponseDto> createUrl(@RequestBody UrlCreateDto urlRequestDto) {
        UrlResponseDto urlResponseDto = new UrlResponseDto(urlRequestDto.url(), urlRequestDto.shortUrl());
        return ResponseEntity.status(HttpStatus.CREATED).body(urlResponseDto);
    }

}
