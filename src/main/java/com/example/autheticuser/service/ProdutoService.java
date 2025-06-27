package com.example.autheticuser.service;

import com.example.autheticuser.model.Produto;
import com.example.autheticuser.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    // Criar produto
    public Produto criarProduto(Produto produto) {
        // Validações
        if (produto.getNome() == null || produto.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do produto é obrigatório");
        }

        if (produto.getPreco() == null || produto.getPreco().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Preço deve ser maior que zero");
        }

        if (produto.getQuantidadeEstoque() == null || produto.getQuantidadeEstoque() < 0) {
            throw new IllegalArgumentException("Quantidade em estoque deve ser maior ou igual a zero");
        }

        // Verificar se já existe produto com mesmo nome
        if (produtoRepository.existsByNome(produto.getNome())) {
            throw new IllegalArgumentException("Já existe um produto com este nome");
        }

        return produtoRepository.save(produto);
    }

    // Buscar todos os produtos
    @Transactional(readOnly = true)
    public List<Produto> buscarTodosProdutos() {
        return produtoRepository.findAll();
    }

    // Buscar produto por ID
    @Transactional(readOnly = true)
    public Optional<Produto> buscarProdutoPorId(Long id) {
        return produtoRepository.findById(id);
    }

    // Atualizar produto
    public Produto atualizarProduto(Long id, Produto produtoAtualizado) {
        Produto produtoExistente = produtoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));

        // Validações
        if (produtoAtualizado.getNome() != null && !produtoAtualizado.getNome().trim().isEmpty()) {
            // Verificar se o novo nome já existe em outro produto
            if (!produtoExistente.getNome().equals(produtoAtualizado.getNome()) &&
                    produtoRepository.existsByNome(produtoAtualizado.getNome())) {
                throw new IllegalArgumentException("Já existe um produto com este nome");
            }
            produtoExistente.setNome(produtoAtualizado.getNome());
        }

        if (produtoAtualizado.getDescricao() != null) {
            produtoExistente.setDescricao(produtoAtualizado.getDescricao());
        }

        if (produtoAtualizado.getPreco() != null && produtoAtualizado.getPreco().compareTo(BigDecimal.ZERO) > 0) {
            produtoExistente.setPreco(produtoAtualizado.getPreco());
        }

        if (produtoAtualizado.getQuantidadeEstoque() != null && produtoAtualizado.getQuantidadeEstoque() >= 0) {
            produtoExistente.setQuantidadeEstoque(produtoAtualizado.getQuantidadeEstoque());
        }

        if (produtoAtualizado.getCategoria() != null) {
            produtoExistente.setCategoria(produtoAtualizado.getCategoria());
        }

        return produtoRepository.save(produtoExistente);
    }

    // Deletar produto
    public void deletarProduto(Long id) {
        if (!produtoRepository.existsById(id)) {
            throw new IllegalArgumentException("Produto não encontrado");
        }
        produtoRepository.deleteById(id);
    }

    // Buscar produtos por nome
    @Transactional(readOnly = true)
    public List<Produto> buscarProdutosPorNome(String nome) {
        return produtoRepository.findByNomeContainingIgnoreCase(nome);
    }

    // Buscar produtos por categoria
    @Transactional(readOnly = true)
    public List<Produto> buscarProdutosPorCategoria(String categoria) {
        return produtoRepository.findByCategoria(categoria);
    }

    // Buscar produtos com estoque baixo
    @Transactional(readOnly = true)
    public List<Produto> buscarProdutosComEstoqueBaixo() {
        return produtoRepository.findProdutosComEstoqueBaixo();
    }

    // Buscar produtos por faixa de preço
    @Transactional(readOnly = true)
    public List<Produto> buscarProdutosPorFaixaPreco(BigDecimal precoMin, BigDecimal precoMax) {
        return produtoRepository.findByPrecoBetween(precoMin, precoMax);
    }

    // Atualizar estoque
    public Produto atualizarEstoque(Long id, Integer novaQuantidade) {
        if (novaQuantidade < 0) {
            throw new IllegalArgumentException("Quantidade em estoque não pode ser negativa");
        }

        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));

        produto.setQuantidadeEstoque(novaQuantidade);
        return produtoRepository.save(produto);
    }
}