package sv.ues.inventarioapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal de la aplicación API para Gestión de Inventarios.
 *
 * Esta clase contiene el punto de entrada de Spring Boot y se encarga
 * de iniciar la aplicación, cargar la configuración y detectar
 * automáticamente los componentes definidos dentro del proyecto.
 */
@SpringBootApplication
public class ApiParaGestionDeInventariosApplication {

	/**
	 * Método principal utilizado para iniciar la aplicación.
	 *
	 * @param args argumentos recibidos al ejecutar el programa.
	 */
	public static void main(String[] args) {
		SpringApplication.run(ApiParaGestionDeInventariosApplication.class, args);
	}

}
