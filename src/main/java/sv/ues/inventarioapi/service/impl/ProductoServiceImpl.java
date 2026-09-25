package sv.ues.inventarioapi.service.impl;

import org.springframework.stereotype.Service;
import sv.ues.inventarioapi.model.Producto;
import sv.ues.inventarioapi.repository.ProductoRepository;
import sv.ues.inventarioapi.service.ProductoService;
import sv.ues.inventarioapi.exception.ResourceNotFoundException;
import sv.ues.inventarioapi.exception.DuplicateResourceException;

import java.util.List;

/**
 * Implementación del servicio encargado de gestionar
 * la lógica de negocio relacionada con los productos.
 *
 * Esta clase utiliza ProductoRepository para acceder
 * a la base de datos y aplicar las validaciones necesarias
 * antes de realizar operaciones de creación, actualización
 * o eliminación.
 */
@Service
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;

    /**
     * Constructor utilizado para inyectar el repositorio
     * de productos mediante inyección de dependencias.
     *
     * @param productoRepository repositorio de productos.
     */
    public ProductoServiceImpl(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    /**
     * Obtiene todos los productos almacenados en la base de datos.
     *
     * @return lista de productos registrados.
     */
    @Override
    public List<Producto> obtenerTodos() {
        return productoRepository.findAll();
    }

    /**
     * Busca un producto mediante su identificador.
     *
     * Si el producto no existe, se lanza una excepción
     * ResourceNotFoundException.
     *
     * @param id identificador del producto.
     * @return producto encontrado.
     */
    @Override
    public Producto obtenerPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Producto no encontrado con id: " + id
                ));
    }

    /**
     * Registra un nuevo producto en el sistema.
     *
     * Antes de guardar el producto se valida que no exista
     * otro registro con el mismo código.
     *
     * @param producto producto que se desea registrar.
     * @return producto almacenado.
     */
    @Override
    public Producto crear(Producto producto) {

        // Se valida la unicidad del código antes de guardar el producto.
        if (productoRepository.existsByCodigo(producto.getCodigo())) {
            throw new DuplicateResourceException(
                    "Ya existe un producto con el código: " + producto.getCodigo()
            );
        }

        return productoRepository.save(producto);
    }

    /**
     * Actualiza la información de un producto existente.
     *
     * Primero se comprueba que el producto exista y luego
     * se valida que el nuevo código no pertenezca a otro producto.
     *
     * @param id identificador del producto que se desea actualizar.
     * @param producto nuevos datos del producto.
     * @return producto actualizado.
     */
    @Override
    public Producto actualizar(Long id, Producto producto) {

        // Reutiliza obtenerPorId para validar que el producto exista.
        Producto productoExistente = obtenerPorId(id);

        // Impide utilizar un código que ya pertenezca a otro producto.
        if (productoRepository.existsByCodigoAndIdNot(producto.getCodigo(), id)) {
            throw new DuplicateResourceException(
                    "Ya existe otro producto con el código: " + producto.getCodigo()
            );
        }

        // Se actualizan los datos del producto existente.
        productoExistente.setCodigo(producto.getCodigo());
        productoExistente.setNombre(producto.getNombre());
        productoExistente.setDescripcion(producto.getDescripcion());
        productoExistente.setPrecio(producto.getPrecio());
        productoExistente.setCantidadStock(producto.getCantidadStock());

        return productoRepository.save(productoExistente);
    }

    /**
     * Elimina un producto del sistema mediante su identificador.
     *
     * Antes de eliminarlo se verifica que el producto exista.
     *
     * @param id identificador del producto que se desea eliminar.
     */
    @Override
    public void eliminar(Long id) {
        Producto producto = obtenerPorId(id);
        productoRepository.delete(producto);
    }
}