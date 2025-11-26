package br.com.nanooksolutions.importacaoservice.domain.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


@Getter
@Setter
@SuperBuilder
public class MovimentacaoDTO {

    private UUID id; // opcional
    private String tipoMovimentacao; // entrada / saida
    private BigDecimal valor;
    private String moeda; // BRL, USD, BTC
    private String categoria; // restaurante, salário, etc
    private String descricao; // texto cru do banco
    private LocalDate dataMovimentacao;
    private String instituicao; // Banco Inter, Nubank...
    private String responsavel; // Maria, João
    private String origem; // extrato_pdf, holerite_pdf, planilha, etc.
}

