# Users Management — Spring Boot, Arquitectura Hexagonal y DDD

Aplicación de gestión de usuarios construida con Java 17 y Spring Boot. La API REST es el punto de entrada activo. El código de la antigua CLI se conserva como adaptador inactivo y no posee un contenedor de dependencias independiente.

Spring es el único *composition root*: `Main` inicia el contexto y las dependencias se resuelven mediante configuración y component scanning de Spring.

## Verificación

```bash
./mvnw clean test
./mvnw clean package
```

En Windows se puede utilizar `mvnw.cmd`.

## Configuración (variables de entorno / `.env`)

Todas las credenciales se leen de variables de entorno, con valores por defecto para desarrollo local.
Para no escribir claves en `application.properties` (que sí se sube a GitHub):

```bash
copy .env.example .env      # Windows  (Linux/Mac: cp .env.example .env)
```

y edite `.env` con sus valores (BD, correo Gmail y clave de aplicación, JWT). El archivo `.env` está en `.gitignore`.

| Variable | Descripción | Por defecto |
|---|---|---|
| `DB_ENGINE` | `mysql` o `postgresql` (elige el adaptador de persistencia) | `mysql` |
| `DB_HOST` / `DB_PORT` / `DB_NAME` | Servidor de base de datos | `localhost` / `3306` / `crud_usuarios` |
| `DB_USERNAME` / `DB_PASSWORD` | Credenciales de BD | `root` / `1234567` |
| `DB_SSL_MODE` | Solo PostgreSQL: `disable` o `require` (Supabase) | `disable` |
| `SMTP_USERNAME` / `SMTP_PASSWORD` | Cuenta Gmail y clave de aplicación | — |
| `JWT_SECRET` | Secreto Base64 de ≥ 32 bytes | valor de desarrollo |
| `PORT` | Puerto HTTP (Render lo define solo) | `8080` |

Usuario administrador inicial creado por los scripts SQL: `admin@example.com` / `Admin1234!`.
Los usuarios nuevos quedan en estado `PENDING`; para que puedan hacer login, el admin debe
cambiarlos a `ACTIVE` con `PUT /api/users/{id}`.

## Ejecución local

```bash
mvnw clean install -U
mvnw spring-boot:run
```

Swagger: http://localhost:8080/swagger-ui/index.html

## Docker

```bash
# App + MySQL
docker compose up --build

# App + PostgreSQL
docker compose -f docker-compose.postgres.yml up --build

# Detener (agregue -v para borrar también los datos)
docker compose down
```

El `Dockerfile` es multi-etapa: compila con Maven dentro de un contenedor y ejecuta el JAR sobre una imagen solo-JRE.
Los scripts `schema.sql` / `schema-postgresql.sql` se ejecutan automáticamente al crear la BD por primera vez.

## PostgreSQL

- Adaptador: `UserRepositoryPostgreSQL` (activo con `DB_ENGINE=postgresql`); `UserRepositoryMySQL` se activa con `DB_ENGINE=mysql`.
  Ambos implementan los mismos puertos de salida, así que dominio y aplicación no cambian.
- Script: `src/main/resources/schema-postgresql.sql`.
- **Supabase**: *Connect* → *Session pooler* (la conexión directa es solo IPv6 y Render no la alcanza).
  Use `DB_HOST=aws-…pooler.supabase.com`, `DB_PORT=5432`, `DB_NAME=postgres`,
  `DB_USERNAME=postgres.<project-ref>`, `DB_SSL_MODE=require`.

## Despliegue en Render

1. Render → **New → Web Service** → conectar el repositorio de GitHub (rama `main`).
2. **Language/Runtime: Docker** (usa el `Dockerfile` de la raíz). Instance type: *Free*.
3. En **Environment** agregar: `DB_ENGINE=postgresql`, `DB_SSL_MODE=require`, `DB_HOST`, `DB_PORT=5432`,
   `DB_NAME=postgres`, `DB_USERNAME`, `DB_PASSWORD` (de Supabase), `SMTP_USERNAME`, `SMTP_PASSWORD`,
   `SMTP_FROM_ADDRESS`, `JWT_SECRET`.
4. **Deploy** y abrir `https://<servicio>.onrender.com/swagger-ui/index.html`.

Alternativa: **New → Blueprint** usando `render.yaml`.

> Nota: desde septiembre de 2025 los Web Services **gratuitos** de Render bloquean los puertos SMTP (25, 465, 587).
> En el plan free el usuario se guarda en la BD, pero la petición responde con error de correo (timeout de 10 s). Para enviar correo desde Render
> se necesita una instancia de pago; en local y en Docker funciona normalmente.
