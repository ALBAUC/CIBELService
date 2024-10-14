package es.unican.CIBEL.controller;

import es.unican.CIBEL.domain.Usuario;
import es.unican.CIBEL.service.AuthenticationService;
import es.unican.CIBEL.util.JwtUtil;
import es.unican.CIBEL.util.LoginResponse;
import es.unican.CIBEL.dto.LoginUserDto;
import es.unican.CIBEL.dto.RegisterUserDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Endpoints for user authentication and registration")
public class AuthController {

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private AuthenticationService authenticationService;

	@Operation(
			summary = "User registration",
			description = "Registers a new user with the provided details."
			)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200", 
					description = "User registered successfully",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(example = "{ \"id\": 10, \"name\": \"Test\", \"password\": \"$2a$10$mm/tmq7BqdRUn9oxX0cbOut2KNGsZUVDV3kk7hG7cykeUQuTZY7IW\", \"email\": \"test@hotmail.com\", \"activos\": [], \"enabled\": true, \"accountNonExpired\": true, \"accountNonLocked\": true, \"credentialsNonExpired\": true, \"username\": \"test@hotmail.com\", \"authorities\": [] }")
							)
					),
			@ApiResponse(
					responseCode = "400", 
					description = "Invalid registration details",
					content = @Content(
							schema = @Schema(example = "")
							)
					)
	})
	@PostMapping("/signup")
	public ResponseEntity<Usuario> register(
			@Parameter(description = "User registration details") 
			@RequestBody RegisterUserDto registerUserDto) {

		Usuario registeredUser = authenticationService.register(registerUserDto);
		return ResponseEntity.ok(registeredUser);
	}

	@Operation(
			summary = "User login",
			description = "Authenticates a user and returns a JWT token."
			)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200", 
					description = "User authenticated successfully",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(example = "{ \"token\": \"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbGluYXNvbG9uYXJ1QGhvdG1haWwuY29tIiwiaWF0IjoxNzI4OTI4Nzc1LCJleHAiOjE3Mjg5NjQ3NzV9.gJ2eiD3W1OEDMhEPYgZNcQ5bG5yXMcQMQWGrR2o3SMI\", \"expiresIn\": 36000000 }")
							)
					),
			@ApiResponse(
					responseCode = "401", 
					description = "Invalid username or password",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(example = "{ \"type\": \"about:blank\", \"title\": \"Unauthorized\", \"status\": 401, \"detail\": \"Bad credentials\", \"instance\": \"/auth/login\", \"description\": \"The username or password is incorrect\" }")
							)
					)
	})
	@PostMapping("/login")
	public ResponseEntity<LoginResponse> authenticate(
			@Parameter(description = "User login credentials") 
			@RequestBody LoginUserDto loginUserDto) {

		Usuario authenticatedUser = authenticationService.authenticate(loginUserDto);
		String jwtToken = jwtUtil.generateToken(authenticatedUser);

		LoginResponse loginResponse = new LoginResponse();
		loginResponse.setToken(jwtToken);
		loginResponse.setExpiresIn(jwtUtil.getExpirationTime());

		return ResponseEntity.ok(loginResponse);
	}
}
