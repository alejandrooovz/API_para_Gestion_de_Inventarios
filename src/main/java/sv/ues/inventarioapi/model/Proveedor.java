package sv.ues.inventarioapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "proveedores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 150, message = "El nombre no puede superar los 150 caracteres")
    @Column(nullable = false, length = 150)
    private String nombre;

    @Size(max = 100, message = "El contacto no puede superar los 100 caracteres")
    @Column(length = 100)
    private String contacto;

    @Size(max = 20, message = "El teléfono no puede superar los 20 caracteres")
    @Column(length = 20)
    private String telefono;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe tener un formato válido")
    @Column(nullable = false, length = 150)
    private String email;

    @NotBlank(message = "La dirección es obligatoria")
    @Size(max = 255, message = "La dirección no puede superar los 255 caracteres")
    @Column(nullable = false, length = 255)
    private String direccion;

    @NotBlank(message = "El NCR de la empresa es obligatorio")
    @Column(name = "ncr_empresa", nullable = false, unique = true, length = 20)
    private String ncrEmpresa;

    @NotBlank(message = "El NIT del contribuyente es obligatorio")
    @Column(name = "nit_contribuyente", nullable = false, unique = true, length = 30)
    private String nitContribuyente;

    @Column(nullable = false)
    @Builder.Default
    private Boolean activo = true;
}
