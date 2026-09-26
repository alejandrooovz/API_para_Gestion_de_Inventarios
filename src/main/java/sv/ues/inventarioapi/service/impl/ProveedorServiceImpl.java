package sv.ues.inventarioapi.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sv.ues.inventarioapi.dto.ProveedorRequestDTO;
import sv.ues.inventarioapi.dto.ProveedorResponseDTO;
import sv.ues.inventarioapi.exception.DuplicateResourceException;
import sv.ues.inventarioapi.exception.ResourceNotFoundException;
import sv.ues.inventarioapi.model.Proveedor;
import sv.ues.inventarioapi.repository.ProveedorRepository;
import sv.ues.inventarioapi.service.ProveedorService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProveedorServiceImpl implements ProveedorService {

    private final ProveedorRepository proveedorRepository;

    @Override
    public List<ProveedorResponseDTO> obtenerTodos() {
        return proveedorRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public ProveedorResponseDTO obtenerPorId(Long id) {
        return toResponse(buscarOfallar(id));
    }

    @Override
    @Transactional
    public ProveedorResponseDTO crear(ProveedorRequestDTO dto) {
        if (proveedorRepository.existsByNitContribuyente(dto.getNitContribuyente())) {
            throw new DuplicateResourceException("Ya existe un proveedor con ese NIT");
        }
        if (proveedorRepository.existsByNcrEmpresa(dto.getNcrEmpresa())) {
            throw new DuplicateResourceException("Ya existe un proveedor con ese NCR");
        }

        Proveedor proveedor = Proveedor.builder()
                .nombre(dto.getNombre())
                .contacto(dto.getContacto())
                .telefono(dto.getTelefono())
                .email(dto.getEmail())
                .direccion(dto.getDireccion())
                .ncrEmpresa(dto.getNcrEmpresa())
                .nitContribuyente(dto.getNitContribuyente())
                .activo(true)
                .build();

        return toResponse(proveedorRepository.save(proveedor));
    }

    @Override
    @Transactional
    public ProveedorResponseDTO actualizar(Long id, ProveedorRequestDTO dto) {
        Proveedor proveedor = buscarOfallar(id);

        if (proveedorRepository.existsByNitContribuyenteAndIdNot(dto.getNitContribuyente(), id)) {
            throw new DuplicateResourceException("Ya existe otro proveedor con ese NIT");
        }
        if (proveedorRepository.existsByNcrEmpresaAndIdNot(dto.getNcrEmpresa(), id)) {
            throw new DuplicateResourceException("Ya existe otro proveedor con ese NCR");
        }

        proveedor.setNombre(dto.getNombre());
        proveedor.setContacto(dto.getContacto());
        proveedor.setTelefono(dto.getTelefono());
        proveedor.setEmail(dto.getEmail());
        proveedor.setDireccion(dto.getDireccion());
        proveedor.setNcrEmpresa(dto.getNcrEmpresa());
        proveedor.setNitContribuyente(dto.getNitContribuyente());

        return toResponse(proveedorRepository.save(proveedor));
    }

    @Override
    @Transactional
    public void inactivar(Long id) {
        Proveedor proveedor = buscarOfallar(id);
        proveedor.setActivo(false);
        proveedorRepository.save(proveedor);
    }

    private Proveedor buscarOfallar(Long id) {
        return proveedorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado con id " + id));
    }

    private ProveedorResponseDTO toResponse(Proveedor p) {
        return ProveedorResponseDTO.builder()
                .id(p.getId())
                .nombre(p.getNombre())
                .contacto(p.getContacto())
                .telefono(p.getTelefono())
                .email(p.getEmail())
                .direccion(p.getDireccion())
                .ncrEmpresa(p.getNcrEmpresa())
                .nitContribuyente(p.getNitContribuyente())
                .activo(p.getActivo())
                .build();
    }
}
