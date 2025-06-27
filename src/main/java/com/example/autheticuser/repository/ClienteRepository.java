package com.example.autheticuser.repository;

import com.example.autheticuser.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    // Buscar por email
    Optional<Cliente> findByEmail(String email);

    // Buscar por nome (case insensitive)
    List<Cliente> findByNomeContainingIgnoreCase(String nome);

    // Buscar por CPF
    Optional<Cliente> findByCpf(String cpf);

    // Verificar se existe cliente com email específico
    boolean existsByEmail(String email);

    // Verificar se existe cliente com CPF específico
    boolean existsByCpf(String cpf);

    // Buscar clientes cadastrados em um período
    @Query("SELECT c FROM Cliente c WHERE c.dataCadastro BETWEEN :dataInicio AND :dataFim")
    List<Cliente> findByDataCadastroBetween(@Param("dataInicio") LocalDateTime dataInicio,
            @Param("dataFim") LocalDateTime dataFim);

    // Buscar clientes por telefone
    List<Cliente> findByTelefoneContaining(String telefone);

    // Contar clientes cadastrados hoje
    @Query("SELECT COUNT(c) FROM Cliente c WHERE DATE(c.dataCadastro) = CURRENT_DATE")
    long countClientesCadastradosHoje();
}