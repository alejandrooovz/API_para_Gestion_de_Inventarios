package sv.ues.inventarioapi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sv.ues.inventarioapi.exception.DuplicateResourceException;
import sv.ues.inventarioapi.exception.ResourceNotFoundException;
import sv.ues.inventarioapi.model.Producto;
import sv.ues.inventarioapi.repository.ProductoRepository;
import sv.ues.inventarioapi.service.impl.ProductoServiceImpl;
import java.util.HashSet;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para ProductoServiceImpl.
 *
 * Se utiliza Mockito para simular el repositorio y probar
 * únicamente la lógica de negocio del servicio.
 */
@ExtendWith(MockitoExtension.class)
class ProductoServiceImplTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoServiceImpl productoService;

    private Producto producto;

    @BeforeEach
    void setUp() {
        producto = new Producto(
                1L,
                "PROD-001",
                "Teclado",
                "Teclado mecánico",
                new BigDecimal("25.50"),
                10,
                new HashSet<>()
        );
    }

    /**
     * Verifica que obtenerTodos retorne los productos
     * proporcionados por el repositorio.
     */
    @Test
    void obtenerTodosDebeRetornarListaDeProductos() {

        when(productoRepository.findAll())
                .thenReturn(List.of(producto));

        List<Producto> resultado = productoService.obtenerTodos();

        assertEquals(1, resultado.size());
        assertEquals("PROD-001", resultado.get(0).getCodigo());

        verify(productoRepository).findAll();
    }

    /**
     * Verifica que un producto existente pueda obtenerse por ID.
     */
    @Test
    void obtenerPorIdDebeRetornarProductoCuandoExiste() {

        when(productoRepository.findById(1L))
                .thenReturn(Optional.of(producto));

        Producto resultado = productoService.obtenerPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Teclado", resultado.getNombre());

        verify(productoRepository).findById(1L);
    }

    /**
     * Verifica que se lance ResourceNotFoundException
     * cuando el producto solicitado no existe.
     */
    @Test
    void obtenerPorIdDebeLanzarExcepcionCuandoNoExiste() {

        when(productoRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> productoService.obtenerPorId(99L)
        );

        verify(productoRepository).findById(99L);
    }

    /**
     * Verifica que un producto nuevo pueda registrarse.
     */
    @Test
    void crearDebeGuardarProductoCuandoCodigoNoExiste() {

        when(productoRepository.existsByCodigo("PROD-001"))
                .thenReturn(false);

        when(productoRepository.save(producto))
                .thenReturn(producto);

        Producto resultado = productoService.crear(producto);

        assertNotNull(resultado);
        assertEquals("PROD-001", resultado.getCodigo());

        verify(productoRepository).existsByCodigo("PROD-001");
        verify(productoRepository).save(producto);
    }

    /**
     * Verifica que no pueda registrarse un código duplicado.
     */
    @Test
    void crearDebeLanzarExcepcionCuandoCodigoExiste() {

        when(productoRepository.existsByCodigo("PROD-001"))
                .thenReturn(true);

        assertThrows(
                DuplicateResourceException.class,
                () -> productoService.crear(producto)
        );

        verify(productoRepository).existsByCodigo("PROD-001");
        verify(productoRepository, never()).save(any());
    }

    /**
     * Verifica que un producto existente pueda actualizarse.
     */
    @Test
    void actualizarDebeModificarProductoCuandoDatosSonValidos() {

        Producto nuevosDatos = new Producto(
                null,
                "PROD-002",
                "Teclado actualizado",
                "Nueva descripción",
                new BigDecimal("30.00"),
                20,
                new HashSet<>()
        );

        when(productoRepository.findById(1L))
                .thenReturn(Optional.of(producto));

        when(productoRepository.existsByCodigoAndIdNot("PROD-002", 1L))
                .thenReturn(false);

        when(productoRepository.save(producto))
                .thenReturn(producto);

        Producto resultado = productoService.actualizar(1L, nuevosDatos);

        assertEquals("PROD-002", resultado.getCodigo());
        assertEquals("Teclado actualizado", resultado.getNombre());
        assertEquals(new BigDecimal("30.00"), resultado.getPrecio());
        assertEquals(20, resultado.getCantidadStock());

        verify(productoRepository).save(producto);
    }

    /**
     * Verifica que no pueda actualizarse un producto utilizando
     * el código perteneciente a otro producto.
     */
    @Test
    void actualizarDebeLanzarExcepcionCuandoCodigoPerteneceAOtroProducto() {

        Producto nuevosDatos = new Producto(
                null,
                "PROD-002",
                "Teclado",
                "Descripción",
                new BigDecimal("30.00"),
                20,
                new HashSet<>()
        );

        when(productoRepository.findById(1L))
                .thenReturn(Optional.of(producto));

        when(productoRepository.existsByCodigoAndIdNot("PROD-002", 1L))
                .thenReturn(true);

        assertThrows(
                DuplicateResourceException.class,
                () -> productoService.actualizar(1L, nuevosDatos)
        );

        verify(productoRepository, never()).save(any());
    }

    /**
     * Verifica que un producto existente pueda eliminarse.
     */
    @Test
    void eliminarDebeEliminarProductoCuandoExiste() {

        when(productoRepository.findById(1L))
                .thenReturn(Optional.of(producto));

        productoService.eliminar(1L);

        verify(productoRepository).delete(producto);
    }
}