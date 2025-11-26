package br.com.nanooksolutions.importacaoservice.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.nanooksolutions.importacaoservice.domain.dto.MovimentacaoDTO;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ImportacaoService {

    public List<MovimentacaoDTO> processarPdfExtrato(MultipartFile arquivo) throws IOException {
        byte[] bytes = arquivo.getBytes();
        try (PDDocument document = PDDocument.load(bytes)) {
            PDFTextStripper stripper = new PDFTextStripper();
            String texto = stripper.getText(document);

            String tipoDeBanco;
            if (texto.contains("PicPay")) {
                tipoDeBanco = "extrato picpay analisado e integrado com sucesso!";
            } else if (texto.contains("Banco Inter")) {
                tipoDeBanco = "extrato Banco Inter analisado e integrado com sucesso!";
            } else {
                tipoDeBanco = "PDF processado com sucesso!";
            }

            String[] linhas = texto.split("\\r?\\n");

            return prepareMovimentacaoDTO(linhas);
        }
    }

    private List<MovimentacaoDTO> prepareMovimentacaoDTO(String[] linhas) {
        List<List<String>> conjuntoTransacoes = new ArrayList<>();
        List<String> transacoesDoDia = new ArrayList<>();

        Pattern pattern = Pattern.compile("(\\d{1,2} de \\p{L}+ de \\d{4})");

        for (String linha : linhas) {
            Matcher matcher = pattern.matcher(linha);

            if (matcher.find()) {
                // Encontrou uma nova data -> significa que um novo dia começou
                if (!transacoesDoDia.isEmpty()) {
                    conjuntoTransacoes.add(new ArrayList<>(transacoesDoDia));
                    transacoesDoDia.clear();
                }
                // Adiciona a data como primeira entrada do "bloco"
                String data = matcher.group(1);
                transacoesDoDia.add("DATA: " + data);
            } else {
                // Linha que pertence ao "bloco" atual
                if (!linha.trim().isEmpty()) {
                    transacoesDoDia.add(linha.trim());
                }
            }
        }

        // Adiciona o último bloco (último dia do extrato)
        if (!transacoesDoDia.isEmpty()) {
            conjuntoTransacoes.add(transacoesDoDia);
        }
        String responsavel = linhas[1];
        List<MovimentacaoDTO> dtos = parseMovimentacoes(conjuntoTransacoes, responsavel);
        dtos.forEach(
                m -> System.out.println(m.getDataMovimentacao() + " - " + m.getDescricao() + " - " + m.getValor()));
        return dtos;

    }

    private List<MovimentacaoDTO> parseMovimentacoes(List<List<String>> conjuntoTransacoes, String responsavel) {
        List<MovimentacaoDTO> movimentacoes = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy", new Locale("pt", "BR"));

        for (List<String> bloco : conjuntoTransacoes) {
            LocalDate dataMovimentacao = null;

            for (String linha : bloco) {
                if (linha.startsWith("DATA:")) {
                    String dataStr = linha.replace("DATA:", "").trim().toLowerCase();
                    dataMovimentacao = LocalDate.parse(dataStr, formatter);
                    continue;
                }

                // Regex para valores monetários
                Pattern valorPattern = Pattern.compile("(-?R\\$\\s?[\\d\\.,]+)");
                Matcher matcher = valorPattern.matcher(linha);

                if (matcher.find()) {
                    String valorStr = matcher.group(1)
                            .replaceAll("[^\\d,-]", "") 
                            .replace(",", ".")
                            .trim();

                    BigDecimal valor = new BigDecimal(valorStr);

                    // Verifica se é saída ou entrada
                    String tipoMovimentacao = valor.compareTo(BigDecimal.ZERO) < 0 ? "saida" : "entrada";

                    // Define categoria inicial (pode evoluir futuramente com NLP)
                    String categoria = inferirCategoria(linha);

                    MovimentacaoDTO dto = MovimentacaoDTO.builder()
                            .id(UUID.randomUUID())
                            .tipoMovimentacao(tipoMovimentacao)
                            .valor(valor.abs()) // sempre positivo, usa tipoMovimentacao para sinal
                            .moeda("BRL")
                            .categoria(categoria)
                            .descricao(linha)
                            .dataMovimentacao(dataMovimentacao)
                            .instituicao("Banco Inter")
                            .responsavel(responsavel) // temporário, virá da lógica de usuários
                            .origem("extrato_pdf")
                            .build();

                    movimentacoes.add(dto);
                }
            }
        }
        return movimentacoes;
    }

    private String inferirCategoria(String descricao) {
        descricao = descricao.toLowerCase();
        if (descricao.contains("ifood"))
            return "alimentacao";
        if (descricao.contains("fatura") || descricao.contains("cartao"))
            return "cartao";
        if (descricao.contains("transferencia"))
            return "transferencia";
        if (descricao.contains("salario"))
            return "salario";
        return "outros";
    }

}
