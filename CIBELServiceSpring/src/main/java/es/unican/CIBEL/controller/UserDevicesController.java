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

@RestController
@RequestMapping("me/dispositivos")
public class UserDevicesController {
	
	@Autowired
	private DispositivoService dispositivoService;
	
	@GetMapping
	public List<Dispositivo> getAuthUserDevices() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario currentUser = (Usuario) authentication.getPrincipal();
		
		return currentUser.getActivos().stream()
                .filter(activo -> activo instanceof Dispositivo)
                .map(activo -> (Dispositivo) activo)
                .collect(Collectors.toList());
	}
	
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
	
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> addDeviceToAuthUser(@PathVariable Long id) {
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
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Usuario> removeDeviceFromAuthUser(@PathVariable Long id) {
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
