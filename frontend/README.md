# PicPay Talentos — Front-end

Interface React para gerenciamento de candidatos, integrada à API Spring Boot deste repositório.

## Executar

1. Inicie o back-end na porta `8080`.
2. Dentro da pasta `frontend`, execute `npm install`.
3. Execute `npm run dev`.
4. Abra `http://localhost:5173`.

O front acessa diretamente `http://localhost:8080/funcionarios`. O back-end permite requisições das origens locais `http://localhost:5173` e `http://127.0.0.1:5173` por meio da configuração de CORS.

Para utilizar outra URL, copie `.env.example` para `.env.local` e altere `VITE_API_URL`. Em outro ambiente, a nova origem do front também deverá ser adicionada à configuração de CORS do back-end.

## Testes e build

- Execute `npm test` para rodar os testes unitários do front-end.
- Execute `npm run build` para validar a compilação de produção.
- Na raiz do repositório, execute `.\gradlew.bat test` para rodar os testes do back-end.
