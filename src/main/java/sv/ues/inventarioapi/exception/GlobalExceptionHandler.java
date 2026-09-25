package sv.ues.inventarioapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.HashMap;
import java.util.Map;
import java.time.LocalDateTime;


/**
 * Manejador global de excepciones de la aplicación.
 *
 * Esta clase centraliza el tratamiento de errores para evitar
 * manejar excepciones individualmente en cada controlador.
 *
 * Se encarga de transformar las excepciones en respuestas HTTP
 * claras y estructuradas para el cliente.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja los errores producidos cuando no se encuentra
     * un recurso solicitado.
     *
     * @param ex excepción generada al no encontrar el recurso.
     * @return respuesta HTTP 404 con información del error.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> manejarResourceNotFound(
            ResourceNotFoundException ex) {

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ex.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }

    /**
     * Maneja los errores de validación generados por @Valid.
     *
     * Extrae los errores de cada campo y los devuelve
     * en una estructura fácil de interpretar.
     *
     * @param ex excepción generada cuando una validación falla.
     * @return respuesta HTTP 400 con los errores de validación.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> manejarValidaciones(
            MethodArgumentNotValidException ex) {

        Map<String, String> errores = new HashMap<>();

        // Se recorren los errores de cada campo validado.
        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errores.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        /**
         * Maneja los intentos de registrar o actualizar
         * un recurso con información duplicada.
         *
         * En el módulo de productos se utiliza principalmente
         * cuando el código del producto ya está registrado.
         *
         * @param ex excepción generada por un recurso duplicado.
         * @return respuesta HTTP 409 con información del conflicto.
         */
        Map<String, Object> respuesta = new HashMap<>();

        respuesta.put("timestamp", LocalDateTime.now());
        respuesta.put("status", HttpStatus.BAD_REQUEST.value());
        respuesta.put("error", HttpStatus.BAD_REQUEST.getReasonPhrase());
        respuesta.put("errores", errores);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(respuesta);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> manejarDuplicado(
            DuplicateResourceException ex) {

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                ex.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(error);
    }
}

