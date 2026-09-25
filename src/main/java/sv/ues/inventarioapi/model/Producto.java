package sv.ues.inventarioapi.model;

import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;


/**
 * Entidad que representa un producto dentro del sistema de inventario.
 *
 * Cada producto posee un código único, nombre, descripción,
 * precio y cantidad disponible en stock.
 */
@Entity
@Table(name = "productos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Producto {

    /**
     * Identificador único del producto.
     * Se genera automáticamente en la base de datos.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Código único utilizado para identificar el producto.
     */
    @NotBlank(message = "El código es obligatorio")
    @Size(max = 50, message = "El código no puede superar los 50 caracteres")
    @Column(nullable = false, unique = true, length = 50)
    private String codigo;

    /**
     * Nombre del producto.
     */
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nombre;

    /**
     * Descripción opcional del producto.
     */
    @Size(max = 255, message = "La descripción no puede superar los 255 caracteres")
    @Column(length = 255)
    private String descripcion;

    /**
     * Precio unitario del producto.
     * Debe ser mayor que cero.
     */
    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor que 0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    /**
     * Cantidad disponible actualmente en inventario.
     * No puede contener valores negativos.
     */
    @NotNull(message = "La cantidad de stock es obligatoria")
    @PositiveOrZero(message = "La cantidad de stock no puede ser negativa")
    @Column(nullable = false)
    private Integer cantidadStock;
}