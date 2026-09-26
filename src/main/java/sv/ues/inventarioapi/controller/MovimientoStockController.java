package sv.ues.inventarioapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sv.ues.inventarioapi.model.MovimientoStock;
import sv.ues.inventarioapi.service.MovimientoStockService;
import jakarta.validation.Valid;

import java.util.List;

/**
 * Controlador REST encargado de exponer los endpoints
 * relacionados con la gestión de movimientos de stock.
 *
 * Recibe las solicitudes HTTP y delega la lógica de negocio
 * a MovimientoStockService.
 */
@RestController
@RequestMapping("/api/movimientos-stock")
public class MovimientoStockController {

    private final MovimientoStockService movimientoStockService;

    /**
     * Constructor utilizado para inyectar el servicio de movimientos de stock.
     *
     * @param movimientoStockService servicio encargado de gestionar movimientos de stock.
     */
    public MovimientoStockController(MovimientoStockService movimientoStockService) {
        this.movimientoStockService = movimientoStockService;
    }

    /**
     * Registra un nuevo movimiento de stock.
     *
     * La anotación @Valid permite ejecutar las validaciones
     * definidas en la entidad MovimientoStock antes de procesar la solicitud.
     *
     * Endpoint:
     * POST /api/movimientos-stock
     *
     * @param movimiento movimiento recibido en el cuerpo de la solicitud.
     * @return movimiento creado con estado HTTP 201.
     */
    @PostMapping
    public ResponseEntity<MovimientoStock> registrar(@Valid @RequestBody MovimientoStock movimiento) {
        MovimientoStock movimientoCreado = movimientoStockService.registrar(movimiento);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(movimientoCreado);
    }

    /**
     * Obtiene el historial de todos los movimientos de stock.
     *
     * Endpoint:
     * GET /api/movimientos-stock
     *
     * @return lista de movimientos con estado HTTP 200.
     */
    @GetMapping
    public ResponseEntity<List<MovimientoStock>> obtenerHistorial() {
        return ResponseEntity.ok(movimientoStockService.obtenerHistorial());
    }
}