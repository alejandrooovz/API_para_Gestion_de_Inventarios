package sv.ues.inventarioapi.repository;

import sv.ues.inventarioapi.model.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {
    boolean existsByNitContribuyente(String nitContribuyente);
    boolean existsByNitContribuyenteAndIdNot(String nitContribuyente, Long id);
    boolean existsByNcrEmpresa(String ncrEmpresa);
    boolean existsByNcrEmpresaAndIdNot(String ncrEmpresa, Long id);
}
