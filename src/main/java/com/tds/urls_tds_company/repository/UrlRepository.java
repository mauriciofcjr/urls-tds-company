package com.tds.urls_tds_company.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tds.urls_tds_company.model.Url;


public interface UrlRepository extends JpaRepository<Url, Long>{

    Optional<Url> findByUrl(String shortUrl);
}
