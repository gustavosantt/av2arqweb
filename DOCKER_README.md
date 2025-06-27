# 🐳 Docker - AuthenticUser API

Este documento explica como containerizar e executar a aplicação Spring Boot usando Docker.

## 📋 **Pré-requisitos**

- Docker Desktop instalado e rodando
- Java 11 (para build local)
- Maven (para build local)

## 🏗️ **Estrutura de Arquivos Docker**

```
autheticuser/
├── Dockerfile                    # Configuração da imagem Docker
├── .dockerignore                 # Arquivos ignorados no build
├── docker-compose-app.yml        # Orquestração da aplicação
├── docker-build.sh              # Script de build (Linux/Mac)
├── docker-build.ps1             # Script de build (Windows)
└── DOCKER_README.md             # Este arquivo
```

## 🚀 **Métodos de Execução**

### **1. Script Automático (Recomendado)**

#### **Linux/Mac:**
```bash
chmod +x docker-build.sh
./docker-build.sh
```

#### **Windows (PowerShell):**
```powershell
.\docker-build.ps1
```

### **2. Comandos Manuais**

#### **Build da Aplicação:**
```bash
# Build Maven
./mvnw clean package -DskipTests

# Build Docker
docker build -t authenticuser-api .
```

#### **Executar Container:**
```bash
docker run -d \
  --name authenticuser-container \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=docker \
  authenticuser-api
```

### **3. Docker Compose**

```bash
# Executar aplicação completa
docker-compose -f docker-compose-app.yml up -d

# Ver logs
docker-compose -f docker-compose-app.yml logs -f

# Parar serviços
docker-compose -f docker-compose-app.yml down
```

## 🔧 **Configurações do Dockerfile**

### **Imagem Base**
- **OpenJDK 11 Slim** - Imagem otimizada para produção
- **Multi-stage build** - Reduz tamanho final da imagem

### **Otimizações**
- **`.dockerignore`** - Exclui arquivos desnecessários
- **JAR único** - Copia apenas o arquivo executável
- **Porta 8080** - Exposta para acesso externo

## 🌐 **Acessos da Aplicação**

Após executar o container, acesse:

- **API:** http://localhost:8080
- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **Health Check:** http://localhost:8080/actuator/health
- **H2 Console:** http://localhost:8080/h2-console
- **Actuator:** http://localhost:8080/actuator

## 📊 **Monitoramento com Docker**

### **Ver Logs:**
```bash
# Logs em tempo real
docker logs -f authenticuser-container

# Últimas 100 linhas
docker logs --tail 100 authenticuser-container
```

### **Verificar Status:**
```bash
# Status do container
docker ps

# Informações detalhadas
docker inspect authenticuser-container
```

### **Health Check:**
```bash
# Verificar saúde da aplicação
curl http://localhost:8080/actuator/health
```

## 🛠️ **Comandos Úteis**

### **Gerenciamento de Containers:**
```bash
# Listar containers
docker ps -a

# Parar container
docker stop authenticuser-container

# Remover container
docker rm authenticuser-container

# Reiniciar container
docker restart authenticuser-container
```

### **Gerenciamento de Imagens:**
```bash
# Listar imagens
docker images

# Remover imagem
docker rmi authenticuser-api

# Forçar rebuild
docker build --no-cache -t authenticuser-api .
```

### **Shell no Container:**
```bash
# Acessar shell do container
docker exec -it authenticuser-container /bin/bash

# Executar comando específico
docker exec authenticuser-container java -version
```

## 🔍 **Troubleshooting**

### **Problema: Porta 8080 já em uso**
```bash
# Verificar o que está usando a porta
netstat -tulpn | grep 8080

# Usar porta alternativa
docker run -d -p 8081:8080 authenticuser-api
```

### **Problema: Container não inicia**
```bash
# Ver logs de erro
docker logs authenticuser-container

# Verificar recursos do sistema
docker stats
```

### **Problema: Build falha**
```bash
# Limpar cache do Docker
docker system prune -a

# Rebuild sem cache
docker build --no-cache -t authenticuser-api .
```

## 📈 **Monitoramento com Prometheus/Grafana**

Para usar o ambiente de monitoramento completo:

```bash
# Iniciar monitoramento
cd monitoring
docker-compose up -d

# Acessar Grafana
# http://localhost:3000 (admin/admin123)
```

## 🔒 **Segurança em Produção**

### **Variáveis de Ambiente:**
```bash
docker run -d \
  -e JWT_SECRET=suaChaveSecretaMuitoSegura \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/authenticuser \
  authenticuser-api
```

### **Volumes para Persistência:**
```bash
docker run -d \
  -v /app/logs:/app/logs \
  -v /app/data:/app/data \
  authenticuser-api
```

### **Network Segura:**
```bash
# Criar network isolada
docker network create app-network

# Executar na network
docker run -d --network app-network authenticuser-api
```

## 🎯 **Boas Práticas**

1. **Sempre use tags específicas** para imagens em produção
2. **Configure health checks** para monitoramento
3. **Use volumes** para dados persistentes
4. **Configure logs** para auditoria
5. **Use secrets** para dados sensíveis
6. **Monitore recursos** do container

## 📝 **Exemplo de Deploy Completo**

```bash
# 1. Build da aplicação
./mvnw clean package -DskipTests

# 2. Build da imagem
docker build -t authenticuser-api:v1.0.0 .

# 3. Executar com monitoramento
docker-compose -f docker-compose-app.yml up -d

# 4. Verificar status
docker ps
curl http://localhost:8080/actuator/health

# 5. Acessar aplicação
open http://localhost:8080/swagger-ui.html
```

---

**🐳 Sua aplicação está pronta para containerização!** 