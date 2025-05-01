package com.tds.urls_tds_company.web.controller;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tds.urls_tds_company.exception.EntityNotFoundException;
import com.tds.urls_tds_company.model.Url;
import com.tds.urls_tds_company.model.dto.UrlCreateDto;
import com.tds.urls_tds_company.model.dto.UrlResponseDto;
import com.tds.urls_tds_company.model.dto.mapper.UrlMapper;
import com.tds.urls_tds_company.service.UrlService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

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
    public ResponseEntity<Void> urlOriginal(@PathVariable String shortUrl, HttpServletResponse response)
            throws IOException {
        Optional<Url> url = urlService.getOriginalUrl(shortUrl);

        if (url.isPresent()) {
            response.sendRedirect(url.get().getUrl());
            return ResponseEntity.ok().build();
        } else {
            throw new EntityNotFoundException(String.format("Url com atalho: %s não foi encontrado!", shortUrl));
        }

    }

    @GetMapping("/estatisticas/{shortUrl}")
    public ResponseEntity<Map<String, Integer>> getStatistics(@PathVariable String shortUrl) {
        return ResponseEntity.ok(urlService.getDailyAndTotalAccess(shortUrl));
    }

}
