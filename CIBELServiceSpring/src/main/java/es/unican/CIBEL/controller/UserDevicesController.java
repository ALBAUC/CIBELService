package es.unican.CIBEL.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.unican.CIBEL.domain.Dispositivo;
import es.unican.CIBEL.domain.Usuario;
import es.unican.CIBEL.service.DispositivoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@Tag(name = "User devices", description = "Operations related to user devices")
@RestController
@RequestMapping("me/dispositivos")
public class UserDevicesController {

	@Autowired
	private DispositivoService dispositivoService;

	@Operation(
			summary = "Get authenticated user's devices",
			description = "Returns a list of devices owned by the authenticated user.",
			security = @SecurityRequirement(name = "Bearer Token")
			)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Successfully retrieves the devices belonging to the authenticated user.",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(example = "[{ \"id\": 1, \"nombre\": \"Lenovo ThinkPad T440s\", \"icono\": \"https://m.media-amazon.com/images/I/51ujDlKCobL.jpg\", \"tipo\": { \"id\": 1, \"nombre\": \"Ordenadores\", \"nombre_en\": \"Computers\" }, \"vulnerabilidades\": [], \"durabilidad\": 0, \"reparabilidad\": 0, \"reciclabilidad\": 0, \"efClimatica\": 0, \"efRecursos\": 0, \"ecoPuntuacion\": 0 }]")
							)
					)
	})
	@GetMapping
	public List<Dispositivo> getAuthUserDevices() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario currentUser = (Usuario) authentication.getPrincipal();

		return currentUser.getActivos().stream()
				.filter(activo -> activo instanceof Dispositivo)
				.map(activo -> (Dispositivo) activo)
				.collect(Collectors.toList());
	}

	@Operation(
			summary = "Get eco score of authenticated user",
			description = "Calculates and returns the eco score of the authenticated user based on their devices.",
			security = @SecurityRequirement(name = "Bearer Token")
			)
	@GetMapping("/ecoscore")
	public int getEcoScore() {
		List<Dispositivo> dispositivos = getAuthUserDevices();
		int result = 100;
		if (dispositivos.size() != 0) {
			double s = 0;
			for (Dispositivo d : dispositivos) {
				s += d.getEcoPuntuacion();
			}
			result = (int) Math.round(s / dispositivos.size());
		}
		return result;
	}

	@Operation(
			summary = "Get security score of authenticated user",
			description = "Calculates and returns the security score of the authenticated user based on their devices.",
			security = @SecurityRequirement(name = "Bearer Token")
			)
	@GetMapping("/securityscore")
	public int getSecurityScore() {
		List<Dispositivo> dispositivos = getAuthUserDevices();
		int result = 100;
		if (dispositivos.size() != 0) {
			double s = 0;
			for (Dispositivo d : dispositivos) {
				s += d.calcularPuntuacionSeguridad();
			}
			result = (int) Math.round(s / dispositivos.size());
		}
		return result;
	}

	@Operation(
			summary = "Add device to authenticated user",
			description = "Adds a device to the authenticated user's list of devices.",
			security = @SecurityRequirement(name = "Bearer Token")
			)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Device added successfully",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(example = "{ \"id\": 1, \"name\": \"John Doe\", \"password\": \"$2a$10$hvTuh0Hx1HNUO0R7A8PbkeNhGN52ncaWvyyxZzYljy2uj9HzpCAPG\", \"email\": \"john.doe@example.com\", \"activos\": [] }")
							)
					),
			@ApiResponse(
					responseCode = "404",
					description = "Device not found",
					content = @Content(
							schema = @Schema(example = "")
							)
					)
	})
	@PutMapping("/{id}")
	public ResponseEntity<Usuario> addDeviceToAuthUser(@Parameter(description = "ID of the device to add", example="1", required = true)
	@PathVariable Long id) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario currentUser = (Usuario) authentication.getPrincipal();

		ResponseEntity<Usuario> result;
		Usuario uMod = dispositivoService.addDeviceToUser(currentUser, id);
		if (uMod == null) {
			result = ResponseEntity.notFound().build();
		} else {
			result = ResponseEntity.ok(uMod);
		}

		return result;
	}

	@Operation(
			summary = "Remove device from authenticated user",
			description = "Removes a device from the authenticated user's list of devices.",
			security = @SecurityRequirement(name = "Bearer Token")
			)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200", 
					description = "Device removed successfully", 
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(example = "{ \"id\": 1, \"name\": \"John Doe\", \"password\": \"$2a$10$hvTuh0Hx1HNUO0R7A8PbkeNhGN52ncaWvyyxZzYljy2uj9HzpCAPG\", \"email\": \"john.doe@example.com\", \"activos\": [] }")
							)),
			@ApiResponse(
					responseCode = "404", 
					description = "Device not found",
					content = @Content(
							schema = @Schema(example = "")
							))
	})
	@DeleteMapping("/{id}")
	public ResponseEntity<Usuario> removeDeviceFromAuthUser(@Parameter(description = "ID of the device to remove", example = "1", required = true)
	@PathVariable Long id) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario currentUser = (Usuario) authentication.getPrincipal();

		ResponseEntity<Usuario> result;
		Usuario uMod = dispositivoService.removeDeviceFromUser(currentUser, id);
		if (uMod == null) {
			result = ResponseEntity.notFound().build();
		} else {
			result = ResponseEntity.ok(uMod);
		}

		return result;
	}
}
