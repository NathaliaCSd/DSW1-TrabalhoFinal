# PetBnB

## Descrição
Projeto web Java Jakarta para o trabalho de CRUD com persistência em PostgreSQL.

## Requisitos
- Maven funcional
- Tomcat 11.x
- PostgreSQL instalado e em execução

## Build
Para compilar e gerar o arquivo WAR:

```bash
cd /home/nati/Documentos/codes/DSW1/PetBnB
mvn clean package
```

O WAR gerado estará em `target/PetBnB.war`.

## Banco de dados
O script de criação está em `db/PostGre/create.sql`.

### Comando para criar e popular o banco de dados
Execute o script para criar as tabelas e inserir os registros iniciais:

```bash
PGPASSWORD=admin psql -U petbnbuser -h localhost -f db/PostGre/create.sql
```

### Usando usuário `postgres`
```bash
PGPASSWORD=admin psql -U postgres -h localhost -f db/PostGre/create.sql
```

### Parâmetros de conexão usados pela aplicação
- Banco: `PetBnB`
- Host: `localhost`
- Porta: `5432`
- Usuário padrão: `petbnbuser`
- Senha padrão: `admin`

Se preferir usar variáveis de ambiente, a aplicação lê:
- `POSTGRES_HOST`
- `POSTGRES_USER`
- `POSTGRES_PASSWORD`

## Deploy
### Usando Tomcat via Maven
```bash
mvn tomcat7:redeploy
```

Após o comando, a aplicação estará disponível em `http://localhost:8080/PetBnB`.
