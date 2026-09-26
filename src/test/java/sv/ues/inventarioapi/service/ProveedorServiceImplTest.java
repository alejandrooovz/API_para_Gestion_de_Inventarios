package sv.ues.inventarioapi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sv.ues.inventarioapi.dto.ProveedorRequestDTO;
import sv.ues.inventarioapi.dto.ProveedorResponseDTO;
import sv.ues.inventarioapi.exception.DuplicateResourceException;
import sv.ues.inventarioapi.exception.ResourceNotFoundException;
import sv.ues.inventarioapi.model.Proveedor;
import sv.ues.inventarioapi.repository.ProveedorRepository;
import sv.ues.inventarioapi.service.impl.ProveedorServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProveedorServiceImplTest {

    @Mock
    private ProveedorRepository proveedorRepository;

    @InjectMocks
    private ProveedorServiceImpl proveedorService;

    private Proveedor proveedor;
    private ProveedorRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        proveedor = Proveedor.builder()
                .id(1L)
                .nombre("Distribuidora de papel de oficina")
                .contacto("Raul Beltran")
                .telefono("2522 0001")
                .email("ventas@distribuidorapapel.com")
                .direccion("Avenida Revolucion, San Salvador Centro")
                .ncrEmpresa("1234-0")
                .nitContribuyente("0614-000001-120-0")
                .activo(true)
                .build();

        requestDTO = ProveedorRequestDTO.builder()
                .nombre("Distribuidora de papel de oficina")
                .contacto("Raul Beltran")
                .telefono("2522 0001")
                .email("ventas@distribuidorapapel.com")
                .direccion("Avenida Revolucion, San Salvador Centro")
                .ncrEmpresa("1234-0")
                .nitContribuyente("0614-000001-120-0")
                .build();
    }

    @Test
    void obtenerTodos_debeRetornarListaDeProveedores() {
        when(proveedorRepository.findAll()).thenReturn(List.of(proveedor));

        List<ProveedorResponseDTO> resultado = proveedorService.obtenerTodos();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNombre()).isEqualTo(proveedor.getNombre());
        verify(proveedorRepository, times(1)).findAll();
    }

    @Test
    void obtenerPorId_debeRetornarProveedor_cuandoExiste() {
        when(proveedorRepository.findById(1L)).thenReturn(Optional.of(proveedor));

        ProveedorResponseDTO resultado = proveedorService.obtenerPorId(1L);

        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getNitContribuyente()).isEqualTo("0614-000001-120-0");
    }

    @Test
    void obtenerPorId_debeLanzarExcepcion_cuandoNoExiste() {
        when(proveedorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> proveedorService.obtenerPorId(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void crear_debeGuardarProveedor_cuandoNoHayDuplicados() {
        when(proveedorRepository.existsByNitContribuyente(requestDTO.getNitContribuyente())).thenReturn(false);
        when(proveedorRepository.existsByNcrEmpresa(requestDTO.getNcrEmpresa())).thenReturn(false);
        when(proveedorRepository.save(any(Proveedor.class))).thenReturn(proveedor);

        ProveedorResponseDTO resultado = proveedorService.crear(requestDTO);

        assertThat(resultado.getNombre()).isEqualTo(requestDTO.getNombre());
        assertThat(resultado.getActivo()).isTrue();
        verify(proveedorRepository, times(1)).save(any(Proveedor.class));
    }

    @Test
    void crear_debeRechazar_cuandoNitYaExiste() {
        when(proveedorRepository.existsByNitContribuyente(requestDTO.getNitContribuyente())).thenReturn(true);

        assertThatThrownBy(() -> proveedorService.crear(requestDTO))
                .isInstanceOf(DuplicateResourceException.class);

        verify(proveedorRepository, never()).save(any(Proveedor.class));
    }

    @Test
    void crear_debeRechazar_cuandoNcrYaExiste() {
        when(proveedorRepository.existsByNitContribuyente(requestDTO.getNitContribuyente())).thenReturn(false);
        when(proveedorRepository.existsByNcrEmpresa(requestDTO.getNcrEmpresa())).thenReturn(true);

        assertThatThrownBy(() -> proveedorService.crear(requestDTO))
                .isInstanceOf(DuplicateResourceException.class);

        verify(proveedorRepository, never()).save(any(Proveedor.class));
    }

    @Test
    void actualizar_debeModificarProveedor_cuandoDatosSonValidos() {
        when(proveedorRepository.findById(1L)).thenReturn(Optional.of(proveedor));
        when(proveedorRepository.existsByNitContribuyenteAndIdNot(requestDTO.getNitContribuyente(), 1L)).thenReturn(false);
        when(proveedorRepository.existsByNcrEmpresaAndIdNot(requestDTO.getNcrEmpresa(), 1L)).thenReturn(false);
        when(proveedorRepository.save(any(Proveedor.class))).thenReturn(proveedor);

        ProveedorResponseDTO resultado = proveedorService.actualizar(1L, requestDTO);

        assertThat(resultado.getNombre()).isEqualTo(requestDTO.getNombre());
        verify(proveedorRepository, times(1)).save(any(Proveedor.class));
    }

    @Test
    void actualizar_debeRechazar_cuandoNitPerteneceAOtroProveedor() {
        when(proveedorRepository.findById(1L)).thenReturn(Optional.of(proveedor));
        when(proveedorRepository.existsByNitContribuyenteAndIdNot(requestDTO.getNitContribuyente(), 1L)).thenReturn(true);

        assertThatThrownBy(() -> proveedorService.actualizar(1L, requestDTO))
                .isInstanceOf(DuplicateResourceException.class);
    }

    @Test
    void actualizar_debeLanzarExcepcion_cuandoProveedorNoExiste() {
        when(proveedorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> proveedorService.actualizar(99L, requestDTO))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void inactivar_debeCambiarEstadoActivoAFalse() {
        when(proveedorRepository.findById(1L)).thenReturn(Optional.of(proveedor));
        when(proveedorRepository.save(any(Proveedor.class))).thenReturn(proveedor);

        proveedorService.inactivar(1L);

        assertThat(proveedor.getActivo()).isFalse();
        verify(proveedorRepository, times(1)).save(proveedor);
    }

    @Test
    void inactivar_debeLanzarExcepcion_cuandoProveedorNoExiste() {
        when(proveedorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> proveedorService.inactivar(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
