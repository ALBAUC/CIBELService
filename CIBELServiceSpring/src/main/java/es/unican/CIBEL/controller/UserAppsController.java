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

import es.unican.CIBEL.domain.Aplicacion;
import es.unican.CIBEL.domain.Usuario;
import es.unican.CIBEL.service.AppService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "User apps", description = "Operations related to user apps")
@RestController
@RequestMapping("me/apps")
public class UserAppsController {

	@Autowired
	private AppService appService;

	@Operation(
			summary = "Get authenticated user's applications",
			description = "Returns a list of applications owned by the authenticated user.",
			security = @SecurityRequirement(name = "Bearer Token")
			)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Successfully retrieves the apps belonging to the authenticated user.",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(example = "[{ \"id\": 183, \"nombre\": \"Instagram\", \"icono\": \"https://i.imgur.com/MNaIwAy.jpg\", \"tipo\": { \"id\": 32, \"nombre\": \"Redes Sociales\", \"nombre_en\": \"Social Networks\" }, \"vulnerabilidades\": [] }]")
							)
					)
	})
	@GetMapping
	public List<Aplicacion> getAuthUserApps() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario currentUser = (Usuario) authentication.getPrincipal();

		return currentUser.getActivos().stream()
				.filter(activo -> activo instanceof Aplicacion)
				.map(activo -> (Aplicacion) activo)
				.collect(Collectors.toList());
	}

	@Operation(
			summary = "Add application to authenticated user",
			description = "Adds an application to the authenticated user's list of applications.",
			security = @SecurityRequirement(name = "Bearer Token")
			)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200", 
					description = "Application added successfully",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(example = "{ \"id\": 1, \"name\": \"John Doe\", \"password\": \"$2a$10$hvTuh0Hx1HNUO0R7A8PbkeNhGN52ncaWvyyxZzYljy2uj9HzpCAPG\", \"email\": \"john.doe@example.com\", \"activos\": [] }")
							)
					),
			@ApiResponse(
					responseCode = "404", 
					description = "Application not found",
					content = @Content(
							schema = @Schema(example = "")
							)
					)
	})
	@PutMapping("/{id}")
	public ResponseEntity<Usuario> addAppToAuthUser(@Parameter(description = "ID of the application to add", example = "183", required = true)
	@PathVariable Long id) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario currentUser = (Usuario) authentication.getPrincipal();

		ResponseEntity<Usuario> result;
		Usuario uMod = appService.addAppToUser(currentUser, id);
		if (uMod == null) {
			result = ResponseEntity.notFound().build();
		} else {
			result = ResponseEntity.ok(uMod);
		}

		return result;
	}

	@Operation(
			summary = "Remove application from authenticated user",
			description = "Removes an application from the authenticated user's list of applications.",
			security = @SecurityRequirement(name = "Bearer Token")
			)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200", 
					description = "Application removed successfully",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(example = "{ \"id\": 1, \"name\": \"John Doe\", \"password\": \"$2a$10$hvTuh0Hx1HNUO0R7A8PbkeNhGN52ncaWvyyxZzYljy2uj9HzpCAPG\", \"email\": \"john.doe@example.com\", \"activos\": [] }")
							)
					),
			@ApiResponse(
					responseCode = "404", 
					description = "Application not found",
					content = @Content(
							schema = @Schema(example = "")
							)
					)
	})
	@DeleteMapping("/{id}")
	public ResponseEntity<Usuario> removeAppFromAuthUser(@Parameter(description = "ID of the application to remove", example = "183", required = true)
	@PathVariable Long id) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario currentUser = (Usuario) authentication.getPrincipal();

		ResponseEntity<Usuario> result;
		Usuario uMod = appService.removeAppFromUser(currentUser, id);
		if (uMod == null) {
			result = ResponseEntity.notFound().build();
		} else {
			result = ResponseEntity.ok(uMod);
		}

		return result;
	}
}
