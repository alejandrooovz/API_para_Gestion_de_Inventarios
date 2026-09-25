# API para Gestión de Inventarios

Proyecto académico desarrollado en Java con Spring Boot para la gestión de inventarios.

La API permite administrar productos mediante operaciones CRUD y está preparada para integrarse con otros módulos del sistema, como proveedores, relaciones producto-proveedor y movimientos de inventario.

---

## Tecnologías utilizadas

- Java 21
- Spring Boot 4.1.1
- Spring Data JPA
- Spring Validation
- PostgreSQL
- H2 para pruebas
- Maven
- Lombok
- JUnit
- Mockito
- Docker
- Git
- GitHub
- GitHub Actions
- act
- Postman
- IntelliJ IDEA
- PlantUML

---

## Arquitectura del proyecto

El proyecto utiliza una arquitectura por capas:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
Base de datos
```

Estructura principal:

```text
src
├── main
│   ├── java
│   │   └── sv
│   │       └── ues
│   │           └── inventarioapi
│   │               ├── controller
│   │               ├── exception
│   │               ├── model
│   │               ├── repository
│   │               ├── service
│   │               │   └── impl
│   │               └── ApiParaGestionDeInventariosApplication.java
│   └── resources
│       └── application.properties
│
└── test
    ├── java
    │   └── sv
    │       └── ues
    │           └── inventarioapi
    │               ├── ApiParaGestionDeInventariosApplicationTests.java
    │               └── service
    │                   └── ProductoServiceImplTest.java
    └── resources
        └── application-test.properties
```

---

# Módulo de Productos

Actualmente se encuentra implementado el módulo principal de productos.

La entidad `Producto` contiene los siguientes atributos:

```text
id
codigo
nombre
descripcion
precio
cantidadStock
```

## Validaciones de Producto

- El código es obligatorio.
- El código no puede superar los 50 caracteres.
- El código debe ser único.
- El nombre es obligatorio.
- El nombre no puede superar los 100 caracteres.
- La descripción no puede superar los 255 caracteres.
- El precio es obligatorio.
- El precio debe ser mayor que cero.
- La cantidad de stock es obligatoria.
- La cantidad de stock no puede ser negativa.

---

# Endpoints de Productos

URL base:

```text
http://localhost:8080/api/productos
```

## Obtener todos los productos

```http
GET /api/productos
```

## Obtener producto por ID

```http
GET /api/productos/{id}
```

## Crear producto

```http
POST /api/productos
```

Ejemplo:

```json
{
  "codigo": "PROD-001",
  "nombre": "Producto de ejemplo",
  "descripcion": "Descripción del producto",
  "precio": 10.50,
  "cantidadStock": 15
}
```

Respuesta esperada:

```text
201 Created
```

## Actualizar producto

```http
PUT /api/productos/{id}
```

Respuesta esperada:

```text
200 OK
```

## Eliminar producto

```http
DELETE /api/productos/{id}
```

Respuesta esperada:

```text
204 No Content
```

---

# Manejo de errores

El proyecto utiliza manejo global de excepciones mediante `@ControllerAdvice`.

Se manejan:

- Producto no encontrado.
- Código de producto duplicado.
- Datos inválidos.
- Validaciones de campos.

Códigos principales:

```text
400 Bad Request
404 Not Found
409 Conflict
```

---

# Base de datos

El proyecto utiliza PostgreSQL como base de datos principal.

```text
Base de datos: inventario_db
Puerto: 5432
```

---

# Variables de entorno

La aplicación utiliza:

```text
DB_URL
DB_USERNAME
DB_PASSWORD
```

Ejemplo:

```text
DB_URL=jdbc:postgresql://localhost:5432/inventario_db
DB_USERNAME=postgres
DB_PASSWORD=TU_CONTRASEÑA
```

No se recomienda guardar contraseñas reales dentro del repositorio.

## application.properties

```properties
spring.application.name=inventario-api

spring.datasource.url=${DB_URL:jdbc:postgresql://localhost:5432/inventario_db}
spring.datasource.username=${DB_USERNAME:postgres}
spring.datasource.password=${DB_PASSWORD:}

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

---

# Ejecución local

## Desde IntelliJ IDEA

Configurar:

```text
DB_URL
DB_USERNAME
DB_PASSWORD
```

y ejecutar:

```text
ApiParaGestionDeInventariosApplication.java
```

## Con Maven

Windows:

```powershell
.\mvnw spring-boot:run
```

Linux:

```bash
./mvnw spring-boot:run
```

---

# Pruebas automáticas

El proyecto incluye:

- 8 pruebas unitarias para `ProductoServiceImpl`.
- 1 prueba de carga del contexto de Spring Boot.
- Total: 9 pruebas automatizadas.

Las pruebas unitarias utilizan Mockito para simular `ProductoRepository`.

Escenarios cubiertos:

- Obtener todos los productos.
- Obtener un producto por ID.
- Excepción cuando el producto no existe.
- Crear producto.
- Rechazar código duplicado.
- Actualizar producto.
- Rechazar código duplicado durante actualización.
- Eliminar producto.

Ejecutar:

```powershell
.\mvnw test
```

Resultado esperado:

```text
Tests run: 9, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

---

# Perfil de pruebas con H2

Las pruebas usan H2 en memoria para no depender de PostgreSQL local ni de credenciales personales.

Archivo:

```text
src/test/resources/application-test.properties
```

Configuración:

```properties
spring.datasource.url=jdbc:h2:mem:inventario_test;MODE=PostgreSQL;DB_CLOSE_DELAY=-1
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=false
spring.jpa.open-in-view=false
```

El perfil se activa con:

```text
@ActiveProfiles("test")
```

---

# Docker

Construir imagen:

```powershell
docker build -t inventario-api .
```

Ejecutar contenedor conectado al PostgreSQL de Windows:

```powershell
docker run --name inventario-api-container `
  -p 8080:8080 `
  -e DB_URL=jdbc:postgresql://host.docker.internal:5432/inventario_db `
  -e DB_USERNAME=postgres `
  -e DB_PASSWORD=TU_CONTRASEÑA `
  inventario-api
```

Comandos útiles:

```powershell
docker stop inventario-api-container
docker start inventario-api-container
docker rm inventario-api-container
```

---

# Integración continua

El workflow se encuentra en:

```text
.github/workflows/ci.yml
```

Flujo actual:

```text
PostgreSQL temporal
        ↓
Java 21
        ↓
Maven
        ↓
Pruebas automáticas
        ↓
Compilación
        ↓
Docker Build
```

El pipeline utiliza:

```bash
./mvnw clean verify
```

La imagen Docker se construye únicamente si las pruebas pasan correctamente.

## Workflow actual

```yaml
name: CI Inventario API

on:
  push:
    branches:
      - main
      - master

  pull_request:
    branches:
      - main
      - master

jobs:
  build:
    runs-on: ubuntu-latest

    env:
      DB_URL: jdbc:postgresql://localhost:5432/inventario_db
      DB_USERNAME: postgres
      DB_PASSWORD: postgres

    services:
      postgres:
        image: postgres:18
        env:
          POSTGRES_DB: inventario_db
          POSTGRES_USER: postgres
          POSTGRES_PASSWORD: postgres
        ports:
          - 5432:5432
        options: >-
          --health-cmd="pg_isready -U postgres -d inventario_db"
          --health-interval=10s
          --health-timeout=5s
          --health-retries=5

    steps:
      - name: Descargar código
        uses: actions/checkout@v4

      - name: Configurar Java 21
        uses: actions/setup-java@v5
        with:
          distribution: temurin
          java-version: "21"
          cache: maven

      - name: Dar permisos a Maven Wrapper
        run: chmod +x mvnw

      - name: Compilar y ejecutar pruebas
        run: ./mvnw clean verify

      - name: Construir imagen Docker
        run: docker build -t inventario-api .
```

La contraseña `postgres` del workflow pertenece únicamente al servicio PostgreSQL temporal de CI.

---

# Ejecución local del pipeline con act

Instalar:

```powershell
winget install --id nektos.act -e
```

Verificar:

```powershell
act --version
```

Ejecutar:

```powershell
act
```

Docker Desktop debe estar iniciado.

Una ejecución correcta termina con:

```text
Job succeeded
```

---

# Git y GitHub

Repositorio:

```text
https://github.com/alejandrooovz/API_para_Gestion_de_Inventarios.git
```

Clonar:

```powershell
git clone https://github.com/alejandrooovz/API_para_Gestion_de_Inventarios.git
```

Guardar cambios:

```powershell
git add .
git commit -m "Descripcion del cambio"
git push
```

---

# Diagrama de clases

Archivo:

```text
diagrama-clases.puml
```

Actualmente representa:

```text
Producto
ProductoController
ProductoService
ProductoServiceImpl
ProductoRepository
ResourceNotFoundException
DuplicateResourceException
GlobalExceptionHandler
ErrorResponse
Proveedor
ProductoProveedor
```

El diagrama deberá ampliarse al integrar los módulos del resto del equipo.

---

# Estado actual

Actualmente se encuentra implementado, documentado y probado:

- Proyecto base Spring Boot.
- Java 21 y Maven.
- PostgreSQL.
- Variables de entorno.
- CRUD completo de Producto.
- Validaciones.
- Manejo global de excepciones.
- Validación de códigos duplicados.
- Lombok.
- Pruebas manuales con Postman.
- Dockerfile multi-stage.
- Imagen Docker probada.
- Ejecución de la API dentro de Docker.
- Git y GitHub.
- GitHub Actions.
- Ejecución local con act.
- CI con pruebas automáticas.
- PostgreSQL temporal en CI.
- 8 pruebas unitarias de ProductoServiceImpl.
- 1 prueba de contexto Spring Boot.
- H2 en memoria para pruebas.
- Perfil `test`.
- Diagrama de clases inicial.
- JavaDoc y comentarios técnicos.
- README actualizado.

---

# Trabajo pendiente de integración grupal

La base técnica y el módulo de productos están terminados.

Queda pendiente:

- Integrar Proveedores.
- Integrar Producto-Proveedor.
- Integrar entradas de inventario.
- Integrar salidas de inventario.
- Actualización automática del stock.
- Pruebas de integración del sistema completo.
- Actualizar el diagrama general.
- Actualizar el README final cuando todos los módulos estén integrados.

---

# Proyecto académico

Proyecto desarrollado como parte de la asignatura de Programación Orientada a Objetos.

Universidad de El Salvador.

## API para Gestión de Inventarios
