package com.example.autheticuser.controller;

import com.example.autheticuser.service.ClienteService;
import com.example.autheticuser.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/estatisticas")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')") // Todos os endpoints deste controller requerem role ADMIN
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Estatísticas", description = "API para estatísticas do sistema (apenas ADMIN)")
public class EstatisticasController {

    private final ClienteService clienteService;
    private final ProdutoService produtoService;

    @GetMapping("/dashboard")
    @Operation(summary = "Dashboard de estatísticas", description = "Retorna estatísticas gerais do sistema")
    public ResponseEntity<Map<String, Object>> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();

        // Estatísticas de clientes
        long clientesCadastradosHoje = clienteService.contarClientesCadastradosHoje();
        long totalClientes = clienteService.buscarTodosClientes().size();

        // Estatísticas de produtos
        long totalProdutos = produtoService.buscarTodosProdutos().size();
        long produtosComEstoqueBaixo = produtoService.buscarProdutosComEstoqueBaixo().size();

        stats.put("clientes", Map.of(
                "total", totalClientes,
                "cadastradosHoje", clientesCadastradosHoje));

        stats.put("produtos", Map.of(
                "total", totalProdutos,
                "comEstoqueBaixo", produtosComEstoqueBaixo));

        stats.put("timestamp", System.currentTimeMillis());

        return ResponseEntity.ok(stats);
    }

    @GetMapping("/resumo")
    @Operation(summary = "Resumo do sistema", description = "Retorna um resumo das principais métricas")
    public ResponseEntity<Map<String, Object>> getResumo() {
        Map<String, Object> resumo = new HashMap<>();

        resumo.put("totalClientes", clienteService.buscarTodosClientes().size());
        resumo.put("totalProdutos", produtoService.buscarTodosProdutos().size());
        resumo.put("produtosEstoqueBaixo", produtoService.buscarProdutosComEstoqueBaixo().size());
        resumo.put("clientesCadastradosHoje", clienteService.contarClientesCadastradosHoje());

        return ResponseEntity.ok(resumo);
    }
}
