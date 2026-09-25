package sv.ues.inventarioapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sv.ues.inventarioapi.model.Producto;
import sv.ues.inventarioapi.service.ProductoService;
import jakarta.validation.Valid;

import java.util.List;

/**
 * Controlador REST encargado de exponer los endpoints
 * relacionados con la gestión de productos.
 *
 * Recibe las solicitudes HTTP y delega la lógica de negocio
 * a ProductoService.
 */
@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;

    /**
     * Constructor utilizado para inyectar el servicio de productos.
     *
     * @param productoService servicio encargado de gestionar productos.
     */
    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    /**
     * Obtiene todos los productos registrados.
     *
     * Endpoint:
     * GET /api/productos
     *
     * @return lista de productos con estado HTTP 200.
     */
    @GetMapping
    public ResponseEntity<List<Producto>> obtenerTodos() {
        return ResponseEntity.ok(productoService.obtenerTodos());
    }

    /**
     * Obtiene un producto mediante su identificador.
     *
     * Endpoint:
     * GET /api/productos/{id}
     *
     * @param id identificador del producto.
     * @return producto encontrado con estado HTTP 200.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.obtenerPorId(id));
    }

    /**
     * Registra un nuevo producto.
     *
     * La anotación @Valid permite ejecutar las validaciones
     * definidas en la entidad Producto antes de procesar la solicitud.
     *
     * Endpoint:
     * POST /api/productos
     *
     * @param producto producto recibido en el cuerpo de la solicitud.
     * @return producto creado con estado HTTP 201.
     */
    @PostMapping
    public ResponseEntity<Producto> crear(@Valid @RequestBody Producto producto) {
        Producto productoCreado = productoService.crear(producto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productoCreado);
    }

    /**
     * Actualiza un producto existente.
     *
     * Endpoint:
     * PUT /api/productos/{id}
     *
     * @param id identificador del producto que se desea actualizar.
     * @param producto datos actualizados del producto.
     * @return producto actualizado con estado HTTP 200.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody Producto producto) {

        return ResponseEntity.ok(
                productoService.actualizar(id, producto)
        );
    }

    /**
     * Actualiza un producto existente.
     *
     * Endpoint:
     * PUT /api/productos/{id}
     *
     * @param id identificador del producto que se desea actualizar.
     * @param producto datos actualizados del producto.
     * @return producto actualizado con estado HTTP 200.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}