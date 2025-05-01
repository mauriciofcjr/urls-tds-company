package com.tds.urls_tds_company.service;

import org.springframework.stereotype.Service;

import com.tds.urls_tds_company.exception.UrlUniqueViolationException;
import com.tds.urls_tds_company.model.Url;
import com.tds.urls_tds_company.repository.UrlRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UrlService {

    private final UrlRepository urlRepository;

    @Transactional
    public Url createUrl(Url url) {        
        try{
            return urlRepository.save(url);
        }catch (org.springframework.dao.DataIntegrityViolationException ex){
            throw new UrlUniqueViolationException(String.format("Recurso já cadastrado! Url: '%s', Atalho: '%s'", url.getUrl(), url.getShortUrl()));
        }
        
    }

}
