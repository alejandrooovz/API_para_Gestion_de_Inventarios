package sv.ues.inventarioapi.exception;

/**
 * Excepción personalizada utilizada cuando un recurso
 * solicitado no existe dentro del sistema.
 *
 * En el módulo de productos se utiliza, por ejemplo,
 * cuando se intenta buscar, actualizar o eliminar
 * un producto cuyo identificador no está registrado.
 */
public class ResourceNotFoundException extends RuntimeException {



    /**
     * Crea una nueva excepción con un mensaje descriptivo.
     *
     * @param mensaje descripción del error ocurrido.
     */
    public ResourceNotFoundException(String mensaje) {
        super(mensaje);
    }
}