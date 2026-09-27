# API para Gestion de Inventarios

Proyecto academico desarrollado en Java con Spring Boot para la gestion de inventarios.

La API administra productos y proveedores mediante operaciones CRUD, modela la relacion muchos a muchos entre ambos, y registra movimientos de inventario (entradas y salidas) que actualizan automaticamente el stock disponible de cada producto.

---

## Tecnologias utilizadas

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
    v
Service
    v
Repository
    v
Base de datos
```

Estructura principal:

```text
src
+-- main
|   +-- java
|   |   +-- sv
|   |       +-- ues
|   |           +-- inventarioapi
|   |               +-- controller
|   |               +-- dto
|   |               +-- exception
|   |               +-- model
|   |               +-- repository
|   |               +-- service
|   |               |   +-- impl
|   |               +-- ApiParaGestionDeInventariosApplication.java
|   +-- resources
|       +-- application.properties
|
+-- test
    +-- java
    |   +-- sv
    |       +-- ues
    |           +-- inventarioapi
    |               +-- ApiParaGestionDeInventariosApplicationTests.java
    |               +-- service
    |                   +-- ProductoServiceImplTest.java
    |                   +-- ProveedorServiceImplTest.java
    |                   +-- MovimientoStockServiceImplTest.java
    +-- resources
        +-- application-test.properties
```

---

# Modulo de Productos

Implementado, documentado y probado.

La entidad `Producto` contiene los siguientes atributos:

```text
id
codigo
nombre
descripcion
precio
cantidadStock
proveedores (relacion ManyToMany con Proveedor)
```

## Validaciones de Producto

- El codigo es obligatorio.
- El codigo no puede superar los 50 caracteres.
- El codigo debe ser unico.
- El nombre es obligatorio.
- El nombre no puede superar los 100 caracteres.
- La descripcion no puede superar los 255 caracteres.
- El precio es obligatorio.
- El precio debe ser mayor que cero.
- La cantidad de stock es obligatoria.
- La cantidad de stock no puede ser negativa.

---

# Modulo de Proveedores

Implementado, documentado y probado.

La entidad `Proveedor` contiene los siguientes atributos:

```text
id
nombre
contacto
telefono
email
direccion
ncrEmpresa
nitContribuyente
activo
productos (relacion ManyToMany con Producto)
```

## Validaciones de Proveedor

- El nombre es obligatorio (maximo 150 caracteres).
- El email es obligatorio y debe tener formato valido.
- La direccion es obligatoria (maximo 255 caracteres).
- El NCR de la empresa es obligatorio y unico.
- El NIT del contribuyente es obligatorio y unico.
- El contacto y el telefono son opcionales.

## Relacion Producto-Proveedor

La relacion muchos a muchos entre `Producto` y `Proveedor` se materializa en la tabla intermedia `producto_proveedor`, generada automaticamente por JPA mediante `@JoinTable` en la entidad `Producto`.

## Baja logica (soft delete)

Un proveedor no se elimina fisicamente de la base de datos. Al invocar el endpoint de baja, su campo `activo` cambia a `false`, preservando el historial y evitando perder la trazabilidad con productos ya asociados.

---

# Modulo de Movimientos de Stock

Implementado, documentado y probado.

La entidad `MovimientoStock` contiene los siguientes atributos:

```text
id
tipoMovimiento (ENTRADA o SALIDA)
cantidad
fechaMovimiento
producto (referencia al Producto afectado)
```

## Logica de negocio

Cada vez que se registra un movimiento, el sistema actualiza automaticamente el stock del producto asociado dentro de la misma transaccion (`@Transactional`):

- **ENTRADA**: el stock del producto aumenta en la cantidad indicada.
- **SALIDA**: el sistema valida que exista stock suficiente antes de disminuirlo. Si la cantidad solicitada supera el stock disponible, la operacion se rechaza y no se guarda ningun cambio.

Si no se envia una fecha en la solicitud, el sistema la asigna automaticamente con la fecha y hora actuales.

## Validaciones de MovimientoStock

- El tipo de movimiento es obligatorio (`ENTRADA` o `SALIDA`).
- La cantidad es obligatoria y debe ser mayor que 0.
- El producto referenciado debe existir; si no, la operacion se rechaza.

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
  "descripcion": "Descripcion del producto",
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

# Endpoints de Proveedores

URL base:

```text
http://localhost:8080/api/proveedores
```

## Obtener todos los proveedores

```http
GET /api/proveedores
```

## Obtener proveedor por ID

```http
GET /api/proveedores/{id}
```

## Crear proveedor

```http
POST /api/proveedores
```

Ejemplo:

```json
{
  "nombre": "Distribuidora de papel de oficina",
  "contacto": "Raul Beltran",
  "telefono": "2522 0001",
  "email": "ventas@distribuidorapapel.com",
  "direccion": "Avenida Revolucion, San Salvador Centro",
  "ncrEmpresa": "1234-0",
  "nitContribuyente": "0614-000001-120-0"
}
```

Respuesta esperada:

```text
201 Created
```

## Actualizar proveedor

```http
PUT /api/proveedores/{id}
```

Respuesta esperada:

```text
200 OK
```

## Inactivar proveedor (baja logica)

```http
DELETE /api/proveedores/{id}
```

Respuesta esperada:

```text
204 No Content
```

---

# Endpoints de Movimientos de Stock

URL base:

```text
http://localhost:8080/api/movimientos-stock
```

## Registrar un movimiento (entrada o salida)

```http
POST /api/movimientos-stock
```

Ejemplo (entrada):

```json
{
  "tipoMovimiento": "ENTRADA",
  "cantidad": 20,
  "producto": { "id": 1 }
}
```

Ejemplo (salida):

```json
{
  "tipoMovimiento": "SALIDA",
  "cantidad": 5,
  "producto": { "id": 1 }
}
```

Respuesta esperada:

```text
201 Created
```

Si la salida solicitada supera el stock disponible, el sistema responde con:

```text
400 Bad Request
```

## Consultar historial de movimientos

```http
GET /api/movimientos-stock
```

Respuesta esperada:

```text
200 OK
```

---

# Manejo de errores

El proyecto utiliza manejo global de excepciones mediante `@ControllerAdvice`.

Se manejan:

- Recurso no encontrado (producto, proveedor o movimiento).
- Codigo de producto duplicado.
- NIT o NCR de proveedor duplicado.
- Stock insuficiente al registrar una salida.
- Datos invalidos.
- Validaciones de campos.

Codigos principales:

```text
400 Bad Request   (validaciones y stock insuficiente)
404 Not Found     (recurso no encontrado)
409 Conflict      (duplicados)
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

La aplicacion utiliza:

```text
DB_URL
DB_USERNAME
DB_PASSWORD
```

Ejemplo:

```text
DB_URL=jdbc:postgresql://localhost:5432/inventario_db
DB_USERNAME=postgres
DB_PASSWORD=TU_CONTRASENA
```

No se recomienda guardar contrasenas reales dentro del repositorio.

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

# Ejecucion local

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

Linux / Chromebook (Crostini):

```bash
./mvnw spring-boot:run
```

---

# Pruebas automaticas

El proyecto incluye:

- 8 pruebas unitarias para `ProductoServiceImpl`.
- 11 pruebas unitarias para `ProveedorServiceImpl`.
- 5 pruebas unitarias para `MovimientoStockServiceImpl`.
- 1 prueba de carga del contexto de Spring Boot.
- Total: 25 pruebas automatizadas.

Las pruebas unitarias utilizan Mockito para simular los repositorios.

Escenarios cubiertos (Producto):

- Obtener todos los productos.
- Obtener un producto por ID.
- Excepcion cuando el producto no existe.
- Crear producto.
- Rechazar codigo duplicado.
- Actualizar producto.
- Rechazar codigo duplicado durante actualizacion.
- Eliminar producto.

Escenarios cubiertos (Proveedor):

- Obtener todos los proveedores.
- Obtener un proveedor por ID.
- Excepcion cuando el proveedor no existe.
- Crear proveedor.
- Rechazar NIT duplicado.
- Rechazar NCR duplicado.
- Actualizar proveedor.
- Rechazar NIT perteneciente a otro proveedor durante actualizacion.
- Inactivar proveedor (baja logica).
- Excepcion al inactivar un proveedor inexistente.

Escenarios cubiertos (Movimiento de Stock):

- Registrar entrada y aumentar el stock del producto.
- Registrar salida y disminuir el stock del producto.
- Rechazar salida cuando el stock es insuficiente.
- Excepcion cuando el producto referenciado no existe.
- Obtener el historial de movimientos.

Ejecutar:

```bash
./mvnw test
```

Resultado esperado:

```text
Tests run: 25, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

---

# Perfil de pruebas con H2

Las pruebas usan H2 en memoria para no depender de PostgreSQL local ni de credenciales personales.

Archivo:

```text
src/test/resources/application-test.properties
```

Configuracion:

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

```bash
docker build -t inventario-api .
```

Ejecutar contenedor conectado al PostgreSQL de Windows:

```powershell
docker run --name inventario-api-container `
  -p 8080:8080 `
  -e DB_URL=jdbc:postgresql://host.docker.internal:5432/inventario_db `
  -e DB_USERNAME=postgres `
  -e DB_PASSWORD=TU_CONTRASENA `
  inventario-api
```

Comandos utiles:

```bash
docker stop inventario-api-container
docker start inventario-api-container
docker rm inventario-api-container
```

---

# Integracion continua

El workflow se encuentra en:

```text
.github/workflows/ci.yml
```

Flujo actual:

```text
PostgreSQL temporal
        v
Java 21
        v
Maven
        v
Pruebas automaticas
        v
Compilacion
        v
Docker Build
```

El pipeline utiliza:

```bash
./mvnw clean verify
```

La imagen Docker se construye unicamente si las pruebas pasan correctamente.

---

# Ejecucion local del pipeline con act

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

Una ejecucion correcta termina con:

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

```bash
git clone https://github.com/alejandrooovz/API_para_Gestion_de_Inventarios.git
```

Guardar cambios:

```bash
git add .
git commit -m "Descripcion del cambio"
git push
```

---

# Diagrama de clases

<img width="2117" height="800" alt="diagrama-clases" src="https://github.com/user-attachments/assets/2f545f7b-8150-4c0d-91f2-27cb6c94230e" />

Archivo:

```text
diagrama-clases.puml
```

Representa los tres modulos integrados:

```text
Producto
ProductoController
ProductoService
ProductoServiceImpl
ProductoRepository

Proveedor
ProveedorController
ProveedorService
ProveedorServiceImpl
ProveedorRepository
ProveedorRequestDTO
ProveedorResponseDTO

MovimientoStock
TipoMovimiento
MovimientoStockController
MovimientoStockService
MovimientoStockServiceImpl
MovimientoStockRepository

ResourceNotFoundException
DuplicateResourceException
StockInsuficienteException
GlobalExceptionHandler
ErrorResponse
```

---

# Estado actual

Actualmente se encuentra implementado, documentado y probado:

- Proyecto base Spring Boot.
- Java 21 y Maven.
- PostgreSQL.
- Variables de entorno.
- CRUD completo de Producto.
- CRUD completo de Proveedor.
- Relacion ManyToMany entre Producto y Proveedor (tabla producto_proveedor).
- Baja logica de Proveedor (campo activo).
- DTOs de request/response para Proveedor.
- Registro de movimientos de stock (entradas y salidas).
- Actualizacion automatica del stock a partir de los movimientos.
- Validacion de stock insuficiente antes de una salida.
- Validaciones con Jakarta Validation.
- Manejo global de excepciones.
- Validacion de datos duplicados (codigo, NIT, NCR).
- Lombok.
- Pruebas manuales con Postman.
- Dockerfile multi-stage.
- Imagen Docker probada.
- Ejecucion de la API dentro de Docker.
- Git y GitHub, con flujo de Pull Requests.
- GitHub Actions.
- Ejecucion local con act.
- CI con pruebas automaticas.
- PostgreSQL temporal en CI.
- 25 pruebas unitarias en total (Producto + Proveedor + Movimiento de Stock).
- 1 prueba de contexto Spring Boot.
- H2 en memoria para pruebas.
- Perfil `test`.
- Diagrama de clases con los tres modulos.
- JavaDoc y comentarios tecnicos.
- README actualizado.

---

# Trabajo pendiente de integracion grupal

La base tecnica y los tres modulos (Producto, Proveedor, Movimiento de Stock) estan integrados en `main`.

Queda pendiente:

- Agregar DTOs de request/response para Movimiento de Stock, actualmente el endpoint recibe la entidad directamente.
- Pruebas de integracion end-to-end del sistema completo (por ejemplo, con `@SpringBootTest` y `MockMvc`).
- Revision final del README y del diagrama antes de la entrega.

---

# Proyecto academico API para Gestion de Inventarios 

Proyecto desarrollado como parte de la asignatura de Programación Orientada a Objetos.  
Universidad de El Salvador.

## Integrantes

* **Ramos Martínez, Roberto Ernesto** — `RM04123`
* **Hernández Belloso, Kevin Daniel** — `HH25003`
* **Quintana Vásquez, Danilo Alejandro** — `QV22002`
* **Segovia Romero, Javier de Jesús** — `SR22025`
