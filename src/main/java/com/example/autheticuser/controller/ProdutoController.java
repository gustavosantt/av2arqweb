package com.example.autheticuser.controller;

import com.example.autheticuser.model.Produto;
import com.example.autheticuser.service.ProdutoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;

@RestController
@RequestMapping("api/produtos")
@RequiredArgsConstructor
@Tag(name = "Produtos", description = "API para gerenciamento de produtos")
@SecurityRequirement(name = "bearerAuth")
public class ProdutoController {

    private final ProdutoService produtoService;

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Criar novo produto", description = "Cria um novo produto no sistema")
    public ResponseEntity<Produto> criarProduto(
            @Parameter(description = "Dados do produto") @Valid @RequestBody Produto produto) {
        Produto novoProduto = produtoService.criarProduto(produto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoProduto);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Listar todos os produtos", description = "Retorna todos os produtos cadastrados")
    public ResponseEntity<List<Produto>> listarProdutos() {
        List<Produto> produtos = produtoService.buscarTodosProdutos();
        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Buscar produto por ID", description = "Retorna um produto específico pelo ID")
    public ResponseEntity<Produto> buscarProdutoPorId(
            @Parameter(description = "ID do produto") @PathVariable Long id) {
        return produtoService.buscarProdutoPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Atualizar produto", description = "Atualiza os dados de um produto existente")
    public ResponseEntity<Produto> atualizarProduto(
            @Parameter(description = "ID do produto") @PathVariable Long id,
            @Parameter(description = "Dados atualizados do produto") @Valid @RequestBody Produto produto) {
        try {
            Produto produtoAtualizado = produtoService.atualizarProduto(id, produto);
            return ResponseEntity.ok(produtoAtualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Deletar produto", description = "Remove um produto do sistema (apenas ADMIN)")
    public ResponseEntity<Void> deletarProduto(
            @Parameter(description = "ID do produto") @PathVariable Long id) {
        try {
            produtoService.deletarProduto(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/buscar")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Buscar produtos por nome", description = "Busca produtos que contenham o nome especificado")
    public ResponseEntity<List<Produto>> buscarProdutosPorNome(
            @Parameter(description = "Nome do produto") @RequestParam String nome) {
        List<Produto> produtos = produtoService.buscarProdutosPorNome(nome);
        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/categoria/{categoria}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Buscar produtos por categoria", description = "Retorna produtos de uma categoria específica")
    public ResponseEntity<List<Produto>> buscarProdutosPorCategoria(
            @Parameter(description = "Categoria dos produtos") @PathVariable String categoria) {
        List<Produto> produtos = produtoService.buscarProdutosPorCategoria(categoria);
        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/estoque-baixo")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Produtos com estoque baixo", description = "Retorna produtos com menos de 10 unidades em estoque")
    public ResponseEntity<List<Produto>> buscarProdutosComEstoqueBaixo() {
        List<Produto> produtos = produtoService.buscarProdutosComEstoqueBaixo();
        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/preco")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Buscar produtos por faixa de preço", description = "Retorna produtos dentro de uma faixa de preço")
    public ResponseEntity<List<Produto>> buscarProdutosPorFaixaPreco(
            @Parameter(description = "Preço mínimo") @RequestParam BigDecimal precoMin,
            @Parameter(description = "Preço máximo") @RequestParam BigDecimal precoMax) {
        List<Produto> produtos = produtoService.buscarProdutosPorFaixaPreco(precoMin, precoMax);
        return ResponseEntity.ok(produtos);
    }

    @PatchMapping("/{id}/estoque")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Atualizar estoque", description = "Atualiza a quantidade em estoque de um produto (apenas ADMIN)")
    public ResponseEntity<Produto> atualizarEstoque(
            @Parameter(description = "ID do produto") @PathVariable Long id,
            @Parameter(description = "Nova quantidade em estoque") @RequestParam Integer quantidade) {
        try {
            Produto produto = produtoService.atualizarEstoque(id, quantidade);
            return ResponseEntity.ok(produto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
