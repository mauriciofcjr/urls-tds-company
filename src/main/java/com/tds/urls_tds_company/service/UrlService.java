package com.tds.urls_tds_company.service;

import java.util.Base64;

import org.springframework.stereotype.Service;

import com.tds.urls_tds_company.exception.EntityNotFoundException;
import com.tds.urls_tds_company.exception.UrlUniqueViolationException;
import com.tds.urls_tds_company.model.Url;
import com.tds.urls_tds_company.model.dto.UrlCreateDto;
import com.tds.urls_tds_company.model.dto.mapper.UrlMapper;
import com.tds.urls_tds_company.repository.UrlRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UrlService {

    private final UrlRepository urlRepository;

    @Transactional
    public Url createUrl(UrlCreateDto urlCreateDto) {
        try {
            urlCreateDto.setShortUrl(generateShortUrl(urlCreateDto.getUrl()));
            return urlRepository.save(UrlMapper.toUrl(urlCreateDto));
        } catch (org.springframework.dao.DataIntegrityViolationException ex) {
            throw new UrlUniqueViolationException(
                    String.format("Recurso já cadastrado! Url: s% ou Atalho: s%", urlCreateDto.getUrl(),
                            urlCreateDto.getShortUrl()));
        }

    }

    public Url findById(Long id) {

        return urlRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException(String.format("Url com id=%s não encontrado", id)));
    }

    public Url getOriginalUrl(String shortUrl) {

        return urlRepository.findByShortUrl(shortUrl)
                .orElseThrow(
                        () -> new EntityNotFoundException(String.format("Url com atalho=%s não encontrado", shortUrl)));
    }

    private String generateShortUrl(String url) {
        return Base64.getUrlEncoder().encodeToString(url.getBytes()).substring(0, 8);
    }

}
