package sv.ues.inventarioapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sv.ues.inventarioapi.model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    boolean existsByCodigo(String codigo);

    boolean existsByCodigoAndIdNot(String codigo, Long id);
}