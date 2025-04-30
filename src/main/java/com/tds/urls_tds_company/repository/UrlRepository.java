package com.tds.urls_tds_company.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tds.urls_tds_company.model.Url;

@Repository
public interface UrlRepository extends JpaRepository<Url, Long>{

    Optional<Url> findByUrl(String shortUrl);
}
