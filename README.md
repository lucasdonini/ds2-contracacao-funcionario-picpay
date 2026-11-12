# Docker

O backend usa Java 21, Spring Boot Web/Validation e Gradle; o frontend usa React e Vite. Os dados ficam em memória: reiniciar o backend perde os registros e réplicas não compartilham dados.

## Local

```sh
docker compose up --build -d
docker compose logs -f
docker compose down
```

Acesse http://localhost:8080. Para trocar a porta no PowerShell, defina `$env:FRONTEND_PORT = '3000'` antes de iniciar. Apenas o frontend publica porta; `/api/` segue pela rede interna até o backend.

## Cloud

```sh
docker build -t picpay-backend .
docker build -t picpay-frontend ./frontend
```

Publique as imagens no registry escolhido e crie dois serviços na plataforma. Ambos escutam na porta 8080 e executam sem root. Configure `BACKEND_URL` no frontend com a origem interna do backend, por exemplo `http://backend:8080`, sem barra final ou caminho. A configuração é aplicada na inicialização, sem reconstruir a imagem. O hostname deve resolver ao iniciar o frontend; reinicie-o se o endereço do backend mudar.

Direcione o tráfego público ao frontend e configure TLS no ingress/balanceador da plataforma. O navegador usa `/api/funcionarios` na mesma origem. Fora do Docker, permanece o comportamento existente de `VITE_API_URL`.

Os builds multiestágio mantêm ferramentas de compilação fora das imagens finais. Os contextos usam listas de inclusão nos `.dockerignore`, excluindo testes, documentação, IDEs, caches, arquivos de ambiente e artefatos locais. Não é necessário Java ou Node no host para construir as imagens.

## Validação

```sh
./gradlew test
npm --prefix frontend ci
npm --prefix frontend test
docker compose config --quiet
docker compose build
```

Com os serviços ativos, verifique `/`, uma rota da SPA e `/api/funcionarios`.
