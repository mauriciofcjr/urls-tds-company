package com.tds.urls_tds_company.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import com.tds.urls_tds_company.exception.EntityNotFoundException;
import com.tds.urls_tds_company.exception.UrlUniqueViolationException;
import com.tds.urls_tds_company.model.EstatisticaVisita;
import com.tds.urls_tds_company.model.Url;
import com.tds.urls_tds_company.model.dto.UrlCreateDto;
import com.tds.urls_tds_company.repository.EstatisticaVisitaRepository;
import com.tds.urls_tds_company.repository.UrlRepository;

@ExtendWith(MockitoExtension.class)
public class UrlServiceTest {

    @Mock
    private UrlRepository urlRepository;

    @Mock
    private EstatisticaVisitaRepository estatisticaVisitaRepository;

    @InjectMocks
    private UrlService urlService;

    private Url url;
    private UrlCreateDto urlCreateDto;
    private EstatisticaVisita estatisticaVisita;
    private LocalDate today;

    @BeforeEach
    void setUp() {
        url = new Url(1L, "https://www.google.com", "abc123");
        urlCreateDto = new UrlCreateDto("https://www.google.com");
        today = LocalDate.now();
        estatisticaVisita = new EstatisticaVisita(1L, "abc123", today, 1);
    }

    @Test
    void createUrl_WithValidData_ReturnsNewUrl() {
        when(urlRepository.save(any(Url.class))).thenReturn(url);

        Url createdUrl = urlService.createUrl(urlCreateDto);

        assertThat(createdUrl).isNotNull();
        assertThat(createdUrl.getUrl()).isEqualTo(urlCreateDto.getUrl());
        assertThat(createdUrl.getShortUrl()).isNotNull();
        verify(urlRepository).save(any(Url.class));
    }

    @Test
    void createUrl_WithDuplicateUrl_ThrowsException() {
        when(urlRepository.save(any(Url.class))).thenThrow(DataIntegrityViolationException.class);

        assertThatThrownBy(() -> urlService.createUrl(urlCreateDto))
                .isInstanceOf(UrlUniqueViolationException.class)
                .hasMessage("URl já cadastrada no sistema!");
    }

    @Test
    void findById_WithValidId_ReturnsUrl() {
        when(urlRepository.findById(1L)).thenReturn(Optional.of(url));

        Url foundUrl = urlService.findById(1L);

        assertThat(foundUrl).isNotNull();
        assertThat(foundUrl).isEqualTo(url);
    }

    @Test
    void findById_WithInvalidId_ThrowsException() {
        when(urlRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> urlService.findById(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Url com id=99 não encontrado");
    }

    @Test
    void getOriginalUrl_WithValidShortUrl_ReturnsUrlAndRegistersAccess() {
        when(urlRepository.findByShortUrl("abc123")).thenReturn(Optional.of(url));
        when(estatisticaVisitaRepository.findByShortUrlAndAccessDate("abc123", today))
                .thenReturn(Optional.of(estatisticaVisita));

        Optional<Url> result = urlService.getOriginalUrl("abc123");

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(url);
        verify(estatisticaVisitaRepository).save(any(EstatisticaVisita.class));
    }

    @Test
    void getOriginalUrl_WithValidShortUrl_CreatesNewStatistics() {
        when(urlRepository.findByShortUrl("abc123")).thenReturn(Optional.of(url));
        when(estatisticaVisitaRepository.findByShortUrlAndAccessDate("abc123", today))
                .thenReturn(Optional.empty());

        Optional<Url> result = urlService.getOriginalUrl("abc123");

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(url);
        verify(estatisticaVisitaRepository).save(any(EstatisticaVisita.class));
    }

    @Test
    void getOriginalUrl_WithInvalidShortUrl_ReturnsEmpty() {
        when(urlRepository.findByShortUrl("invalid")).thenReturn(Optional.empty());

        Optional<Url> result = urlService.getOriginalUrl("invalid");

        assertThat(result).isEmpty();
        verify(estatisticaVisitaRepository, never()).save(any());
    }
 

    @Test
    void getDailyAndTotalAccess_WithNoStatistics_ReturnsZeros() {
        when(estatisticaVisitaRepository.findDailyAndTotalAccess("abc123", today))
                .thenReturn(List.of());

        Map<String, Integer> statistics = urlService.getDailyAndTotalAccess("abc123");

        assertThat(statistics)
                .containsEntry("acessoTotal", 0)
                .containsEntry("acessosDiarios", 0);
    }

}
