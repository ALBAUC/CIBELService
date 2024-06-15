package es.unican.CIBEL.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.unican.CIBEL.domain.Aplicacion;
import es.unican.CIBEL.domain.Tipo;
import es.unican.CIBEL.domain.Vulnerabilidad;
import es.unican.CIBEL.service.AppService;

@RestController
@RequestMapping("apps")
public class AppController {
	
	@Autowired
    private AppService aplicacionService;
	
	@GetMapping
    public List<Aplicacion> getAllAplicaciones() {
        return aplicacionService.aplicaciones();
    }
	
	@GetMapping("/{id}")
    public ResponseEntity<Aplicacion> getAplicacionById(@PathVariable Long id) {
        ResponseEntity<Aplicacion> result;
        Aplicacion aplicacion = aplicacionService.getAppById(id);

        if (aplicacion == null) {
            result = ResponseEntity.notFound().build();
        } else {
            result = ResponseEntity.ok(aplicacion);
        }

        return result;
    }

    @GetMapping("/vulnerabilidades")
    public List<Vulnerabilidad> getVulnerabilidadesAplicaciones() {
        return aplicacionService.getVulnerabilidadesAplicaciones();
    }

    @GetMapping("/tipos")
    public List<Tipo> getTiposAplicaciones() {
        return aplicacionService.getTiposAplicaciones();
    }

}
