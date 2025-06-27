package com.example.autheticuser.controller;

import com.example.autheticuser.model.Cliente;
import com.example.autheticuser.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Clientes", description = "API para gerenciamento de clientes")
@SecurityRequirement(name = "bearerAuth")
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Criar novo cliente", description = "Cria um novo cliente no sistema")
    public ResponseEntity<Cliente> criarCliente(
            @Parameter(description = "Dados do cliente") @Valid @RequestBody Cliente cliente) {
        try {
            Cliente novoCliente = clienteService.criarCliente(cliente);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoCliente);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Listar todos os clientes", description = "Retorna todos os clientes cadastrados")
    public ResponseEntity<List<Cliente>> listarClientes() {
        List<Cliente> clientes = clienteService.buscarTodosClientes();
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Buscar cliente por ID", description = "Retorna um cliente específico pelo ID")
    public ResponseEntity<Cliente> buscarClientePorId(
            @Parameter(description = "ID do cliente") @PathVariable Long id) {
        return clienteService.buscarClientePorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Atualizar cliente", description = "Atualiza os dados de um cliente existente")
    public ResponseEntity<Cliente> atualizarCliente(
            @Parameter(description = "ID do cliente") @PathVariable Long id,
            @Parameter(description = "Dados atualizados do cliente") @Valid @RequestBody Cliente cliente) {
        try {
            Cliente clienteAtualizado = clienteService.atualizarCliente(id, cliente);
            return ResponseEntity.ok(clienteAtualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Deletar cliente", description = "Remove um cliente do sistema (apenas ADMIN)")
    public ResponseEntity<Void> deletarCliente(
            @Parameter(description = "ID do cliente") @PathVariable Long id) {
        try {
            clienteService.deletarCliente(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/buscar")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Buscar clientes por nome", description = "Busca clientes que contenham o nome especificado")
    public ResponseEntity<List<Cliente>> buscarClientesPorNome(
            @Parameter(description = "Nome do cliente") @RequestParam String nome) {
        List<Cliente> clientes = clienteService.buscarClientesPorNome(nome);
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/email/{email}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Buscar cliente por email", description = "Retorna um cliente específico pelo email")
    public ResponseEntity<Cliente> buscarClientePorEmail(
            @Parameter(description = "Email do cliente") @PathVariable String email) {
        return clienteService.buscarClientePorEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/cpf/{cpf}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Buscar cliente por CPF", description = "Retorna um cliente específico pelo CPF")
    public ResponseEntity<Cliente> buscarClientePorCpf(
            @Parameter(description = "CPF do cliente") @PathVariable String cpf) {
        return clienteService.buscarClientePorCpf(cpf)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/telefone")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Buscar clientes por telefone", description = "Busca clientes que contenham o telefone especificado")
    public ResponseEntity<List<Cliente>> buscarClientesPorTelefone(
            @Parameter(description = "Telefone do cliente") @RequestParam String telefone) {
        List<Cliente> clientes = clienteService.buscarClientesPorTelefone(telefone);
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/periodo")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Buscar clientes por período", description = "Retorna clientes cadastrados em um período específico (apenas ADMIN)")
    public ResponseEntity<List<Cliente>> buscarClientesPorPeriodo(
            @Parameter(description = "Data de início") @RequestParam LocalDateTime dataInicio,
            @Parameter(description = "Data de fim") @RequestParam LocalDateTime dataFim) {
        List<Cliente> clientes = clienteService.buscarClientesPorPeriodo(dataInicio, dataFim);
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/estatisticas/cadastros-hoje")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Contar clientes cadastrados hoje", description = "Retorna o número de clientes cadastrados hoje (apenas ADMIN)")
    public ResponseEntity<Long> contarClientesCadastradosHoje() {
        long quantidade = clienteService.contarClientesCadastradosHoje();
        return ResponseEntity.ok(quantidade);
    }
}
