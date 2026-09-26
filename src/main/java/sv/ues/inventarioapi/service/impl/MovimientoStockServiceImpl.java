package sv.ues.inventarioapi.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sv.ues.inventarioapi.model.MovimientoStock;
import sv.ues.inventarioapi.model.Producto;
import sv.ues.inventarioapi.model.TipoMovimiento;
import sv.ues.inventarioapi.repository.MovimientoStockRepository;
import sv.ues.inventarioapi.repository.ProductoRepository;
import sv.ues.inventarioapi.service.MovimientoStockService;
import sv.ues.inventarioapi.exception.ResourceNotFoundException;
import sv.ues.inventarioapi.exception.StockInsuficienteException;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementación del servicio encargado de gestionar
 * la lógica de negocio relacionada con los movimientos de stock.
 *
 * Esta clase utiliza MovimientoStockRepository y ProductoRepository
 * para acceder a la base de datos y aplicar las validaciones necesarias
 * antes de realizar operaciones de registro de movimientos.
 */
@Service
public class MovimientoStockServiceImpl implements MovimientoStockService {

    private final MovimientoStockRepository movimientoStockRepository;
    private final ProductoRepository productoRepository;

    /**
     * Constructor utilizado para inyectar los repositorios
     * mediante inyección de dependencias.
     *
     * @param movimientoStockRepository repositorio de movimientos de stock.
     * @param productoRepository repositorio de productos.
     */
    public MovimientoStockServiceImpl(MovimientoStockRepository movimientoStockRepository,
                                       ProductoRepository productoRepository) {
        this.movimientoStockRepository = movimientoStockRepository;
        this.productoRepository = productoRepository;
    }

    /**
     * Registra un nuevo movimiento de stock y actualiza el stock del producto.
     *
     * La operación se ejecuta dentro de una transacción para garantizar
     * la consistencia entre el registro del movimiento y la actualización
     * del stock del producto.
     *
     * @param movimiento movimiento que se desea registrar.
     * @return movimiento almacenado.
     */
    @Override
    @Transactional
    public MovimientoStock registrar(MovimientoStock movimiento) {

        // Validar que el producto exista
        Producto producto = productoRepository.findById(movimiento.getProducto().getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Producto no encontrado con id: " + movimiento.getProducto().getId()
                ));

        // Establecer la fecha del movimiento si no viene
        if (movimiento.getFechaMovimiento() == null) {
            movimiento.setFechaMovimiento(LocalDateTime.now());
        }

        // Asignar el producto completo al movimiento
        movimiento.setProducto(producto);

        // Aplicar la lógica según el tipo de movimiento
        if (movimiento.getTipoMovimiento() == TipoMovimiento.ENTRADA) {
            // ENTRADA: aumentar el stock
            producto.setCantidadStock(producto.getCantidadStock() + movimiento.getCantidad());
        } else if (movimiento.getTipoMovimiento() == TipoMovimiento.SALIDA) {
            // SALIDA: verificar stock suficiente y disminuir
            if (producto.getCantidadStock() < movimiento.getCantidad()) {
                throw new StockInsuficienteException(
                        "Stock insuficiente. Stock actual: " + producto.getCantidadStock() +
                        ", cantidad solicitada: " + movimiento.getCantidad()
                );
            }
            producto.setCantidadStock(producto.getCantidadStock() - movimiento.getCantidad());
        }

        // Guardar el producto con el stock actualizado
        productoRepository.save(producto);

        // Guardar y retornar el movimiento
        return movimientoStockRepository.save(movimiento);
    }

    /**
     * Obtiene el historial de todos los movimientos de stock.
     *
     * @return lista de movimientos registrados.
     */
    @Override
    public List<MovimientoStock> obtenerHistorial() {
        return movimientoStockRepository.findAll();
    }
}