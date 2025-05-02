package com.tds.urls_tds_company.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tds.urls_tds_company.model.EstatisticaVisita;

public interface EstatisticaVisitaRepository extends JpaRepository<EstatisticaVisita, Long>{

    Optional<EstatisticaVisita> findByShortUrlAndAccessDate(String shortUrl, LocalDate accessDate);

    @Query("SELECT " +
           "COALESCE(SUM(d.accessCount), 0) AS totalAccess, " +
           "COALESCE(SUM(CASE WHEN d.accessDate = :date THEN d.accessCount ELSE 0 END), 0) AS dailyAccess " +
           "FROM EstatisticaVisita d WHERE d.shortUrl = :shortUrl")
    List<Object[]> findDailyAndTotalAccess(@Param("shortUrl") String shortUrl, @Param("date") LocalDate date);
}
