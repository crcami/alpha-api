# Arquitetura Alpha Steel API

## Visão Geral

A **Alpha Steel API** é uma API RESTful construída com **Quarkus** para gerenciar produtos, matérias-primas e planejamento de produção em uma empresa de Materiais Industriais. A arquitetura segue os princípios de **Domain-Driven Design (DDD)** com camadas bem definidas, autenticação JWT e integração com banco de dados Oracle.

---

## Stack Tecnológica

### Core Framework

- **Quarkus 3.31.2**: Framework Java nativo na nuvem
- **Java 21**: Linguagem de programação

### Persistência

- **Hibernate ORM com Panache**: ORM simplificado
- **Oracle Database**: Sistema de gerenciamento de banco de dados
- **JDBC Oracle Driver**: Conectividade com Oracle

### Segurança

- **SmallRye JWT**: Autenticação e autorização baseada em tokens JWT
- **Quarkus Security JPA**: Gerenciamento de usuários no banco de dados
- **Access Token Lifespan**: 10 minutos
- **Refresh Token Lifespan**: 30 minutos

### API & Documentação

- **RESTEasy Reactive (Quarkus REST)**: Endpoints REST reativos
- **Jackson**: Serialização/deserialização JSON
- **SmallRye OpenAPI**: Documentação OpenAPI 3.0
- **Swagger UI**: Interface de documentação interativa (disponível em `/swagger-ui`)

### Validação & Email

- **Hibernate Validator**: Validação de dados
- **Quarkus Mailer**: Envio de emails (recuperação de senha)
- **Quarkus Scheduler**: Tarefas agendadas para limpeza de tokens

### Testes

- **JUnit 5**: Framework de testes
- **REST Assured**: Testes de API REST

---

## Arquitetura em Camadas

A aplicação segue uma arquitetura em camadas clara e desacoplada:

```
┌──────────────────────────────────────────┐
│         Resource Layer (REST)            │
│  - ProductResource                       │
│  - RawMaterialResource                   │
│  - ProductionResource                    │
│  - AuthResource, UserResource            │
└──────────────┬───────────────────────────┘
               │
┌──────────────▼───────────────────────────┐
│         Service Layer (Business)         │
│  - ProductService                        │
│  - RawMaterialService                    │
│  - ProductionPlannerService              │
│  - AuthService, UserService, ...         │
└──────────────┬───────────────────────────┘
               │
┌──────────────▼───────────────────────────┐
│      Repository Layer (Data Access)      │
│  - ProductRepository                     │
│  - RawMaterialRepository                 │
│  - UnitOfMeasureRepository               │
│  - AppUserRepository, ...                │
└──────────────┬───────────────────────────┘
               │
┌──────────────▼───────────────────────────┐
│          Domain Layer (Entities)         │
│  - ProductEntity                         │
│  - RawMaterialEntity                     │
│  - ProductMaterialEntity (BOM)           │
│  - AppUserEntity, ...                    │
└──────────────────────────────────────────┘
```

### 1. **Resource Layer** (`resource/`)

Responsável por expor endpoints REST e mapear requisições HTTP para serviços.

**Principais Recursos:**

- `ProductResource`: CRUD de produtos
- `RawMaterialResource`: CRUD de matérias-primas
- `UnitOfMeasureResource`: CRUD de unidades de medida
- `ProductionResource`: Sugestões de produção baseadas em estoque
- `AuthResource`: Login, registro, refresh token, recuperação de senha
- `UserResource`: Gerenciamento de perfil de usuário

**Padrões:**

- Validação de entrada com annotations (`@Valid`)
- Injeção de dependência com CDI (`@Inject`)
- Documentação OpenAPI com `@Tag`, `@Operation`, etc.

### 2. **Service Layer** (`service/`)

Contém a lógica de negócio da aplicação.

**Principais Serviços:**

- `ProductService`: Lógica de produtos (criação, atualização, BOM)
- `RawMaterialService`: Lógica de matérias-primas
- `UnitOfMeasureService`: Gerenciamento de unidades
- `ProductionPlannerService`: Algoritmos de planejamento de produção
- `AuthService`: Autenticação, geração de tokens, reset de senha
- `UserService`: Gerenciamento de usuários

**Características:**

- Transações gerenciadas com `@Transactional`
- Validação de regras de negócio
- Tratamento de exceções customizadas

### 3. **Repository Layer** (`repository/`)

Camada de acesso a dados usando Hibernate Panache.

**Principais Repositórios:**

- `ProductRepository`: Acesso a produtos
- `RawMaterialRepository`: Acesso a matérias-primas
- `ProductMaterialRepository`: Acesso a BOM (Bill of Materials)
- `UnitOfMeasureRepository`: Acesso a unidades de medida
- `AppUserRepository`, `RefreshTokenRepository`, etc.

**Padrões:**

- Métodos de consulta customizados
- Queries nomeadas para complexidade

### 4. **Domain Layer** (`domain/`)

Contém as entidades de domínio e DTOs.

#### Entidades Principais (`domain/entity/`)

- `ProductEntity`: Produto final
- `RawMaterialEntity`: Matéria-prima
- `ProductMaterialEntity`: Relação N:N (BOM) entre produtos e matérias-primas
- `UnitOfMeasureEntity`: Unidade de medida
- `CodeSeriesEntity`: Geração automática de códigos
- `AppUserEntity`: Usuário do sistema
- `RefreshTokenEntity`, `PasswordResetTokenEntity`: Tokens de segurança

#### DTOs (`domain/dto/`)

**Request DTOs:**

- `ProductCreateRequest`, `ProductUpdateRequest`
- `RawMaterialCreateRequest`, `RawMaterialUpdateRequest`
- `AuthLoginRequest`, `AuthRegisterRequest`

**Response DTOs:**

- `ProductResponse`, `RawMaterialResponse`
- `ProductBomItemResponse`: Item da lista de materiais
- `ProductionSuggestionResponse`: Sugestão de produção
- `AuthTokenResponse`: Tokens JWT

---

## Módulos Funcionais

### 1. Gestão de Produtos

**Entidades:** `ProductEntity`, `ProductMaterialEntity`

**Funcionalidades:**

- CRUD de produtos
- Gerenciamento de BOM (Bill of Materials)
- Geração automática de códigos (ex: `PRD-00001`)
- Cálculo de custo total do produto baseado em BOM

**Endpoints:**

- `POST /api/v1/products`: Criar produto
- `GET /api/v1/products`: Listar produtos
- `GET /api/v1/products/{id}`: Detalhes do produto
- `PUT /api/v1/products/{id}`: Atualizar produto
- `DELETE /api/v1/products/{id}`: Deletar produto
- `GET /api/v1/products/{id}/bom`: Lista de materiais do produto

### 2. Gestão de Matérias-Primas

**Entidades:** `RawMaterialEntity`

**Funcionalidades:**

- CRUD de matérias-primas
- Controle de estoque (quantidade disponível)
- Geração automática de códigos (ex: `MAT-00001`)
- Gerenciamento de custos e unidades de medida

**Endpoints:**

- `POST /api/v1/raw-materials`: Criar matéria-prima
- `GET /api/v1/raw-materials`: Listar matérias-primas
- `PUT /api/v1/raw-materials/{id}`: Atualizar matéria-prima
- `DELETE /api/v1/raw-materials/{id}`: Deletar matéria-prima

### 3. Planejamento de Produção

**Serviço:** `ProductionPlannerService`

**Funcionalidades:**

- Análise de qais produtos podem ser fabricados com estoque atual
- Cálculo de quantidade máxima producível
- Identificação de matérias-primas faltantes

**Algoritmo:**

```
Para cada produto:
  1. Obter BOM (lista de materiais necessários)
  2. Para cada material na BOM:
     a. Verificar quantidade em estoque
     b. Calcular quantas unidades do produto podem ser feitas
  3. Retornar menor quantidade (gargalo)
  4. Listar materiais insuficientes
```

**Endpoints:**

- `GET /api/v1/production/suggestions`: Sugestões de produção

### 4. Autenticação e Segurança

**Entidades:** `AppUserEntity`, `RefreshTokenEntity`, `PasswordResetTokenEntity`

**Funcionalidades:**

- **Registro**: Criação de conta com email e senha
- **Login**: Autenticação com email/senha, retorna access + refresh tokens
- **Refresh Token**: Renova access token usando refresh token
- **Recuperação de Senha**: Fluxo completo com email
- **Alteração de Senha**: Para usuários autenticados
- **Gerenciamento de Perfil**: Atualização de dados do usuário

**Segurança:**

- Senhas criptografadas com BCrypt
- Access tokens JWT com 10 minutos de validade
- Refresh tokens com 30 minutos de validade
- Limpeza automática de tokens expirados (scheduler)
- Endpoints públicos: `/api/v1/auth/*`, `/swagger-ui`, `/openapi`
- Endpoints protegidos: `/api/v1/*` (requer autenticação)

**Endpoints:**

- `POST /api/v1/auth/register`: Registrar novo usuário
- `POST /api/v1/auth/login`: Login
- `POST /api/v1/auth/refresh`: Renovar access token
- `POST /api/v1/auth/forgot-password`: Solicitar reset de senha
- `POST /api/v1/auth/reset-password`: Resetar senha com token
- `GET /api/v1/users/me`: Perfil do usuário
- `PUT /api/v1/users/me`: Atualizar perfil
- `PUT /api/v1/users/me/password`: Alterar senha

### 5. Unidades de Medida

**Entidades:** `UnitOfMeasureEntity`

**Funcionalidades:**

- CRUD de unidades de medida (kg, m, un, etc.)
- Códigos únicos e abreviações

**Endpoints:**

- `POST /api/v1/units`: Criar unidade
- `GET /api/v1/units`: Listar unidades
- `PUT /api/v1/units/{id}`: Atualizar unidade
- `DELETE /api/v1/units/{id}`: Deletar unidade

---

## Componentes Auxiliares

### 1. Geração de Códigos (`CodeGenerator`)

Serviço utilitário para gerar códigos sequenciais automáticos:

- **Produtos**: `PRD-00001`, `PRD-00002`, ...
- **Matérias-Primas**: `MAT-00001`, `MAT-00002`, ...
- **Unidades de Medida**: `UOM-00001`, `UOM-00002`, ...

**Implementação:**

- Usa `CodeSeriesEntity` para manter o último número da série
- Thread-safe com `@Lock(LockModeType.PESSIMISTIC_WRITE)`
- Formatação com padding de zeros

### 2. Normalização de Texto (`TextNormalizer`)

Utilitário para normalizar strings:

- Remove acentos e caracteres especiais
- Converte para maiúsculas
- Usado para códigos e identificadores

### 3. Seed de Dados (`DataSeeder`)

Inicializa dados básicos no banco:

- Unidades de medida padrão
- Usuário administrador padrão
- Executado na inicialização da aplicação

### 4. Tratamento de Exceções

**Exceptions Customizadas:**

- `NotFoundException`: Recurso não encontrado (HTTP 404)
- `ConflictException`: Conflito de dados (HTTP 409)
- `UnauthorizedException`: Não autorizado (HTTP 401)

**Global Exception Mapper:**

- `GlobalExceptionMappers`: Mapeia exceções para respostas HTTP padronizadas
- Retorna `ApiError` com mensagem, status e timestamp

---

## Configuração e Ambiente

### Profiles do Quarkus

- **dev**: Desenvolvimento local (schema `update`, logs SQL desabilitados)
- **test**: Testes (schema `drop-and-create`, email mock)
- **prod**: Produção (schema `validate`, sem mocks)

### Variáveis de Ambiente

**Banco de Dados:**

- `DB_USER`: Usuário do Oracle (padrão: `alpha`)
- `DB_PASS`: Senha do Oracle (padrão: `alpha`)
- `DB_URL`: JDBC URL (padrão: `jdbc:oracle:thin:@localhost:1521/XEPDB1`)

**Email (SMTP):**

- `SMTP_FROM`: Email remetente
- `SMTP_HOST`: Servidor SMTP (padrão: Outlook)
- `SMTP_PORT`: Porta SMTP (padrão: 587)
- `SMTP_USERNAME`: Usuário SMTP
- `SMTP_PASSWORD`: Senha SMTP

### CORS

Configurado para desenvolvimento com frontend local:

- Origins permitidas: `http://localhost:5173`, `http://127.0.0.1:5173`
- Métodos: GET, POST, PUT, DELETE, OPTIONS
- Headers: accept, authorization, content-type, x-requested-with
- Credentials: habilitado

---

## Padrões de Projeto Utilizados

### 1. Repository Pattern

Abstração da camada de acesso a dados com Panache Repository.

### 2. DTO Pattern

Separação entre entidades de domínio e objetos de transferência de dados.

### 3. Dependency Injection (CDI)

Injeção de dependências gerenciada pelo container Quarkus.

### 4. Strategy Pattern

`ProductionPlannerService` implementa estratégia de cálculo de produção.

### 5. Builder Pattern

Usado em DTOs e na construção de tokens JWT.

---

## Fluxos de Dados Principais

### Fluxo de Criação de Produto

```
1. Cliente → POST /api/v1/products (ProductCreateRequest)
2. ProductResource valida e chama ProductService
3. ProductService:
   a. Gera código automático (CodeGenerator)
   b. Cria ProductEntity
   c. Persiste no banco via ProductRepository
   d. Para cada material na BOM:
      - Cria ProductMaterialEntity
      - Persiste via ProductMaterialRepository
4. ProductService retorna ProductResponse
5. ProductResource retorna HTTP 201 Created
```

### Fluxo de Autenticação

```
1. Cliente → POST /api/v1/auth/login (email, password)
2. AuthResource chama AuthService
3. AuthService:
   a. Busca usuário por email
   b. Valida senha com BCrypt
   c. Gera access token JWT (10 min)
   d. Gera refresh token aleatório (30 min)
   e. Persiste refresh token no banco
4. AuthService retorna AuthTokenResponse
5. Cliente armazena tokens
6. Cliente usa access token em header: Authorization: Bearer {token}
```

### Fluxo de Sugestão de Produção

```
1. Cliente → GET /api/v1/production/suggestions
2. ProductionResource chama ProductionPlannerService
3. ProductionPlannerService:
   a. Busca todos os produtos
   b. Para cada produto:
      - Busca BOM (materiais necessários)
      - Para cada material:
        * Consulta estoque de matéria-prima
        * Calcula quantidade producível
      - Identifica gargalo (menor quantidade)
      - Lista materiais insuficientes
4. ProductionPlannerService retorna lista de ProductionSuggestionResponse
5. ProductionResource retorna HTTP 200 OK
```

---

## Modelo de Dados (Entidades Principais)

### ProductEntity

```
- id: Long (PK)
- code: String (único, ex: "PRD-00001")
- name: String
- description: String
- totalCost: BigDecimal
- materials: List<ProductMaterialEntity> (BOM)
```

### RawMaterialEntity

```
- id: Long (PK)
- code: String (único, ex: "MAT-00001")
- name: String
- description: String
- stockQuantity: BigDecimal
- unitCost: BigDecimal
- unitOfMeasure: UnitOfMeasureEntity
```

### ProductMaterialEntity (BOM)

```
- id: ProductMaterialId (PK composta)
- product: ProductEntity
- rawMaterial: RawMaterialEntity
- quantityNeeded: BigDecimal
```

### AppUserEntity

```
- id: Long (PK)
- email: String (único)
- password: String (BCrypt hash)
- name: String
- roles: String (ex: "user,admin")
```

---

## Considerações de Segurança

1. **Autenticação JWT**: Tokens assinados com RSA (private/public key)
2. **Senhas Criptografadas**: BCrypt para hashing
3. **HTTPS**: Recomendado para produção
4. **CORS**: Configurado para origins específicas
5. **Validação de Input**: Bean Validation em todos os DTOs
6. **SQL Injection**: Protegido pelo ORM (Hibernate)
7. **Tokens Expirados**: Limpeza automática via scheduler

---

## Documentação da API

A documentação completa da API está disponível via Swagger UI:

**Desenvolvimento:**

- Swagger UI: http://localhost:8080/swagger-ui
- OpenAPI Spec: http://localhost:8080/openapi

**Características:**

- Documentação interativa
- Exemplos de request/response
- Testagem in-browser
- Schemas de todos os DTOs
- Códigos de status HTTP documentados

---

## Estratégia de Testes

### Testes Unitários

- Framework: JUnit 5
- Foco: Lógica de negócio em Services

### Testes de Integração

- Framework: REST Assured + Quarkus Test
- Foco: Endpoints REST completos
- Profile: `test` (banco em memória, email mock)

### Comandos

```bash
# Testes unitários
mvn test

# Testes de integração
mvn verify
```

---

## Execução e Deploy

### Desenvolvimento Local

```bash
# Com Maven
mvn quarkus:dev

# Com Docker Compose (banco de dados)
cd docker && docker compose up
```

### Build para Produção

```bash
# JAR tradicional
mvn package

# Native binary (GraalVM)
mvn package -Pnative
```

### Variáveis de Ambiente Necessárias

Ver seção "Configuração e Ambiente" acima.

---

## Roadmap e Melhorias Futuras

1. **Paginação**: Adicionar paginação em listagens
2. **Filtros Avançados**: Busca e filtros em produtos/materiais
3. **Auditoria**: Logs de alterações (createdAt, updatedAt, modifiedBy)
4. **Roles e Permissões**: Sistema de permissões granulares
5. **Inventário**: Controle de entrada/saída de estoque
6. **Produção Real**: Registro de ordens de produção executadas
7. **Relatórios**: Dashboards e relatórios analíticos
8. **WebSockets**: Notificações em tempo real
9. **Cache**: Redis para otimização de consultas
10. **Observabilidade**: Métricas com Prometheus/Grafana
