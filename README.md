# Alpha Steel API

API RESTful para gestão de produtos, matérias-primas e planejamento de produção em empresa de Materiais Industriais.

## Stack Tecnológica

- **Java 21** + **Quarkus 3.31.2**
- **Oracle Database** + **Hibernate ORM Panache**
- **JWT Authentication** (Access token: 10min | Refresh token: 30min)
- **OpenAPI/Swagger** para documentação interativa

## Funcionalidades Principais

### Gestão

- ✅ CRUD de Produtos com geração automática de códigos
- ✅ CRUD de Matérias-Primas com controle de estoque
- ✅ CRUD de Unidades de Medida
- ✅ BOM (Bill of Materials) - Lista de materiais por produto

### Planejamento de Produção

- ✅ Sugestões de produção baseadas em estoque disponível
- ✅ Cálculo de quantidade máxima producível
- ✅ Identificação de materiais faltantes

### Segurança

- ✅ Registro e autenticação de usuários
- ✅ Refresh tokens para renovação de sessão
- ✅ Recuperação de senha via email
- ✅ Limpeza automática de tokens expirados

## Arquitetura

O projeto segue **Domain-Driven Design (DDD)** com arquitetura em camadas:

```
Resource → Service → Repository → Domain
```

**Para detalhes completos da arquitetura, consulte:** [`docs/ARCHITECTURE.md`](./docs/ARCHITECTURE.md)

### Estrutura de Pacotes

```
com.alphasteel.alphaapi/
├── resource/          # Endpoints REST
├── service/           # Lógica de negócio
├── repository/        # Acesso a dados (Panache)
├── domain/
│   ├── entity/        # Entidades JPA
│   └── dto/           # Request/Response DTOs
├── security/          # Autenticação e autorização
├── exception/         # Exceptions customizadas
└── common/            # Utilitários (CodeGenerator, DataSeeder, etc.)
```

## Configuração e Execução

### Pré-requisitos

- Java 21+
- Maven 3.8+
- Docker & Docker Compose (para Oracle DB)

### 1. Iniciar Banco de Dados

```bash
cd docker
docker compose up -d
```

### 2. Configurar Variáveis de Ambiente

Copie o arquivo `.env.example` para `.env` e configure:

```bash
# Banco de Dados
DB_USER=alpha
DB_PASS=alpha
DB_URL=jdbc:oracle:thin:@localhost:1521/XEPDB1

# Email (SMTP)
SMTP_FROM=no-reply@alphasteel.com
SMTP_HOST=smtp-mail.outlook.com
SMTP_PORT=587
SMTP_USERNAME=seu-email@outlook.com
SMTP_PASSWORD=sua-senha
```

### 3. Executar em Modo Desenvolvimento

```bash
mvn quarkus:dev
```

A aplicação estará disponível em: **http://localhost:8080**

### 4. Acessar Documentação da API

- **Swagger UI**: http://localhost:8080/swagger-ui
- **OpenAPI Spec**: http://localhost:8080/openapi

## 📚 Documentação da API

Todos os endpoints estão documentados no Swagger UI. Principais recursos:

| Recurso            | Endpoint Base           | Autenticação |
| ------------------ | ----------------------- | ------------ |
| Autenticação       | `/api/v1/auth/*`        | ❌ Público   |
| Produtos           | `/api/v1/products`      | ✅ Requerida |
| Matérias-Primas    | `/api/v1/raw-materials` | ✅ Requerida |
| Unidades de Medida | `/api/v1/units`         | ✅ Requerida |
| Produção           | `/api/v1/production/*`  | ✅ Requerida |
| Usuários           | `/api/v1/users/*`       | ✅ Requerida |

### Autenticação

1. **Registrar**: `POST /api/v1/auth/register`
2. **Login**: `POST /api/v1/auth/login` → Retorna `accessToken` e `refreshToken`
3. **Usar nos Requests**:

```
Authorization: Bearer {accessToken}
```

4. **Renovar Token**: `POST /api/v1/auth/refresh` com `refreshToken`

## Build para Produção

### JAR Tradicional

```bash
mvn clean package
java -jar target/quarkus-app/quarkus-run.jar
```

### Native Binary (GraalVM)

```bash
mvn clean package -Pnative
./target/alpha-api-1.0.0-SNAPSHOT-runner
```

## Documentação Técnica

- **[ARCHITECTURE.md](./docs/ARCHITECTURE.md)**: Arquitetura detalhada, padrões, fluxos e modelo de dados
- **[.env.example](./.env.example)**: Exemplo de variáveis de ambiente

## Segurança

- ✅ Senhas criptografadas com **BCrypt**
- ✅ Tokens JWT assinados com **RSA (private/public key)**
- ✅ Access tokens com **10 minutos** de validade
- ✅ Refresh tokens com **30 minutos** de validade
- ✅ CORS configurado para origens específicas
- ✅ Validação de input em todos os endpoints
- ⚠️ **Recomendado HTTPS em produção**

## Licença

Propriedade de Alpha Steel.
