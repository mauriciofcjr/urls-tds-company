package com.tds.urls_tds_company.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tds.urls_tds_company.model.EstatisticaVisita;

public interface EstatisticaVisitaRepository extends JpaRepository<EstatisticaVisita, Long>{

    Optional<EstatisticaVisita> findByShortUrlAndAccessDate(String shortUrl, LocalDate accessDate);
}
