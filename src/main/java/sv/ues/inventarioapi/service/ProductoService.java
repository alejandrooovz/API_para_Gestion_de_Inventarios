package sv.ues.inventarioapi.service;

import sv.ues.inventarioapi.model.Producto;

import java.util.List;

/**
 * Servicio que define las operaciones disponibles
 * para la gestión de productos dentro del sistema.
 *
 * Esta interfaz separa la lógica de negocio de la capa
 * de controladores y de la persistencia de datos.
 */
public interface ProductoService {

    /**
     * Obtiene todos los productos registrados.
     *
     * @return lista de productos existentes.
     */
    List<Producto> obtenerTodos();

    /**
     * Busca un producto mediante su identificador.
     *
     * @param id identificador único del producto.
     * @return producto correspondiente al identificador indicado.
     */
    Producto obtenerPorId(Long id);

    /**
     * Registra un nuevo producto en el sistema.
     *
     * @param producto producto que se desea registrar.
     * @return producto almacenado.
     */
    Producto crear(Producto producto);

    /**
     * Actualiza la información de un producto existente.
     *
     * @param id identificador del producto a actualizar.
     * @param producto nuevos datos del producto.
     * @return producto actualizado.
     */
    Producto actualizar(Long id, Producto producto);

    /**
     * Elimina un producto mediante su identificador.
     *
     * @param id identificador del producto a eliminar.
     */
    void eliminar(Long id);
}