package br.com.nanooksolutions.importacaoservice.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.nanooksolutions.importacaoservice.domain.dto.MovimentacaoDTO;
import br.com.nanooksolutions.importacaoservice.service.ImportacaoService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/pdf")
@RequiredArgsConstructor
public class PDFController {

    private final ImportacaoService importacaoService;

    @PostMapping("/extrato")
    public ResponseEntity<?> importExtratoBancario(@RequestParam MultipartFile extrato) {
        try {
            
            return ResponseEntity.ok(importacaoService.processarPdfExtrato(extrato));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao processar PDF: " + e.getMessage());
        }
    }
}