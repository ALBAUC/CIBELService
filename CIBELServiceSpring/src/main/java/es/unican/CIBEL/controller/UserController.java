package es.unican.CIBEL.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.unican.CIBEL.domain.Usuario;
import es.unican.CIBEL.dto.UpdateNameDto;
import es.unican.CIBEL.dto.UpdatePasswordDto;
import es.unican.CIBEL.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("me")
@Tag(name = "User Management", description = "Endpoints for managing the authenticated user")
public class UserController {

	@Autowired
	private UserService userService;

	@Operation(
			summary = "Get authenticated user details",
			description = "Retrieves the details of the currently authenticated user.",
			security = @SecurityRequirement(name = "Bearer Token")
			)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200", 
					description = "User details retrieved successfully",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(example = "{ \"id\": 1, \"name\": \"John Doe\", \"password\": \"$2a$10$hvTuh0Hx1HNUO0R7A8PbkeNhGN52ncaWvyyxZzYljy2uj9HzpCAPG\", \"email\": \"john.doe@example.com\", \"activos\": [] }")
							)
					),
			@ApiResponse(
					responseCode = "403", 
					description = "User not authenticated",
					content = @Content(
							schema = @Schema(example = "")
							)
					)
	})
	@GetMapping
	public ResponseEntity<Usuario> getAuthUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario currentUser = (Usuario) authentication.getPrincipal();
		return ResponseEntity.ok(currentUser);
	}

	@Operation(
			summary = "Update user name",
			description = "Updates the name of the authenticated user.",
			security = @SecurityRequirement(name = "Bearer Token")
			)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200", 
					description = "User name updated successfully",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(example = "{ \"id\": 1, \"name\": \"John Doe\", \"password\": \"$2a$10$hvTuh0Hx1HNUO0R7A8PbkeNhGN52ncaWvyyxZzYljy2uj9HzpCAPG\", \"email\": \"john.doe@example.com\", \"activos\": [] }")
							)
					),
			@ApiResponse(
					responseCode = "400", 
					description = "Invalid name input",
					content = @Content(
							schema = @Schema(example = "")
							)
					),
			@ApiResponse(
					responseCode = "403", 
					description = "User not authenticated",
					content = @Content(
							schema = @Schema(example = "")
							)
					)
	})
	@PutMapping("/name")
	public ResponseEntity<Usuario> updateUserName(
			@Parameter(description = "New name for the user") 
			@RequestBody UpdateNameDto updateNameDto) {

		String newName = updateNameDto.getNewName();
		if (newName == null || newName.trim().isEmpty()) {
			return ResponseEntity.badRequest().body(null); // Si el nuevo nombre es inválido
		}

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario currentUser = (Usuario) authentication.getPrincipal();

		currentUser.setName(newName);
		Usuario updatedUser = userService.save(currentUser); // Actualizar en la BD

		return ResponseEntity.ok(updatedUser);
	}

	@Operation(
			summary = "Update user password",
			description = "Updates the password of the authenticated user.",
			security = @SecurityRequirement(name = "Bearer Token")
			)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200", 
					description = "Password updated successfully",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(example = "Password updated successfully")
							)
					),
			@ApiResponse(
					responseCode = "400", 
					description = "Invalid password input",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(example = "Invalid password input")
							)
					),
			@ApiResponse(
					responseCode = "401", 
					description = "Incorrect old password",
					content = @Content(
							mediaType = "text/plain",
							schema = @Schema(example = "Incorrect old password")
							)
					),
			@ApiResponse(
					responseCode = "403", 
					description = "User not authenticated",
					content = @Content(
							schema = @Schema(example = "")
							)
					)
	})
	@PutMapping("/password")
	public ResponseEntity<String> updatePassword(
			@Parameter(description = "Old and new passwords for the user") 
			@RequestBody UpdatePasswordDto updatePasswordDto) {

		String oldPassword = updatePasswordDto.getOldPassword();
		String newPassword = updatePasswordDto.getNewPassword();

		if (oldPassword == null || newPassword == null || newPassword.trim().isEmpty()) {
			return ResponseEntity.badRequest().body("Invalid password input");
		}

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario currentUser = (Usuario) authentication.getPrincipal();

		// Verificar la contraseña actual
		if (!userService.checkPassword(currentUser, oldPassword)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect old password");
		}

		// Actualizar la contraseña
		currentUser.setPassword(userService.encodePassword(newPassword)); // Encriptar nueva contraseña
		userService.save(currentUser);

		return ResponseEntity.ok("Password updated successfully");
	}

	@Operation(
			summary = "Delete user account",
			description = "Deletes the authenticated user account.",
			security = @SecurityRequirement(name = "Bearer Token")
			)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200", 
					description = "User deleted successfully",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(example = "[]")
							)
					),
			@ApiResponse(
					responseCode = "403", 
					description = "User not authenticated",
					content = @Content(
							schema = @Schema(example = "")
							)
					)
	})
	@DeleteMapping
	public ResponseEntity<String> deleteUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario currentUser = (Usuario) authentication.getPrincipal();

		userService.delete(currentUser); // Eliminar el usuario de la BD

		return ResponseEntity.ok("User deleted successfully");
	}
}
