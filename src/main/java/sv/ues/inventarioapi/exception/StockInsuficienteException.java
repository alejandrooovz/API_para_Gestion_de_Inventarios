package sv.ues.inventarioapi.exception;

/**
 * Excepción personalizada utilizada cuando se intenta
 * realizar una salida de stock superior al disponible.
 */
public class StockInsuficienteException extends RuntimeException {

    /**
     * Crea una nueva excepción con un mensaje descriptivo.
     *
     * @param mensaje descripción del error ocurrido.
     */
    public StockInsuficienteException(String mensaje) {
        super(mensaje);
    }
}