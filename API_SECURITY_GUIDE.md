# 🔒 Guia de Segurança da API - AuthenticUser

Este documento explica como usar a API com as configurações de segurança implementadas.

## 🎯 **Visão Geral da Segurança**

A API implementa um sistema de autenticação JWT com dois níveis de acesso:

### **Roles (Papéis)**
- **`USER`** - Usuário comum com permissões limitadas
- **`ADMIN`** - Administrador com acesso total ao sistema

## 🔑 **Usuários Padrão**

### **Admin**
- **Username:** `admin`
- **Password:** `123456`
- **Role:** `ADMIN`
- **Permissões:** Acesso total a todos os endpoints

### **User**
- **Username:** `user`
- **Password:** `password`
- **Role:** `USER`
- **Permissões:** Acesso limitado (leitura e criação)

## 📋 **Matriz de Permissões**

| Endpoint | USER | ADMIN | Descrição |
|----------|------|-------|-----------|
| `POST /api/produtos` | ✅ | ✅ | Criar produto |
| `GET /api/produtos` | ✅ | ✅ | Listar produtos |
| `GET /api/produtos/{id}` | ✅ | ✅ | Buscar produto |
| `PUT /api/produtos/{id}` | ✅ | ✅ | Atualizar produto |
| `DELETE /api/produtos/{id}` | ❌ | ✅ | Deletar produto |
| `PATCH /api/produtos/{id}/estoque` | ❌ | ✅ | Atualizar estoque |
| `POST /api/clientes` | ✅ | ✅ | Criar cliente |
| `GET /api/clientes` | ✅ | ✅ | Listar clientes |
| `GET /api/clientes/{id}` | ✅ | ✅ | Buscar cliente |
| `PUT /api/clientes/{id}` | ✅ | ✅ | Atualizar cliente |
| `DELETE /api/clientes/{id}` | ❌ | ✅ | Deletar cliente |
| `GET /api/clientes/periodo` | ❌ | ✅ | Buscar por período |
| `GET /api/estatisticas/**` | ❌ | ✅ | Estatísticas do sistema |

## 🚀 **Como Usar a API**

### **1. Autenticação**

Primeiro, faça login para obter um token JWT:

```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "123456"
  }'
```

**Resposta:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer"
}
```

### **2. Usar o Token**

Inclua o token no header `Authorization`:

```bash
curl -X GET http://localhost:8080/api/produtos \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

## 📝 **Exemplos de Uso**

### **Criar um Produto (USER/ADMIN)**

```bash
curl -X POST http://localhost:8080/api/produtos \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Smartphone XYZ",
    "descricao": "Smartphone de última geração",
    "preco": 1299.99,
    "quantidadeEstoque": 50,
    "categoria": "Eletrônicos"
  }'
```

### **Listar Produtos (USER/ADMIN)**

```bash
curl -X GET http://localhost:8080/api/produtos \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### **Deletar um Produto (ADMIN apenas)**

```bash
curl -X DELETE http://localhost:8080/api/produtos/1 \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### **Criar um Cliente (USER/ADMIN)**

```bash
curl -X POST http://localhost:8080/api/clientes \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João Silva",
    "email": "joao@email.com",
    "telefone": "(11) 99999-9999",
    "endereco": "Rua das Flores, 123",
    "cpf": "123.456.789-00"
  }'
```

### **Acessar Estatísticas (ADMIN apenas)**

```bash
curl -X GET http://localhost:8080/api/estatisticas/dashboard \
  -H "Authorization: Bearer YOUR_TOKEN"
```

## 🔍 **Endpoints Especiais**

### **Produtos com Estoque Baixo**
```bash
curl -X GET http://localhost:8080/api/produtos/estoque-baixo \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### **Buscar Produtos por Categoria**
```bash
curl -X GET http://localhost:8080/api/produtos/categoria/Eletrônicos \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### **Buscar Clientes por Nome**
```bash
curl -X GET "http://localhost:8080/api/clientes/buscar?nome=João" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

## ⚠️ **Tratamento de Erros**

### **401 Unauthorized**
- Token inválido ou expirado
- Token não fornecido

### **403 Forbidden**
- Usuário não tem permissão para acessar o endpoint
- Role insuficiente (ex: USER tentando acessar endpoint ADMIN)

### **400 Bad Request**
- Dados inválidos na requisição
- Validações de negócio falharam

## 🛠️ **Testando com Postman**

### **1. Configurar Environment**
- Crie uma variável `baseUrl` com valor `http://localhost:8080`
- Crie uma variável `token` (será preenchida após login)

### **2. Login Request**
```
POST {{baseUrl}}/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "123456"
}
```

### **3. Extrair Token**
No script de teste do login:
```javascript
pm.test("Login successful", function () {
    pm.response.to.have.status(200);
    var jsonData = pm.response.json();
    pm.environment.set("token", jsonData.token);
});
```

### **4. Usar Token**
Em outras requisições:
```
Authorization: Bearer {{token}}
```

## 📊 **Monitoramento**

A API inclui endpoints de monitoramento via Actuator:

- **Health Check:** `GET /actuator/health`
- **Métricas:** `GET /actuator/metrics`
- **Prometheus:** `GET /actuator/prometheus`

## 🔧 **Configurações de Segurança**

### **Anotações Utilizadas**
- `@PreAuthorize("hasAnyRole('USER', 'ADMIN')")` - USER ou ADMIN
- `@PreAuthorize("hasRole('ADMIN')")` - Apenas ADMIN
- `@PreAuthorize("hasRole('USER')")` - Apenas USER

### **Configuração Global**
- `@EnableMethodSecurity(prePostEnabled = true)` - Habilita anotações
- JWT Resource Server configurado
- Endpoints públicos: `/auth/**`, `/swagger-ui/**`, `/actuator/**`

## 🎯 **Boas Práticas**

1. **Sempre use HTTPS em produção**
2. **Configure tokens com tempo de expiração adequado**
3. **Implemente rate limiting para endpoints sensíveis**
4. **Monitore tentativas de acesso não autorizado**
5. **Use logs para auditoria de ações administrativas**

---

**🔐 Sua API está segura e pronta para uso!** 