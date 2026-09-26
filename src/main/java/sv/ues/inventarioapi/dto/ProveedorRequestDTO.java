package sv.ues.inventarioapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProveedorRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 150)
    private String nombre;

    @Size(max = 100)
    private String contacto;

    @Size(max = 20)
    private String telefono;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe tener un formato válido")
    private String email;

    @NotBlank(message = "La dirección es obligatoria")
    @Size(max = 255)
    private String direccion;

    @NotBlank(message = "El NCR de la empresa es obligatorio")
    private String ncrEmpresa;

    @NotBlank(message = "El NIT del contribuyente es obligatorio")
    private String nitContribuyente;
}
