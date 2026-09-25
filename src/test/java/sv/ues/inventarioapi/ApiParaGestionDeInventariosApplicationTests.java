package sv.ues.inventarioapi;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Prueba de integración básica que verifica que el contexto
 * completo de Spring Boot pueda iniciar correctamente.
 */
@SpringBootTest
@ActiveProfiles("test")
class ApiParaGestionDeInventariosApplicationTests {

	/**
	 * Verifica que Spring pueda cargar correctamente
	 * todos los componentes de la aplicación.
	 */
	@Test
	void contextLoads() {
	}
}