#!/bin/bash

# Script para build e execução da aplicação Spring Boot com Docker

echo "🐳 Docker Build Script - AuthenticUser API"
echo "=========================================="

# Verificar se o Docker está rodando
if ! docker info > /dev/null 2>&1; then
    echo "❌ Erro: Docker não está rodando!"
    echo "Por favor, inicie o Docker Desktop e tente novamente."
    exit 1
fi

echo "✅ Docker está rodando"

# Nome da imagem
IMAGE_NAME="authenticuser-api"
CONTAINER_NAME="authenticuser-container"

# Parar e remover container existente (se houver)
echo "🔄 Parando container existente..."
docker stop $CONTAINER_NAME 2>/dev/null || true
docker rm $CONTAINER_NAME 2>/dev/null || true

# Build da aplicação Maven
echo "🔨 Build da aplicação Maven..."
./mvnw clean package -DskipTests

if [ $? -ne 0 ]; then
    echo "❌ Erro no build Maven!"
    exit 1
fi

echo "✅ Build Maven concluído"

# Build da imagem Docker
echo "🐳 Build da imagem Docker..."
docker build -t $IMAGE_NAME .

if [ $? -ne 0 ]; then
    echo "❌ Erro no build Docker!"
    exit 1
fi

echo "✅ Imagem Docker criada: $IMAGE_NAME"

# Executar o container
echo "🚀 Iniciando container..."
docker run -d \
    --name $CONTAINER_NAME \
    -p 8080:8080 \
    -e SPRING_PROFILES_ACTIVE=docker \
    $IMAGE_NAME

if [ $? -ne 0 ]; then
    echo "❌ Erro ao iniciar container!"
    exit 1
fi

echo "✅ Container iniciado: $CONTAINER_NAME"
echo ""
echo "🌐 Aplicação disponível em: http://localhost:8080"
echo "📚 Swagger UI: http://localhost:8080/swagger-ui.html"
echo "🔍 Health Check: http://localhost:8080/actuator/health"
echo ""
echo "📋 Comandos úteis:"
echo "   Ver logs: docker logs -f $CONTAINER_NAME"
echo "   Parar: docker stop $CONTAINER_NAME"
echo "   Remover: docker rm $CONTAINER_NAME"
echo "   Shell: docker exec -it $CONTAINER_NAME /bin/bash"
echo ""
echo "🎉 Aplicação Dockerizada iniciada com sucesso!" 