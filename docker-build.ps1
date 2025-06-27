# Script PowerShell para build e execução da aplicação Spring Boot com Docker

Write-Host "🐳 Docker Build Script - AuthenticUser API" -ForegroundColor Cyan
Write-Host "==========================================" -ForegroundColor Cyan
Write-Host ""

# Verificar se o Docker está rodando
try {
    docker info | Out-Null
    Write-Host "✅ Docker está rodando" -ForegroundColor Green
}
catch {
    Write-Host "❌ Erro: Docker não está rodando!" -ForegroundColor Red
    Write-Host "Por favor, inicie o Docker Desktop e tente novamente." -ForegroundColor Yellow
    exit 1
}

# Nome da imagem
$IMAGE_NAME = "authenticuser-api"
$CONTAINER_NAME = "authenticuser-container"

# Parar e remover container existente (se houver)
Write-Host "🔄 Parando container existente..." -ForegroundColor Yellow
docker stop $CONTAINER_NAME 2>$null
docker rm $CONTAINER_NAME 2>$null

# Build da aplicação Maven
Write-Host "🔨 Build da aplicação Maven..." -ForegroundColor Yellow
& ./mvnw clean package -DskipTests

if ($LASTEXITCODE -ne 0) {
    Write-Host "❌ Erro no build Maven!" -ForegroundColor Red
    exit 1
}

Write-Host "✅ Build Maven concluído" -ForegroundColor Green

# Build da imagem Docker
Write-Host "🐳 Build da imagem Docker..." -ForegroundColor Yellow
docker build -t $IMAGE_NAME .

if ($LASTEXITCODE -ne 0) {
    Write-Host "❌ Erro no build Docker!" -ForegroundColor Red
    exit 1
}

Write-Host "✅ Imagem Docker criada: $IMAGE_NAME" -ForegroundColor Green

# Executar o container
Write-Host "🚀 Iniciando container..." -ForegroundColor Yellow
docker run -d `
    --name $CONTAINER_NAME `
    -p 8080:8080 `
    -e SPRING_PROFILES_ACTIVE=docker `
    $IMAGE_NAME

if ($LASTEXITCODE -ne 0) {
    Write-Host "❌ Erro ao iniciar container!" -ForegroundColor Red
    exit 1
}

Write-Host "✅ Container iniciado: $CONTAINER_NAME" -ForegroundColor Green
Write-Host ""
Write-Host "🌐 Aplicação disponível em: http://localhost:8080" -ForegroundColor Cyan
Write-Host "📚 Swagger UI: http://localhost:8080/swagger-ui.html" -ForegroundColor Cyan
Write-Host "🔍 Health Check: http://localhost:8080/actuator/health" -ForegroundColor Cyan
Write-Host ""
Write-Host "📋 Comandos úteis:" -ForegroundColor Yellow
Write-Host "   Ver logs: docker logs -f $CONTAINER_NAME" -ForegroundColor White
Write-Host "   Parar: docker stop $CONTAINER_NAME" -ForegroundColor White
Write-Host "   Remover: docker rm $CONTAINER_NAME" -ForegroundColor White
Write-Host "   Shell: docker exec -it $CONTAINER_NAME /bin/bash" -ForegroundColor White
Write-Host ""
Write-Host "🎉 Aplicação Dockerizada iniciada com sucesso!" -ForegroundColor Green 