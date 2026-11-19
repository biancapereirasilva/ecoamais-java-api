# Ecoa+ — API

API RESTful do **Ecoa+**, plataforma de agendamento de serviços públicos pensada para ser acessível (pictogramas, linguagem simples e lista de documentos de cada serviço). Projeto da **Global Solution 2026/2 — CitizenTech**, FIAP.

- **API publicada:** https://ecoamais-api.onrender.com/servicos
- **Vídeo de demonstração:** https://www.youtube.com/watch?v=lrDCqrXxn-0
- **Stack:** Java 21, Spring Boot, Maven, JDBC puro (sem JPA), Oracle FIAP, Docker e Render

## Integrantes

| Nome | RM |
|---|---|
| Bianca Pereira da Silva | 571077 |
| Isabelle Souza Lima Pires Araujo | 569370 |
| Maria Eduarda Cavallari Quarelo | 570462 |
| Thays Lira de Oliveira | 568799 |

## O que a API faz

Um cidadão escolhe um **serviço** (consulta, vacina, segunda via de RG, CadÚnico...), uma **unidade** que o oferece (UBS, CRAS, Poupatempo) e agenda um **atendimento** em uma data e hora.

Entidades: `Servico`, `Unidade`, `Cidadao` e `Atendimento`. Um serviço é oferecido em várias unidades e uma unidade oferece vários serviços (relação N:N, tabela `tb_servico_unidade`).

## Endpoints

| Método | Rota | Descrição |
|---|---|---|
| GET | `/servicos` | Lista os serviços |
| GET | `/servicos/{id}` | Busca um serviço |
| POST | `/servicos` | Cadastra um serviço |
| PUT | `/servicos/{id}` | Atualiza um serviço |
| DELETE | `/servicos/{id}` | Exclui um serviço |
| GET | `/unidades` | Lista as unidades (`?servicoId=1` filtra pelas que oferecem o serviço) |
| GET | `/unidades/{id}` | Busca uma unidade |
| POST | `/unidades` | Cadastra uma unidade |
| PUT | `/unidades/{id}` | Atualiza uma unidade |
| DELETE | `/unidades/{id}` | Exclui uma unidade |
| GET | `/cidadaos` | Lista os cidadãos |
| GET | `/cidadaos/{id}` | Busca um cidadão |
| POST | `/cidadaos` | Cadastra um cidadão |
| PUT | `/cidadaos/{id}` | Atualiza um cidadão |
| DELETE | `/cidadaos/{id}` | Exclui um cidadão |
| GET | `/atendimentos` | Lista todos os atendimentos |
| GET | `/atendimentos/{id}` | Busca um atendimento |
| GET | `/cidadaos/{idCidadao}/atendimentos` | Lista os atendimentos de um cidadão |
| POST | `/cidadaos/{idCidadao}/atendimentos` | Agenda um atendimento |
| PUT | `/atendimentos/{id}` | Atualiza um atendimento (inclusive o status) |
| DELETE | `/atendimentos/{id}` | Exclui um atendimento |

## Status HTTP

| Status | Quando |
|---|---|
| 200 | Consulta ou atualização feita com sucesso |
| 201 | Cadastro feito (header `Location` com o endereço do novo registro) |
| 204 | Exclusão feita |
| 400 | Dados inválidos (validação, JSON quebrado, código da rota que não é número) |
| 404 | Registro ou rota não encontrada |
| 405 | Método não permitido na rota |
| 409 | Regra de negócio violada |
| 415 | Corpo enviado em formato diferente de JSON |
| 500 | Erro no banco ou erro inesperado (mensagem genérica, sem detalhes técnicos) |

Todo erro volta no mesmo formato:

```json
{ "mensagem": "Esse horário já está ocupado nesta unidade. Escolha outro." }
```

## Regras de negócio

- Só um atendimento por horário em cada unidade (atendimento cancelado libera o horário).
- A unidade precisa oferecer o serviço escolhido.
- Um novo atendimento começa com o status `AGENDADO`. Status possíveis: `AGENDADO`, `CHAMADO`, `CONCLUIDO` e `CANCELADO`.
- Não é possível excluir cidadão, serviço ou unidade que tenham atendimentos (409).
- Validação dos dados de entrada com Bean Validation (campos obrigatórios, tamanhos, categoria, pictograma, telefone e data futura).

## Estrutura do projeto

```
br.com.fiap
├── controller   rotas REST e status HTTP
├── exception    erros do domínio
├── handler      GlobalExceptionHandler (JSON de erro padronizado)
├── config       CorsFilter (CORS)
└── model
    ├── (entidades)  Servico, Unidade, Cidadao, Atendimento
    ├── bo           regras de negócio
    ├── dao          acesso ao banco com JDBC (ConnectionFactory)
    └── dto          entrada e saída da API (Request, Response e ErroResposta)
```

## Banco de dados

Oracle FIAP, com tabelas `tb_servico`, `tb_unidade`, `tb_cidadao`, `tb_servico_unidade` e `tb_atendimento`, e uma sequence para cada tabela (`sq_tb_...`).

Rode os scripts SQL nesta ordem, uma única vez:

1. Script de estrutura (sequences e tabelas).
2. Script de dados de exemplo (serviços, unidades e quais serviços cada unidade oferece).

## Variáveis de ambiente

A conexão com o banco não tem credenciais no código. Configure:

| Variável | Exemplo |
|---|---|
| `DB_URL` | `jdbc:oracle:thin:@oracle.fiap.com.br:1521:ORCL` |
| `DB_USER` | usuário do Oracle |
| `DB_PASSWORD` | senha do Oracle |

## Como rodar

Localmente, com as três variáveis configuradas:

```
./mvnw spring-boot:run
```

No Windows, use `.\mvnw.cmd spring-boot:run`. A API sobe em `http://localhost:8080`.

Para gerar o `.jar`:

```
./mvnw clean -DskipTests package
```

Com Docker:

```
docker build -t ecoamais-api .
docker run -p 8080:8080 -e DB_URL=... -e DB_USER=... -e DB_PASSWORD=... ecoamais-api
```

## Deploy

Publicada no **Render** como Web Service (ambiente Docker), ligado a este repositório. A cada `push` na branch `main` o Render faz um novo deploy. As variáveis `DB_URL`, `DB_USER` e `DB_PASSWORD` ficam configuradas no painel do Render. No plano gratuito, a primeira chamada depois de um tempo parado pode levar cerca de um minuto.