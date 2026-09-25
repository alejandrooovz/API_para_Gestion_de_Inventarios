package sv.ues.inventarioapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sv.ues.inventarioapi.model.Producto;

/**
 * Repositorio encargado del acceso a datos de la entidad Producto.
 *
 * Extiende JpaRepository para utilizar operaciones CRUD
 * sin necesidad de implementar manualmente consultas básicas.
 */
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    /**
     * Verifica si existe un producto registrado con el código indicado.
     *
     * @param codigo código del producto a validar.
     * @return true si ya existe un producto con ese código,
     *         false en caso contrario.
     */
    boolean existsByCodigo(String codigo);

    /**
     * Verifica si existe otro producto con el mismo código,
     * excluyendo el producto cuyo identificador se recibe.
     *
     * Este método se utiliza principalmente durante la actualización
     * de productos para evitar códigos duplicados.
     *
     * @param codigo código del producto a validar.
     * @param id identificador del producto que debe excluirse.
     * @return true si existe otro producto con ese código,
     *         false en caso contrario.
     */
    boolean existsByCodigoAndIdNot(String codigo, Long id);
}