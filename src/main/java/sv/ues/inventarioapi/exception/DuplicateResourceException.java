package sv.ues.inventarioapi.exception;

/**
 * Excepción personalizada utilizada cuando se intenta
 * registrar o actualizar un recurso con información
 * que debe ser única y ya existe en el sistema.
 *
 * En el módulo de productos se utiliza principalmente
 * para evitar códigos de producto duplicados.
 */
public class DuplicateResourceException extends RuntimeException {

    /**
     * Crea una nueva excepción con un mensaje descriptivo.
     *
     * @param mensaje descripción del conflicto detectado.
     */
    public DuplicateResourceException(String mensaje) {
        super(mensaje);
    }
}