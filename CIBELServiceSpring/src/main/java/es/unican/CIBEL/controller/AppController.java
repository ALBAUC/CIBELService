package es.unican.CIBEL.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.unican.CIBEL.domain.Aplicacion;
import es.unican.CIBEL.domain.Tipo;
import es.unican.CIBEL.domain.Vulnerabilidad;
import es.unican.CIBEL.service.AppService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Applications", description = "Operations related to Android applications")
@RestController
@RequestMapping("apps")
public class AppController {

	@Autowired
	private AppService aplicacionService;

	@Operation(
			summary = "Get all applications",
			description = "Returns a list of all applications."
			)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200", 
					description = "List of applications retrieved successfully",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(example = "[{ \"id\": 183, \"nombre\": \"Instagram\", \"icono\": \"https://i.imgur.com/MNaIwAy.jpg\", \"tipo\": { \"id\": 32, \"nombre\": \"Redes Sociales\", \"nombre_en\": \"Social Networks\" }, \"vulnerabilidades\": [] }]")
							)
					)
	})
	@GetMapping
	public List<Aplicacion> getAllAplicaciones() {
		return aplicacionService.aplicaciones();
	}

	@Operation(
			summary = "Get an application by ID",
			description = "Returns the details of a specific application based on its ID."
			)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200", 
					description = "Application retrieved successfully",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(example = "{ \"id\": 183, \"nombre\": \"Instagram\", \"icono\": \"https://i.imgur.com/MNaIwAy.jpg\", \"tipo\": { \"id\": 32, \"nombre\": \"Redes Sociales\", \"nombre_en\": \"Social Networks\" }, \"vulnerabilidades\": [] }")
							)
					),
			@ApiResponse(
					responseCode = "404", 
					description = "Application not found with the specified ID",
					content = @Content(
							schema = @Schema(example = "")
							)
					),
	})
	@GetMapping("/{id}")
	public ResponseEntity<Aplicacion> getAplicacionById(
			@Parameter(description = "ID of the application", example = "1") 
			@PathVariable Long id) {
		ResponseEntity<Aplicacion> result;
		Aplicacion aplicacion = aplicacionService.getAppById(id);

		if (aplicacion == null) {
			result = ResponseEntity.notFound().build();
		} else {
			result = ResponseEntity.ok(aplicacion);
		}

		return result;
	}

	@Operation(
			summary = "Get vulnerabilities of applications",
			description = "Returns a list of all known vulnerabilities for applications."
			)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200", 
					description = "List of vulnerabilities retrieved successfully",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(example = "[ { \"idCVE\": \"CVE-2019-16248\", \"descripcion\": \"La funcionalidad 'delete for' en Telegram versiones anteriores a 5.11 en Android no elimina completamente...\", \"confidentialityImpact\": \"HIGH\", \"integrityImpact\": \"NONE\", \"availabilityImpact\": \"NONE\", \"baseScore\": 5.5, \"baseSeverity\": \"MEDIUM\", \"versionEndExcluding\": \"5.11.0\", \"versionEndIncluding\": null } ]")
							)
					)
	})
	@GetMapping("/vulnerabilidades")
	public List<Vulnerabilidad> getVulnerabilidadesAplicaciones() {
		return aplicacionService.getVulnerabilidadesAplicaciones();
	}

	@Operation(
			summary = "Get application types",
			description = "Returns a list of available categories for applications."
			)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200", 
					description = "List of application categories retrieved successfully",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(example = "[ { \"id\": 20, \"nombre\": \"Utilidades\", \"nombre_en\": \"Utilities\" }, { \"id\": 21, \"nombre\": \"Compras\", \"nombre_en\": \"Shopping\" } ]")
							)
					)
	})
	@GetMapping("/tipos")
	public List<Tipo> getTiposAplicaciones() {
		return aplicacionService.getTiposAplicaciones();
	}
}
