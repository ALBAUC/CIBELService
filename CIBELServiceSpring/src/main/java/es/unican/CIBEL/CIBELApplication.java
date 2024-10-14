package es.unican.CIBEL;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
		info = @Info(
				title = "CIBEL Service",
				version = "1.0.0",
				description = "Service providing information about vulnerabilities in Android applications, as well as vulnerabilities and sustainability of IoT devices.",
				license = @License(name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0"),
				contact = @Contact(name = "Alina Solonaru Botnari", email = "solonarua@unican.com")
				),
		servers = {
				@Server(description = "Local Environment", url = "http://localhost:8080/"),
				@Server(description = "Development Environment", url = "https://thorough-healthy-escargot.ngrok-free.app/"),
		}
		)
@SpringBootApplication
public class CIBELApplication {

	public static void main(String[] args) {
		SpringApplication.run(CIBELApplication.class, args);
	}
}
