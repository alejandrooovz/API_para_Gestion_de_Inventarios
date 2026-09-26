package sv.ues.inventarioapi.service;

import sv.ues.inventarioapi.model.MovimientoStock;

import java.util.List;

/**
 * Servicio que define las operaciones disponibles
 * para la gestión de movimientos de stock dentro del sistema.
 *
 * Esta interfaz separa la lógica de negocio de la capa
 * de controladores y de la persistencia de datos.
 */
public interface MovimientoStockService {

    /**
     * Registra un nuevo movimiento de stock.
     *
     * @param movimiento movimiento que se desea registrar.
     * @return movimiento almacenado.
     */
    MovimientoStock registrar(MovimientoStock movimiento);

    /**
     * Obtiene el historial de todos los movimientos de stock.
     *
     * @return lista de movimientos registrados.
     */
    List<MovimientoStock> obtenerHistorial();
}