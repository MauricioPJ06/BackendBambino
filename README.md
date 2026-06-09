# Backend Bambino

Backend de Bambino Chicken con Spring Boot, autenticación JWT y autorización por roles persistidos en base de datos.

## Requisitos

- Java 21
- Maven 3.9+ (o `./mvnw`)
- MySQL accesible

## Dependencias clave

- `org.projectlombok:lombok`: reduce boilerplate (getters/setters/builders).
- `org.apache.poi:poi-ooxml`: lectura y escritura de archivos Excel (`.xlsx`).
- `com.google.guava:guava`: utilidades avanzadas de colecciones y helpers.
- `org.apache.commons:commons-lang3`: utilidades generales (`StringUtils`, `NumberUtils`, etc).
- `ch.qos.logback:logback-classic`: logging configurable por niveles y appenders.

## Pruebas (TDD)

- Base de testing: `org.springframework.boot:spring-boot-starter-test`.
- Framework de pruebas: `org.junit.jupiter:junit-jupiter`.
- Mocks para aislar unidades: `org.mockito:mockito-core`.
- Soporte de seguridad en tests: `org.springframework.security:spring-security-test`.

Ejecutar pruebas:

```bash
./mvnw test
```

## Seguridad (actual)

- El login real usa `JWT` + usuarios + roles desde la base de datos.
- Se eliminó la configuración de usuario por defecto de Spring Security (`spring.security.user.*`).
- Se eliminó `httpBasic()` de `SecurityConfig`, por lo que no existe Basic Auth por defecto.

## Variables de entorno requeridas

Definidas en `src/main/resources/application.properties`:

- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `SPRING_MAIL_USERNAME`
- `SPRING_MAIL_PASSWORD`
- `SPRING_MAIL_FROM`
- `UPLOAD_DIR` (opcional, default: `/app/uploads`)
- `CULQI_PUBLIC_KEY`
- `CULQI_SECRET_KEY`
- `CULQI_API_BASE_URL` (opcional, default: `https://api.culqi.com`)
- `CULQI_CHECKOUT_SCRIPT_URL` (opcional, default: `https://js.culqi.com/checkout-js`)
- `CULQI_CURRENCY` (opcional, default: `PEN`)

Nota: el backend usa uploads locales mediante `UPLOAD_DIR`.

## Configuración local

1. Copiar plantilla:

```bash
cp .env.example .env
```

2. Editar `.env` con tus valores reales.

3. Compilar:

```bash
./mvnw -q -DskipTests package
```

4. Ejecutar:

```bash
./mvnw spring-boot:run
```

## Docker/VPS

1. Copiar plantilla y completar secretos reales:

```bash
cp .env.example .env
```

2. Inyectar variables del `.env` al contenedor (por `--env-file`, `docker-compose`, o variables del VPS).

3. Asegurar volumen o ruta persistente para uploads (`UPLOAD_DIR`, por ejemplo `/app/uploads`).

4. Construir y ejecutar imagen del backend con esas variables cargadas.

## Archivos de entorno y git

- `.env` y `.env.*` están ignorados por git.
- `!.env.example` está permitido para versionar la plantilla.
- Nunca subir `.env` real ni secretos del VPS/SMTP/BD.
