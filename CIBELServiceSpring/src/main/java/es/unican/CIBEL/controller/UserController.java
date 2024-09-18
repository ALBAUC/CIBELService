package es.unican.CIBEL.controller;

import java.util.Map;

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
import es.unican.CIBEL.service.UserService;

@RestController
@RequestMapping("me")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping
	public ResponseEntity<Usuario> getAuthUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario currentUser = (Usuario) authentication.getPrincipal();
		return ResponseEntity.ok(currentUser);
	}
	
	@PutMapping("/name")
    public ResponseEntity<Usuario> updateUserName(@RequestBody Map<String, String> request) {
        String newName = request.get("newName");
        if (newName == null || newName.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(null); // Si el nuevo nombre es inválido
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario currentUser = (Usuario) authentication.getPrincipal();

        currentUser.setName(newName);
        Usuario updatedUser = userService.save(currentUser); // Actualizar en la BD

        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/password")
    public ResponseEntity<String> updatePassword(@RequestBody Map<String, String> request) {
        String oldPassword = request.get("oldPassword");
        String newPassword = request.get("newPassword");

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

    @DeleteMapping
    public ResponseEntity<String> deleteUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario currentUser = (Usuario) authentication.getPrincipal();

        userService.delete(currentUser); // Eliminar el usuario de la BD

        return ResponseEntity.ok("User deleted successfully");
    }

}
