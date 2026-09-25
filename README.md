# API para Gestión de Inventarios

API REST desarrollada con Spring Boot para la gestión de inventarios.

El sistema permite administrar un catálogo de productos, proveedores y movimientos de stock. Cada entrada o salida de inventario debe actualizar la cantidad disponible del producto correspondiente.

## Tecnologías utilizadas

- Java 21
- Spring Boot
- Spring Web MVC
- Spring Data JPA
- Hibernate
- PostgreSQL
- Maven
- Lombok
- Jakarta Validation
- Postman

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

Para el módulo de productos se implementó:

```text
ProductoController
        ↓
ProductoService
        ↓
ProductoServiceImpl
        ↓
ProductoRepository
        ↓
PostgreSQL
```

## Módulo de Productos

El módulo de productos permite realizar las siguientes operaciones:

| Método | Endpoint | Descripción |
|---|---|---|
| GET | `/api/productos` | Obtiene todos los productos |
| GET | `/api/productos/{id}` | Obtiene un producto por ID |
| POST | `/api/productos` | Crea un nuevo producto |
| PUT | `/api/productos/{id}` | Actualiza un producto |
| DELETE | `/api/productos/{id}` | Elimina un producto |

## Entidad Producto

La entidad `Producto` contiene los siguientes atributos:

- `id`
- `codigo`
- `nombre`
- `descripcion`
- `precio`
- `cantidadStock`

El código de cada producto debe ser único.

## Validaciones

El módulo de productos incluye validaciones para evitar información incorrecta.

Entre ellas:

- El código es obligatorio.
- El nombre es obligatorio.
- El precio debe ser mayor que cero.
- La cantidad de stock no puede ser negativa.
- El código del producto no puede repetirse.

## Manejo de errores

La aplicación utiliza manejo global de excepciones mediante `@ControllerAdvice`.

Algunos códigos de respuesta utilizados son:

| Código | Significado |
|---|---|
| 200 | Operación realizada correctamente |
| 201 | Recurso creado correctamente |
| 204 | Recurso eliminado correctamente |
| 400 | Datos inválidos |
| 404 | Recurso no encontrado |
| 409 | Conflicto por recurso duplicado |

## Base de datos

El proyecto utiliza PostgreSQL.

Nombre de la base de datos utilizada durante el desarrollo:

```text
inventario_db
```

Configuración de ejemplo en `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/inventario_db
spring.datasource.username=postgres
spring.datasource.password=TU_CONTRASENA

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

> Por seguridad, cada desarrollador debe utilizar su propia contraseña de PostgreSQL.

## Cómo ejecutar el proyecto

### Requisitos

Antes de ejecutar el proyecto se debe tener instalado:

- Java 21
- PostgreSQL
- Maven o utilizar Maven Wrapper
- Un IDE compatible con Java, por ejemplo IntelliJ IDEA

### 1. Crear la base de datos

En PostgreSQL ejecutar:

```sql
CREATE DATABASE inventario_db;
```

### 2. Configurar la conexión

Editar el archivo:

```text
src/main/resources/application.properties
```

y colocar las credenciales correspondientes de PostgreSQL.

### 3. Ejecutar la aplicación

Desde IntelliJ IDEA se puede ejecutar la clase:

```text
ApiParaGestionDeInventariosApplication
```

También se puede ejecutar desde consola con Maven Wrapper.

En Windows:

```bash
mvnw.cmd spring-boot:run
```

En Linux o macOS:

```bash
./mvnw spring-boot:run
```

La aplicación se ejecutará por defecto en:

```text
http://localhost:8080
```

## Ejemplo de creación de producto

Petición:

```http
POST /api/productos
```

Body:

```json
{
  "codigo": "PROD-001",
  "nombre": "Teclado mecánico",
  "descripcion": "Teclado mecánico RGB",
  "precio": 45.99,
  "cantidadStock": 10
}
```

Respuesta esperada:

```json
{
  "id": 1,
  "codigo": "PROD-001",
  "nombre": "Teclado mecánico",
  "descripcion": "Teclado mecánico RGB",
  "precio": 45.99,
  "cantidadStock": 10
}
```

## Estado actual

Actualmente se encuentra implementado y probado el CRUD completo del módulo de productos, incluyendo:

- Creación de productos.
- Consulta general.
- Consulta por ID.
- Actualización.
- Eliminación.
- Validaciones.
- Manejo de productos inexistentes.
- Manejo de códigos duplicados.

## Pendientes

- Integración del módulo de proveedores.
- Integración de entradas y salidas de stock.
- Diagrama de clases completo.
- Dockerización.
- Pipeline CI/CD local con `act`.
