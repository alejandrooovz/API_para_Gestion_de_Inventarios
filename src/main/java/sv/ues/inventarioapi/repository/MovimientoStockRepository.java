package sv.ues.inventarioapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sv.ues.inventarioapi.model.MovimientoStock;

/**
 * Repositorio encargado del acceso a datos de la entidad MovimientoStock.
 *
 * Extiende JpaRepository para utilizar operaciones CRUD
 * sin necesidad de implementar manualmente consultas básicas.
 */
public interface MovimientoStockRepository extends JpaRepository<MovimientoStock, Long> {
}