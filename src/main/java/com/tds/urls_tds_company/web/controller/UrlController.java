package com.tds.urls_tds_company.web.controller;

import java.io.IOException;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tds.urls_tds_company.model.Url;
import com.tds.urls_tds_company.model.dto.UrlCreateDto;
import com.tds.urls_tds_company.model.dto.UrlResponseDto;
import com.tds.urls_tds_company.model.dto.mapper.UrlMapper;
import com.tds.urls_tds_company.service.UrlService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;


@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/url")
public class UrlController {

    private final UrlService urlService;

    @PostMapping
    public ResponseEntity<String> createUrl(@Valid @RequestBody UrlCreateDto urlCreateDto) {
        Url url = urlService.createUrl(urlCreateDto);
        UrlResponseDto urlResponseDto = UrlMapper.toDto(url);
        return ResponseEntity.status(HttpStatus.CREATED).body(urlResponseDto.getShortUrl());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UrlResponseDto> getById(@PathVariable Long id) {
        Url url = urlService.findById(id);
        return ResponseEntity.ok(UrlMapper.toDto(url));
    }

    @GetMapping("/original/{shortUrl}")
    public ResponseEntity<Void> urlOriginal(@PathVariable String shortUrl, HttpServletResponse response) throws IOException{
        Url url = urlService.getOriginalUrl(shortUrl);
        if(!url.getUrl().isEmpty()){
            response.sendRedirect(url.getUrl());
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
        
        
    }

    @GetMapping("/estatisticas")
    public String getMethodName(@RequestParam String param) {
        return new String();
    }
    

}
