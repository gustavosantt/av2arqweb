package com.example.autheticuser.service;

import com.example.autheticuser.model.Cliente;
import com.example.autheticuser.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ClienteService {

    private final ClienteRepository clienteRepository;

    // Criar cliente
    public Cliente criarCliente(Cliente cliente) {
        // Validações
        if (cliente.getNome() == null || cliente.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do cliente é obrigatório");
        }

        if (cliente.getEmail() == null || cliente.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email do cliente é obrigatório");
        }

        // Validar formato do email (básico)
        if (!cliente.getEmail().contains("@")) {
            throw new IllegalArgumentException("Email inválido");
        }

        // Verificar se já existe cliente com mesmo email
        if (clienteRepository.existsByEmail(cliente.getEmail())) {
            throw new IllegalArgumentException("Já existe um cliente com este email");
        }

        // Verificar se já existe cliente com mesmo CPF (se fornecido)
        if (cliente.getCpf() != null && !cliente.getCpf().trim().isEmpty()) {
            if (clienteRepository.existsByCpf(cliente.getCpf())) {
                throw new IllegalArgumentException("Já existe um cliente com este CPF");
            }
        }

        return clienteRepository.save(cliente);
    }

    // Buscar todos os clientes
    @Transactional(readOnly = true)
    public List<Cliente> buscarTodosClientes() {
        return clienteRepository.findAll();
    }

    // Buscar cliente por ID
    @Transactional(readOnly = true)
    public Optional<Cliente> buscarClientePorId(Long id) {
        return clienteRepository.findById(id);
    }

    // Buscar cliente por email
    @Transactional(readOnly = true)
    public Optional<Cliente> buscarClientePorEmail(String email) {
        return clienteRepository.findByEmail(email);
    }

    // Atualizar cliente
    public Cliente atualizarCliente(Long id, Cliente clienteAtualizado) {
        Cliente clienteExistente = clienteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));

        // Validações
        if (clienteAtualizado.getNome() != null && !clienteAtualizado.getNome().trim().isEmpty()) {
            clienteExistente.setNome(clienteAtualizado.getNome());
        }

        if (clienteAtualizado.getEmail() != null && !clienteAtualizado.getEmail().trim().isEmpty()) {
            // Validar formato do email
            if (!clienteAtualizado.getEmail().contains("@")) {
                throw new IllegalArgumentException("Email inválido");
            }

            // Verificar se o novo email já existe em outro cliente
            if (!clienteExistente.getEmail().equals(clienteAtualizado.getEmail()) &&
                    clienteRepository.existsByEmail(clienteAtualizado.getEmail())) {
                throw new IllegalArgumentException("Já existe um cliente com este email");
            }
            clienteExistente.setEmail(clienteAtualizado.getEmail());
        }

        if (clienteAtualizado.getTelefone() != null) {
            clienteExistente.setTelefone(clienteAtualizado.getTelefone());
        }

        if (clienteAtualizado.getEndereco() != null) {
            clienteExistente.setEndereco(clienteAtualizado.getEndereco());
        }

        if (clienteAtualizado.getCpf() != null && !clienteAtualizado.getCpf().trim().isEmpty()) {
            // Verificar se o novo CPF já existe em outro cliente
            if (!clienteExistente.getCpf().equals(clienteAtualizado.getCpf()) &&
                    clienteRepository.existsByCpf(clienteAtualizado.getCpf())) {
                throw new IllegalArgumentException("Já existe um cliente com este CPF");
            }
            clienteExistente.setCpf(clienteAtualizado.getCpf());
        }

        if (clienteAtualizado.getDataNascimento() != null) {
            clienteExistente.setDataNascimento(clienteAtualizado.getDataNascimento());
        }

        return clienteRepository.save(clienteExistente);
    }

    // Deletar cliente
    public void deletarCliente(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new IllegalArgumentException("Cliente não encontrado");
        }
        clienteRepository.deleteById(id);
    }

    // Buscar clientes por nome
    @Transactional(readOnly = true)
    public List<Cliente> buscarClientesPorNome(String nome) {
        return clienteRepository.findByNomeContainingIgnoreCase(nome);
    }

    // Buscar clientes por telefone
    @Transactional(readOnly = true)
    public List<Cliente> buscarClientesPorTelefone(String telefone) {
        return clienteRepository.findByTelefoneContaining(telefone);
    }

    // Buscar clientes cadastrados em um período
    @Transactional(readOnly = true)
    public List<Cliente> buscarClientesPorPeriodo(LocalDateTime dataInicio, LocalDateTime dataFim) {
        return clienteRepository.findByDataCadastroBetween(dataInicio, dataFim);
    }

    // Contar clientes cadastrados hoje
    @Transactional(readOnly = true)
    public long contarClientesCadastradosHoje() {
        return clienteRepository.countClientesCadastradosHoje();
    }

    // Buscar cliente por CPF
    @Transactional(readOnly = true)
    public Optional<Cliente> buscarClientePorCpf(String cpf) {
        return clienteRepository.findByCpf(cpf);
    }
}