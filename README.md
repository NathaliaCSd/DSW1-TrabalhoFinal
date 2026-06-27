# 🐾 PetBnB

Plataforma web de hospedagem de animais de estimação — conecta tutores que precisam viajar com anfitriões dispostos a receber pets em suas casas.

Desenvolvido por **Nathalia Cristina Santos – 795698**  
Disciplina: Desenvolvimento de Software para Web I (DSW1) — UFSCar

---

## Tecnologias utilizadas

| Camada | Tecnologia |
|---|---|
| Back-end | Spring Boot 4.x + Spring MVC |
| Persistência | Spring Data JPA + Hibernate |
| Segurança | Spring Security 6 + BCrypt |
| Front-end | Thymeleaf + Bootstrap 5 |
| Banco de dados | PostgreSQL |
| Build | Maven |
| REST API | Spring MVC (`@RestController`) |
| Cliente REST | Spring `RestClient` (projeto separado) |

---

## Perfis de usuário

O sistema possui três perfis distintos, todos gerenciados pelo administrador:

| Perfil | Dados cadastrais | Acesso |
|---|---|---|
| **Administrador** | Nome, login, senha | CRUD de todos os usuários |
| **Tutor (Dono de Pet)** | Nome, login, senha, CPF, telefone, sexo, data de nascimento | Gerencia pets e faz reservas |
| **Anfitrião** | Nome, login, senha, CNPJ, descrição do serviço | Gerencia casas disponíveis |

> O cadastro de novos usuários é realizado **exclusivamente pelo administrador**. Não há cadastro público.

---

## Estrutura do projeto

```
PetBnB/
├── src/main/java/br/ufscar/dc/dsw/
│   ├── Application.java               # Inicialização + seed do admin
│   ├── config/
│   │   ├── SecurityConfig.java        # Regras de acesso por perfil
│   │   ├── UserDetailsServiceImpl.java # Integração Spring Security + banco
│   │   ├── LoginSuccessHandler.java   # Redirecionamento pós-login por perfil
│   │   └── WebConfig.java             # Internacionalização (i18n)
│   ├── controller/
│   │   ├── HomeController.java
│   │   ├── UsuarioController.java     # CRUD de usuários (só admin)
│   │   ├── PetController.java         # CRUD de pets (tutor)
│   │   ├── CasaController.java        # CRUD de casas (anfitrião)
│   │   ├── ReservaController.java     # Fluxo de reservas (tutor)
│   │   ├── ReservaRestController.java # REST API de reservas
│   │   └── CustomErrorController.java
│   ├── dao/                           # Repositórios Spring Data JPA
│   ├── domain/
│   │   ├── Usuario.java               # Classe base (herança JOINED)
│   │   ├── DonoDePet.java             # Subclasse tutor
│   │   ├── Anfitriao.java             # Subclasse anfitrião
│   │   ├── Pet.java
│   │   ├── Casa.java
│   │   └── Reserva.java
│   └── services/
│       ├── spec/IReservaService.java
│       └── impl/ReservaService.java
└── src/main/resources/
    ├── templates/                     # Views Thymeleaf
    ├── messages_pt_BR.properties      # Internacionalização PT
    └── messages_en.properties         # Internacionalização EN
```

---

## Requisitos

- Java 17+
- Maven 3.8+
- PostgreSQL 14+

---

## Configuração do banco de dados

### Criar usuário e banco

```sql
CREATE USER petbnbuser WITH PASSWORD 'petpet';
CREATE DATABASE "PetBnB" OWNER petbnbuser;
```

### Ou usando o script incluso

```bash
PGPASSWORD=petpet psql -U petbnbuser -h localhost -f db/PostGre/create.sql
```

### Parâmetros de conexão

| Parâmetro | Valor padrão |
|---|---|
| Banco | `PetBnB` |
| Host | `localhost` |
| Porta | `5432` |
| Usuário | `petbnbuser` |
| Senha | `petpet` |

A aplicação também aceita variáveis de ambiente:

```bash
POSTGRES_HOST=localhost
POSTGRES_USER=petbnbuser
POSTGRES_PASSWORD=petpet
```

---

## Como executar

### 1. Clone o repositório

```bash
git clone <url-do-repositorio>
cd PetBnB
```

### 2. Compile e execute

```bash
mvn clean spring-boot:run
```

### 3. Acesse

```
http://localhost:8080
```

### Credenciais do administrador (criadas automaticamente)

| Campo | Valor |
|---|---|
| Login | `admin` |
| Senha | `admin` |

> A senha é armazenada com hash **BCrypt**. Ao recriar o banco, a aplicação gera o admin automaticamente com a senha já criptografada.

### Se a porta 8080 estiver em uso

```bash
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8082
```

---

## REST API (T7)

A API REST está disponível em `http://localhost:8080/reservas/api` e não requer autenticação.

| Método | Endpoint | Descrição |
|---|---|---|
| `GET` | `/reservas/api` | Lista todas as reservas |
| `GET` | `/reservas/api/{id}` | Busca reserva por ID |
| `GET` | `/reservas/api/pet/{petId}` | Lista reservas de um pet |
| `POST` | `/reservas/api` | Cria nova reserva |
| `PUT` | `/reservas/api/{id}` | Atualiza reserva |
| `DELETE` | `/reservas/api/{id}` | Remove reserva |

### Exemplo de payload (POST/PUT)

```json
{
  "dataInicio": "2025-07-10",
  "dataFim": "2025-07-15",
  "pet": { "id": 1 },
  "casa": { "id": 1 }
}
```

### Códigos de resposta

| Código | Significado |
|---|---|
| `200` | OK — GET e PUT com sucesso |
| `201` | Created — POST com sucesso |
| `204` | No Content — DELETE com sucesso |
| `400` | Bad Request — JSON inválido ou datas incorretas |
| `404` | Not Found — recurso não encontrado |
| `422` | Unprocessable Entity — pet ou casa não existe |

### Testando com arquivo `.http` (VS Code REST Client)

Crie um arquivo `testes.http` na raiz do projeto:

```http
### Listar todas
GET http://localhost:8080/reservas/api

### Buscar por id
GET http://localhost:8080/reservas/api/1

### Criar
POST http://localhost:8080/reservas/api
Content-Type: application/json

{
  "dataInicio": "2025-07-10",
  "dataFim": "2025-07-15",
  "pet": { "id": 1 },
  "casa": { "id": 1 }
}

### Atualizar
PUT http://localhost:8080/reservas/api/1
Content-Type: application/json

{
  "dataInicio": "2025-08-01",
  "dataFim": "2025-08-05",
  "pet": { "id": 1 },
  "casa": { "id": 2 }
}

### Deletar
DELETE http://localhost:8080/reservas/api/1
```

---

## Cliente REST (T8)

Projeto separado em `PetBnBClient/` que consome a API acima usando `RestClient`.

```bash
cd PetBnBClient
mvn spring-boot:run
```

Acesse em `http://localhost:8081`

> O projeto principal (porta 8080) precisa estar rodando antes de iniciar o cliente.

---

## Internacionalização

O sistema suporta Português (BR) e Inglês. Para trocar o idioma, use o parâmetro `lang` na URL:

```
http://localhost:8080/?lang=pt_BR
http://localhost:8080/?lang=en
```

Os arquivos de mensagens estão em:
- `src/main/resources/messages_pt_BR.properties`
- `src/main/resources/messages_en.properties`

---

## Fluxo de navegação por perfil

```
Login
 ├── ADMIN        → /usuario/lista     (gerencia tutores e anfitriões)
 ├── TUTOR        → /pets              (gerencia pets → seleciona pet → faz reserva)
 └── ANFITRIÃO    → /casas             (gerencia suas casas + vê outras disponíveis)
```

---

## Observações

- O banco é criado automaticamente pelo Hibernate (`ddl-auto=update`). Na primeira execução, todas as tabelas são geradas.
- Para **recriar o banco do zero** (limpar todos os dados):
  ```sql
  DROP DATABASE "PetBnB";
  CREATE DATABASE "PetBnB" OWNER petbnbuser;
  ```
  Depois reinicie a aplicação — o admin será recriado automaticamente.
- Senhas são armazenadas com **BCrypt** — nunca em texto puro.
- O projeto não requer Tomcat externo; o Spring Boot embute o servidor.
