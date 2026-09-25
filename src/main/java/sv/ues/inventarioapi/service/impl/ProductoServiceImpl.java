package sv.ues.inventarioapi.service.impl;

import org.springframework.stereotype.Service;
import sv.ues.inventarioapi.model.Producto;
import sv.ues.inventarioapi.repository.ProductoRepository;
import sv.ues.inventarioapi.service.ProductoService;
import sv.ues.inventarioapi.exception.ResourceNotFoundException;
import sv.ues.inventarioapi.exception.DuplicateResourceException;

import java.util.List;

@Service
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoServiceImpl(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    public List<Producto> obtenerTodos() {
        return productoRepository.findAll();
    }

    @Override
    public Producto obtenerPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Producto no encontrado con id: " + id
                ));
    }

    @Override
    public Producto crear(Producto producto) {

        if (productoRepository.existsByCodigo(producto.getCodigo())) {
            throw new DuplicateResourceException(
                    "Ya existe un producto con el código: " + producto.getCodigo()
            );
        }

        return productoRepository.save(producto);
    }

    @Override
    public Producto actualizar(Long id, Producto producto) {

        Producto productoExistente = obtenerPorId(id);

        if (productoRepository.existsByCodigoAndIdNot(producto.getCodigo(), id)) {
            throw new DuplicateResourceException(
                    "Ya existe otro producto con el código: " + producto.getCodigo()
            );
        }

        productoExistente.setCodigo(producto.getCodigo());
        productoExistente.setNombre(producto.getNombre());
        productoExistente.setDescripcion(producto.getDescripcion());
        productoExistente.setPrecio(producto.getPrecio());
        productoExistente.setCantidadStock(producto.getCantidadStock());

        return productoRepository.save(productoExistente);
    }

    @Override
    public void eliminar(Long id) {
        Producto producto = obtenerPorId(id);
        productoRepository.delete(producto);
    }
}