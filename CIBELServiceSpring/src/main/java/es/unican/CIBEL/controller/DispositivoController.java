package es.unican.CIBEL.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.unican.CIBEL.domain.Dispositivo;
import es.unican.CIBEL.domain.Tipo;
import es.unican.CIBEL.domain.Usuario;
import es.unican.CIBEL.domain.Vulnerabilidad;
import es.unican.CIBEL.service.DispositivoService;

@RestController
@RequestMapping("dispositivos")
public class DispositivoController {
	
	@Autowired
	private DispositivoService dispositivoService;
	
	@GetMapping
    public List<Dispositivo> getAllDispositivos() {
        return dispositivoService.dispositivos();
    }
	
	@GetMapping("/{id}")
	public ResponseEntity<Dispositivo> getDispositivoById(@PathVariable Long id) {
		ResponseEntity<Dispositivo> result;
		Dispositivo dispositivo = dispositivoService.getDispositivoById(id);
		
		if (dispositivo == null) {
			result = ResponseEntity.notFound().build();
		} else {
			result = ResponseEntity.ok(dispositivo);
		}
		
		return result;
	}
	
	@GetMapping("/vulnerabilidades")
	public List<Vulnerabilidad> getVulnerabilidadesDispositivos() {
		return dispositivoService.getVulnerabilidadesDispositivos();
	}
	
	@GetMapping("/tipos")
	public List<Tipo> getTiposDispositivos() {
		return dispositivoService.getTiposDispositivos();
	}
	
	@GetMapping("/me")
	public List<Dispositivo> getAuthUserDevices() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario currentUser = (Usuario) authentication.getPrincipal();
		
		return currentUser.getActivos().stream()
                .filter(activo -> activo instanceof Dispositivo)
                .map(activo -> (Dispositivo) activo)
                .collect(Collectors.toList());
	}
	
}
