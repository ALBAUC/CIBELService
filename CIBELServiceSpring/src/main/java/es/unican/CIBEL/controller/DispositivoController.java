package es.unican.CIBEL.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.unican.CIBEL.domain.Debilidad;
import es.unican.CIBEL.domain.Dispositivo;
import es.unican.CIBEL.domain.Tipo;
import es.unican.CIBEL.domain.Vulnerabilidad;
import es.unican.CIBEL.service.DispositivoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Devices", description = "Operations related to devices")
@RestController
@RequestMapping("dispositivos")
public class DispositivoController {

	@Autowired
	private DispositivoService dispositivoService;

	@Operation(
			summary = "Retrieve all devices",
			description = "Returns a list of all devices. If a device type is provided, the devices will be filtered by that type."
			)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200", 
					description = "List of devices retrieved successfully",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(example = "[{ \"id\": 1, \"nombre\": \"Lenovo ThinkPad T440s\", \"icono\": \"https://m.media-amazon.com/images/I/51ujDlKCobL.jpg\", \"tipo\": { \"id\": 1, \"nombre\": \"Ordenadores\", \"nombre_en\": \"Computers\" }, \"vulnerabilidades\": [], \"durabilidad\": 0, \"reparabilidad\": 0, \"reciclabilidad\": 0, \"efClimatica\": 0, \"efRecursos\": 0, \"ecoPuntuacion\": 0 }]")
							)
					)
	})
	@GetMapping
	public List<Dispositivo> getAllDispositivos(
			@Parameter(description = "Device type to filter by", example = "Smartphones") 
			@RequestParam(required = false) String tipo) {
		// If 'type' is provided, filter the devices
		if (tipo != null) {
			return dispositivoService.getDispositivosByTipo(tipo);
		}
		// If not provided, return all devices
		return dispositivoService.dispositivos();
	}

	@Operation(
			summary = "Retrieve a device by ID",
			description = "Returns the details of a device given its ID"
			)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200", 
					description = "Device retrieved successfully",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(example = "{ \"id\": 1, \"nombre\": \"Lenovo ThinkPad T440s\", \"icono\": \"https://m.media-amazon.com/images/I/51ujDlKCobL.jpg\", \"tipo\": { \"id\": 1, \"nombre\": \"Ordenadores\", \"nombre_en\": \"Computers\" }, \"vulnerabilidades\": [], \"durabilidad\": 0, \"reparabilidad\": 0, \"reciclabilidad\": 0, \"efClimatica\": 0, \"efRecursos\": 0, \"ecoPuntuacion\": 0 }")
							)
					),
			@ApiResponse(
					responseCode = "404", 
					description = "Device with the specified ID not found",
					content = @Content(
							schema = @Schema(example = "")
							)
					)
	})
	@GetMapping("/{id}")
	public ResponseEntity<Dispositivo> getDispositivoById(
			@Parameter(description = "Device ID", example = "1") 
			@PathVariable Long id) {

		ResponseEntity<Dispositivo> result;
		Dispositivo dispositivo = dispositivoService.getDispositivoById(id);

		if (dispositivo == null) {
			result = ResponseEntity.notFound().build();
		} else {
			result = ResponseEntity.ok(dispositivo);
		}

		return result;
	}

	@Operation(
			summary = "Retrieve device vulnerabilities",
			description = "Returns a list of all known vulnerabilities for the devices"
			)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200", 
					description = "List of vulnerabilities retrieved successfully",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(example = "[ { \"idCVE\": \"CVE-2015-7267\", \"descripcion\": \"Las unidades de estado sólido Samsung 850 Pro y PM851 y las unidades...\", \"descripcion_en\": \"Samsung 850 Pro and PM851 solid-state drives and Seagate ST500LT015 and ST500LT025 hard disk...\", \"confidentialityImpact\": \"PARTIAL\", \"integrityImpact\": \"NONE\", \"availabilityImpact\": \"NONE\", \"baseScore\": 1.9, \"baseSeverity\": \"LOW\" } ]")
							)
					)
	})
	@GetMapping("/vulnerabilidades")
	public List<Vulnerabilidad> getVulnerabilidadesDispositivos() {
		return dispositivoService.getVulnerabilidadesDispositivos();
	}

	@Operation(
			summary = "Retrieve device types",
			description = "Returns a list of available device categories"
			)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200", 
					description = "List of device categories retrieved successfully",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(example = "[ { \"id\": 1, \"nombre\": \"Ordenadores\", \"nombre_en\": \"Computers\" }, { \"id\": 2, \"nombre\": \"Smartphones\", \"nombre_en\": \"Smartphones\" } ]")
							)
					)
	})
	@GetMapping("/tipos")
	public List<Tipo> getTiposDispositivos() {
		return dispositivoService.getTiposDispositivos();
	}
	
	@GetMapping("/debilidades")
	public List<Debilidad> getDebilidadesDispositivos() {
		return dispositivoService.getDebilidadesDispositivos();
	}
}
