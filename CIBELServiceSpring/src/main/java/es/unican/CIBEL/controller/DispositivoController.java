package es.unican.CIBEL.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.unican.CIBEL.domain.Dispositivo;
import es.unican.CIBEL.domain.Tipo;
import es.unican.CIBEL.domain.Vulnerabilidad;
import es.unican.CIBEL.service.DispositivoService;

@RestController
@RequestMapping("dispositivos")
public class DispositivoController {
	
	@Autowired
	private DispositivoService dispositivoService;
	
	@GetMapping
	public List<Dispositivo> getAllDispositivos(@RequestParam(required = false) String tipo) {
        // Si 'tipo' es proporcionado, filtrar los dispositivos
        if (tipo != null) {
            return dispositivoService.getDispositivosByTipo(tipo);
        }
        // Si no se proporciona, devolver todos los dispositivos
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
}
