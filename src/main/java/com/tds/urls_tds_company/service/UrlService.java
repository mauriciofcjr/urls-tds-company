package com.tds.urls_tds_company.service;

import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.tds.urls_tds_company.exception.EntityNotFoundException;
import com.tds.urls_tds_company.exception.UrlUniqueViolationException;
import com.tds.urls_tds_company.model.EstatisticaVisita;
import com.tds.urls_tds_company.model.Url;
import com.tds.urls_tds_company.model.dto.UrlCreateDto;
import com.tds.urls_tds_company.model.dto.mapper.UrlMapper;
import com.tds.urls_tds_company.repository.EstatisticaVisitaRepository;
import com.tds.urls_tds_company.repository.UrlRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UrlService {

    private final UrlRepository urlRepository;
    private final EstatisticaVisitaRepository estatisticaVisitaRepository;

    @Transactional
    public Url createUrl(UrlCreateDto urlCreateDto) {
        try {
            Url newUrl = new Url();
            newUrl.setUrl(urlCreateDto.getUrl());
            newUrl.setShortUrl(generateShortUrl(urlCreateDto.getUrl()));
            return urlRepository.save(newUrl);
        } catch (org.springframework.dao.DataIntegrityViolationException ex) {
            throw new UrlUniqueViolationException("URl já cadastrada no sistema!");
        }

    }

    public Url findById(Long id) {

        return urlRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException(String.format("Url com id=%s não encontrado", id)));
    }

    public Optional<Url> getOriginalUrl(String shortUrl) {

        Optional<Url> url = urlRepository.findByShortUrl(shortUrl);
        if (url.isPresent()) {
            registerAccess(shortUrl);
        }
        return url;

    }

    private String generateShortUrl(String url) {
        String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        return uuid;
    }

    private void registerAccess(String shortUrl) {
        LocalDate today = LocalDate.now();
        Optional<EstatisticaVisita> acessosDiarios = estatisticaVisitaRepository.findByShortUrlAndAccessDate(shortUrl,
                today);
        if (acessosDiarios.isPresent()) {
            acessosDiarios.get().incrementAccessCount();
            estatisticaVisitaRepository.save(acessosDiarios.get());
        } else {
            EstatisticaVisita novoAcesso = new EstatisticaVisita(null, shortUrl, today, 1);
            estatisticaVisitaRepository.save(novoAcesso);
        }
    }

    public Map<String, Integer> getDailyAndTotalAccess(String shortUrl) {
        LocalDate date = LocalDate.now();
        List<Object[]> results = estatisticaVisitaRepository.findDailyAndTotalAccess(shortUrl, date);
        if (!results.isEmpty() && results.get(0) != null) {
            Integer totalAccess = ((Number) results.get(0)[0]).intValue();
            Integer dailyAccess = ((Number) results.get(0)[1]).intValue();

            return Map.of("acessosDiarios", dailyAccess, "acessoTotal", totalAccess);
        }
        return Map.of("acessosDiarios", 0, "acessoTotal", 0);
    }

}
