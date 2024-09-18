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

import es.unican.CIBEL.domain.Aplicacion;
import es.unican.CIBEL.domain.Usuario;
import es.unican.CIBEL.service.AppService;

public class UserAppsController {
	@Autowired
	private AppService appService;
	
    @GetMapping
	public List<Aplicacion> getAuthUserApps() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario currentUser = (Usuario) authentication.getPrincipal();
		
		return currentUser.getActivos().stream()
                .filter(activo -> activo instanceof Aplicacion)
                .map(activo -> (Aplicacion) activo)
                .collect(Collectors.toList());
	}
    
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> addAppToAuthUser(@PathVariable Long id) {
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
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Usuario> removeAppFromAuthUser(@PathVariable Long id) {
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
