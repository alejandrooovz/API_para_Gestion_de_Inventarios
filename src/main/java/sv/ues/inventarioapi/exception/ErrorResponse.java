package sv.ues.inventarioapi.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Clase utilizada para estructurar las respuestas de error
 * enviadas por la API.
 *
 * Permite mantener un formato uniforme al responder
 * errores como recursos no encontrados o conflictos.
 */
@Getter
@AllArgsConstructor
public class ErrorResponse {

    /**
     * Fecha y hora en la que ocurrió el error.
     */
    private LocalDateTime timestamp;

    /**
     * Código de estado HTTP asociado al error.
     *
     * Ejemplos:
     * 404 - Not Found
     * 409 - Conflict
     */
    private int status;

    /**
     * Nombre o descripción corta del estado HTTP.
     */
    private String error;

    /**
     * Mensaje detallado que explica la causa del error.
     */
    private String mensaje;
}