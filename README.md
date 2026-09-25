# API para Gestión de Inventarios

Proyecto desarrollado en Java con Spring Boot para la gestión de inventarios.

La API permite administrar productos mediante operaciones CRUD y está preparada para integrarse con otros módulos del sistema, como proveedores y movimientos de inventario.

---

## Tecnologías utilizadas

- Java 21
- Spring Boot
- Spring Data JPA
- Spring Validation
- PostgreSQL
- Maven
- Lombok
- Docker
- Git
- GitHub Actions
- act
- Postman
- IntelliJ IDEA

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
└── main
    ├── java
    │   └── sv
    │       └── ues
    │           └── inventarioapi
    │               ├── controller
    │               ├── exception
    │               ├── model
    │               ├── repository
    │               ├── service
    │               │   └── impl
    │               └── ApiParaGestionDeInventariosApplication.java
    └── resources
        └── application.properties
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

---

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

Ejemplo:

```text
GET http://localhost:8080/api/productos
```

Respuesta esperada:

```json
[
  {
    "id": 1,
    "codigo": "PROD-001",
    "nombre": "Producto de ejemplo",
    "descripcion": "Descripción del producto",
    "precio": 10.50,
    "cantidadStock": 15
  }
]
```

---

## Obtener producto por ID

```http
GET /api/productos/{id}
```

Ejemplo:

```text
GET http://localhost:8080/api/productos/1
```

---

## Crear producto

```http
POST /api/productos
```

Ejemplo de cuerpo JSON:

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

---

## Actualizar producto

```http
PUT /api/productos/{id}
```

Ejemplo:

```text
PUT http://localhost:8080/api/productos/1
```

Cuerpo:

```json
{
  "codigo": "PROD-001",
  "nombre": "Producto actualizado",
  "descripcion": "Descripción actualizada",
  "precio": 15.99,
  "cantidadStock": 20
}
```

Respuesta esperada:

```text
200 OK
```

---

## Eliminar producto

```http
DELETE /api/productos/{id}
```

Ejemplo:

```text
DELETE http://localhost:8080/api/productos/1
```

Respuesta esperada:

```text
204 No Content
```

---

# Manejo de errores

El proyecto utiliza manejo global de excepciones mediante `@ControllerAdvice`.

Se manejan errores como:

- Producto no encontrado.
- Código de producto duplicado.
- Datos inválidos.
- Validaciones de campos.

---

## Producto no encontrado

Ejemplo:

```text
GET /api/productos/999
```

Respuesta:

```json
{
  "timestamp": "2026-09-25T10:00:00",
  "status": 404,
  "error": "Not Found",
  "mensaje": "Producto no encontrado con id: 999"
}
```

---

## Código de producto duplicado

Respuesta:

```json
{
  "timestamp": "2026-09-25T10:00:00",
  "status": 409,
  "error": "Conflict",
  "mensaje": "Ya existe un producto con el código: PROD-001"
}
```

---

## Error de validación

Ejemplo de datos inválidos:

```json
{
  "codigo": "",
  "nombre": "",
  "descripcion": "Producto inválido",
  "precio": -20,
  "cantidadStock": -5
}
```

La API responde con:

```text
400 Bad Request
```

---

# Base de datos

El proyecto utiliza PostgreSQL.

Base de datos utilizada:

```text
inventario_db
```

Puerto predeterminado:

```text
5432
```

---

# Variables de entorno

Las credenciales de PostgreSQL no se almacenan directamente en el código fuente.

La aplicación utiliza las siguientes variables de entorno:

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

---

## Configuración de application.properties

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

## Requisitos

- Java 21
- PostgreSQL
- Maven Wrapper incluido en el proyecto

---

## Ejecutar desde IntelliJ IDEA

1. Abrir el proyecto en IntelliJ IDEA.
2. Configurar las variables de entorno:

```text
DB_URL
DB_USERNAME
DB_PASSWORD
```

3. Ejecutar:

```text
ApiParaGestionDeInventariosApplication.java
```

La aplicación estará disponible en:

```text
http://localhost:8080
```

---

## Ejecutar con Maven

En Windows:

```powershell
.\mvnw spring-boot:run
```

En Linux:

```bash
./mvnw spring-boot:run
```

---

# Docker

El proyecto incluye un `Dockerfile` para compilar y ejecutar la aplicación utilizando contenedores.

## Construir imagen Docker

Desde la raíz del proyecto:

```powershell
docker build -t inventario-api .
```

Verificar la imagen:

```powershell
docker images
```

Deberá aparecer:

```text
inventario-api
```

---

## Ejecutar contenedor Docker

Si PostgreSQL se encuentra instalado directamente en Windows, desde Docker se utiliza:

```text
host.docker.internal
```

Ejemplo:

```powershell
docker run --name inventario-api-container `
  -p 8080:8080 `
  -e DB_URL=jdbc:postgresql://host.docker.internal:5432/inventario_db `
  -e DB_USERNAME=postgres `
  -e DB_PASSWORD=TU_CONTRASEÑA `
  inventario-api
```

Después puede probarse:

```text
GET http://localhost:8080/api/productos
```

---

## Detener el contenedor

```powershell
docker stop inventario-api-container
```

## Iniciar nuevamente el contenedor

```powershell
docker start inventario-api-container
```

## Eliminar el contenedor

```powershell
docker rm inventario-api-container
```

---

# Integración continua local

El proyecto cuenta con un workflow de GitHub Actions ubicado en:

```text
.github/workflows/ci.yml
```

Este pipeline permite comprobar automáticamente que el proyecto puede compilarse y generar su imagen Docker correctamente.

## Flujo del pipeline

```text
Código fuente
    ↓
Git
    ↓
GitHub Actions
    ↓
Java 21
    ↓
Maven
    ↓
Compilación
    ↓
Docker Build
```

---

## Workflow utilizado

```yaml
name: CI Inventario API

on:
  push:
    branches: [ "main", "master" ]
  pull_request:
    branches: [ "main", "master" ]

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
      - name: Descargar código
        uses: actions/checkout@v4

      - name: Configurar Java 21
        uses: actions/setup-java@v4
        with:
          distribution: temurin
          java-version: '21'
          cache: maven

      - name: Dar permisos a Maven Wrapper
        run: chmod +x mvnw

      - name: Compilar proyecto
        run: ./mvnw clean package -DskipTests

      - name: Construir imagen Docker
        run: docker build -t inventario-api .
```

---

# Ejecución local del pipeline con act

`act` permite ejecutar GitHub Actions localmente utilizando Docker.

## Instalar act en Windows

```powershell
winget install --id nektos.act -e
```

Verificar:

```powershell
act --version
```

## Ejecutar pipeline

Primero debe estar Docker Desktop iniciado.

Después, desde la raíz del proyecto:

```powershell
act
```

En la primera ejecución puede solicitar seleccionar una imagen. Para este proyecto puede utilizarse:

```text
Medium
```

Si todo funciona correctamente debe finalizar con:

```text
Job succeeded
```

---

# Git

El proyecto utiliza Git para el control de versiones.

Inicializar repositorio:

```powershell
git init
```

Agregar archivos:

```powershell
git add .
```

Crear commit:

```powershell
git commit -m "Configuracion inicial API de Inventarios"
```

Verificar estado:

```powershell
git status
```

---

# Diagrama de clases

El proyecto incluye el archivo:

```text
diagrama-clases.puml
```

El diagrama utiliza PlantUML y representa inicialmente:

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

El diagrama general podrá ampliarse conforme se integren los demás módulos del sistema.

---

# Estructura general del proyecto

```text
API_para_Gestion_de_Inventarios
│
├── .github
│   └── workflows
│       └── ci.yml
│
├── .mvn
├── src
│   ├── main
│   │   ├── java
│   │   │   └── sv.ues.inventarioapi
│   │   │       ├── controller
│   │   │       ├── exception
│   │   │       ├── model
│   │   │       ├── repository
│   │   │       └── service
│   │   │           └── impl
│   │   └── resources
│   │       └── application.properties
│   └── test
│
├── .dockerignore
├── .gitignore
├── Dockerfile
├── README.md
├── diagrama-clases.puml
├── mvnw
├── mvnw.cmd
└── pom.xml
```

---

# Estado actual

Actualmente se encuentra implementado y probado:

- Proyecto base Spring Boot.
- Conexión con PostgreSQL.
- Entidad Producto.
- Repository de Producto.
- Service de Producto.
- Implementación del Service.
- Controller de Producto.
- CRUD completo.
- Validaciones.
- Manejo global de excepciones.
- Validación de código duplicado.
- Lombok.
- Pruebas de endpoints mediante Postman.
- Dockerfile.
- Construcción de imagen Docker.
- Ejecución de la API dentro de Docker.
- Conexión desde Docker hacia PostgreSQL.
- Git.
- Workflow de GitHub Actions.
- Ejecución local del workflow con act.
- Diagrama de clases inicial.
- Documentación inicial.

---

# Próximas integraciones

El sistema puede ampliarse con:

- Proveedores.
- Relación Producto-Proveedor.
- Entradas de inventario.
- Salidas de inventario.
- Actualización automática del stock.
- Integración completa entre todos los módulos.

---

# Proyecto académico

Proyecto desarrollado como parte de la asignatura de Programación Orientada a Objetos.

Universidad de El Salvador.

## API para Gestión de Inventarios
