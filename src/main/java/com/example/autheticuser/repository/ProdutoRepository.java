package com.example.autheticuser.repository;

import com.example.autheticuser.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    // Buscar por nome (case insensitive)
    List<Produto> findByNomeContainingIgnoreCase(String nome);

    // Buscar por categoria
    List<Produto> findByCategoria(String categoria);

    // Buscar produtos com preço menor que um valor
    List<Produto> findByPrecoLessThan(BigDecimal preco);

    // Buscar produtos com estoque baixo (menos de 10 unidades)
    @Query("SELECT p FROM Produto p WHERE p.quantidadeEstoque < 10")
    List<Produto> findProdutosComEstoqueBaixo();

    // Buscar produtos por faixa de preço
    @Query("SELECT p FROM Produto p WHERE p.preco BETWEEN :precoMin AND :precoMax")
    List<Produto> findByPrecoBetween(@Param("precoMin") BigDecimal precoMin,
            @Param("precoMax") BigDecimal precoMax);

    // Verificar se existe produto com nome específico
    boolean existsByNome(String nome);
}