# PetBnB

## Descrição
Projeto web Java com Spring Boot, JSP e persistência em PostgreSQL. Este trabalho implementa CRUD de usuários, casas e pets usando uma aplicação Spring Boot com frontend JSP.

## Requisitos
- Maven 3+
- Java 17+ (JDK 17 ou superior)
- PostgreSQL instalado e em execução

## Build
Para compilar e gerar o artefato:

```bash
cd /home/nati/Documentos/codes/DSW1/PetBnB
mvn clean package
```

O artefato gerado estará em `target/PetBnB.war`.

## Execução
Você pode iniciar o projeto com o Maven ou usando o WAR gerado.

### Com Maven
```bash
mvn spring-boot:run
```

### Se a porta `8080` estiver em uso
```bash
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8081
```

### Usando o WAR gerado
```bash
java -jar target/PetBnB.war
```

Acesse a aplicação em `http://localhost:8080/`

## Banco de dados
O script de criação está em `db/PostGre/create.sql`.

### Criar o banco de dados
```bash
PGPASSWORD=petpet psql -U petbnbuser -h localhost -f db/PostGre/create.sql
```

## Configuração de conexão
Valores padrão usados pela aplicação:
- Banco: `PetBnB`
- Host: `localhost`
- Porta: `5432`
- Usuário padrão: `petbnbuser`
- Senha padrão: `petpet`

A aplicação também aceita variáveis de ambiente:
- `POSTGRES_HOST`
- `POSTGRES_USER`
- `POSTGRES_PASSWORD`
- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`

## Observações
- A aplicação usa Spring Boot com JSPs em `src/main/webapp`.
- Se o banco não aceitar a senha padrão, ajuste `POSTGRES_PASSWORD` ou `SPRING_DATASOURCE_PASSWORD` antes de iniciar.
- O projeto não exige Tomcat externo para execução com `mvn spring-boot:run` ou `java -jar target/PetBnB.war`.
