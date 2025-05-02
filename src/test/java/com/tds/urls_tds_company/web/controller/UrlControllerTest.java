package com.tds.urls_tds_company.web.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tds.urls_tds_company.exception.EntityNotFoundException;
import com.tds.urls_tds_company.exception.UrlUniqueViolationException;
import com.tds.urls_tds_company.model.Url;
import com.tds.urls_tds_company.model.dto.UrlCreateDto;
import com.tds.urls_tds_company.service.UrlService;

@WebMvcTest(UrlController.class)
public class UrlControllerTest {   

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UrlService urlService;

    private Url url;
    
    private UrlCreateDto urlCreateDto;

    @BeforeEach
    void setUp() {
        url = new Url(1L, "https://www.google.com", "abc123");
        urlCreateDto = new UrlCreateDto("https://www.google.com");
    }

    @Test
    void createUrl_WithValidData_ReturnsCreated() throws Exception {
        when(urlService.createUrl(any(UrlCreateDto.class))).thenReturn(url);

        mockMvc.perform(post("/api/v1/url")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(urlCreateDto)))
                .andExpect(status().isCreated())
                .andExpect(content().string(url.getShortUrl()));
    }

    @Test
    void createUrl_WithInvalidData_ReturnsBadRequest() throws Exception {
        UrlCreateDto invalidDto = new UrlCreateDto("");

        mockMvc.perform(post("/api/v1/url")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void createUrl_WithDuplicateUrl_ReturnsConflict() throws Exception {
        when(urlService.createUrl(any(UrlCreateDto.class)))
                .thenThrow(new UrlUniqueViolationException("URl já cadastrada no sistema!"));

        mockMvc.perform(post("/api/v1/url")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(urlCreateDto)))
                .andExpect(status().isConflict());
    }

    @Test
    void getById_WithValidId_ReturnsUrl() throws Exception {
        when(urlService.findById(1L)).thenReturn(url);

        mockMvc.perform(get("/api/v1/url/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.url").value("https://www.google.com"))
                .andExpect(jsonPath("$.shortUrl").value("abc123"));
    }

    @Test
    void getById_WithInvalidId_ReturnsNotFound() throws Exception {
        when(urlService.findById(99L))
                .thenThrow(new EntityNotFoundException("Url com id=99 não encontrado"));

        mockMvc.perform(get("/api/v1/url/{id}", 99L))
                .andExpect(status().isNotFound());
    }

    @Test
    void getOriginalUrl_WithValidShortUrl_RedirectsToOriginalUrl() throws Exception {
        when(urlService.getOriginalUrl("abc123")).thenReturn(Optional.of(url));

        mockMvc.perform(get("/api/v1/url/original/{shortUrl}", "abc123"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void getOriginalUrl_WithInvalidShortUrl_ReturnsNotFound() throws Exception {
        when(urlService.getOriginalUrl("invalid"))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/url/original/{shortUrl}", "invalid"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getStatistics_WithValidShortUrl_ReturnsStatistics() throws Exception {
        Map<String, Integer> statistics = new HashMap<>();
        statistics.put("acessosDiarios", 5);
        statistics.put("acessoTotal", 10);

        when(urlService.getDailyAndTotalAccess("abc123")).thenReturn(statistics);

        mockMvc.perform(get("/api/v1/url/estatisticas/{shortUrl}", "abc123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.acessosDiarios").value(5))
                .andExpect(jsonPath("$.acessoTotal").value(10));
    }

    @Test
    void getStatistics_WithInvalidShortUrl_ReturnsEmptyStatistics() throws Exception {
        Map<String, Integer> emptyStatistics = new HashMap<>();
        emptyStatistics.put("acessosDiarios", 0);
        emptyStatistics.put("acessoTotal", 0);

        when(urlService.getDailyAndTotalAccess("invalid")).thenReturn(emptyStatistics);

        mockMvc.perform(get("/api/v1/url/estatisticas/{shortUrl}", "invalid"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.acessosDiarios").value(0))
                .andExpect(jsonPath("$.acessoTotal").value(0));
    }
  
}
