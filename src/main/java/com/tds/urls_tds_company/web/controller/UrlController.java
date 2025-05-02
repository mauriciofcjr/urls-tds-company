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
import com.tds.urls_tds_company.web.exception.ErrorMessage;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Content;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Urls", description = "Contém todas as operações relativas aos recursos para cadastro, leitura e estatisticas de uma URL.")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/url")
public class UrlController {

    private final UrlService urlService;

    @Operation(summary = "Criar um novo usuário", description = "Recurso para criar um novo usuário", responses = {
            @ApiResponse(responseCode = "201", description = "Recurso criado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UrlCreateDto.class))),
            @ApiResponse(responseCode = "409", description = "URl já cadastrada no sistema", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "422", description = "Recurso não processado por dados de entrada invalidos", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PostMapping
    public ResponseEntity<String> createUrl(@Valid @RequestBody UrlCreateDto urlCreateDto) {
        Url url = urlService.createUrl(urlCreateDto);
        UrlResponseDto urlResponseDto = UrlMapper.toDto(url);
        return ResponseEntity.status(HttpStatus.CREATED).body(urlResponseDto.getShortUrl());
    }

    @Operation(summary = "Recuperar uma URL pelo ID", description = "Recurso para recuperar uma URL pelo ID", responses = {
            @ApiResponse(responseCode = "200", description = "Recurso entrado com sucesso", content = @Content(mediaType = " application/json;charset=UTF-8", schema = @Schema(implementation = UrlResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "URl não encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<UrlResponseDto> getById(@PathVariable Long id) {
        Url url = urlService.findById(id);
        return ResponseEntity.ok(UrlMapper.toDto(url));
    }

    @Operation(summary = "Recuperar uma URL pelo seu shorUrl", description = "Recurso para recuperar uma URL pelo shorUrl", responses = {
            @ApiResponse(responseCode = "200", description = "Recurso entrado com sucesso", content = @Content(mediaType = " application/json;charset=UTF-8")),
            @ApiResponse(responseCode = "404", description = "URl não encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    @GetMapping("/original/{shortUrl}")
    public ResponseEntity<Void> urlOriginal(@PathVariable String shortUrl, HttpServletResponse response)
            throws IOException {
        Optional<Url> url = urlService.getOriginalUrl(shortUrl);

        if (url.isPresent()) {
            response.sendRedirect(url.get().getUrl());
            return ResponseEntity.status(302).build();
        } else {
            throw new EntityNotFoundException(String.format("Url com atalho: %s não foi encontrado!", shortUrl));
        }

    }

    @Operation(summary = "Recupera e tras estatistica sobre acessos a uma shorUrl", description = "Recupera e tras estatistica sobre acessos a uma shorUrl", responses = {
            @ApiResponse(responseCode = "200", description = "Recurso entrado com sucesso", content = @Content(mediaType = " application/json;charset=UTF-8")),
            @ApiResponse(responseCode = "404", description = "URl não encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    @GetMapping("/estatisticas/{shortUrl}")
    public ResponseEntity<Map<String, Integer>> getStatistics(@PathVariable String shortUrl) {
        return ResponseEntity.ok(urlService.getDailyAndTotalAccess(shortUrl));
    }

}
