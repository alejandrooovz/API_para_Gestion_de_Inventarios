package sv.ues.inventarioapi.service;

import sv.ues.inventarioapi.dto.ProveedorRequestDTO;
import sv.ues.inventarioapi.dto.ProveedorResponseDTO;

import java.util.List;

public interface ProveedorService {
    List<ProveedorResponseDTO> obtenerTodos();
    ProveedorResponseDTO obtenerPorId(Long id);
    ProveedorResponseDTO crear(ProveedorRequestDTO dto);
    ProveedorResponseDTO actualizar(Long id, ProveedorRequestDTO dto);
    void inactivar(Long id);
}
