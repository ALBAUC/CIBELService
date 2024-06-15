package es.unican.CIBEL.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.unican.CIBEL.domain.Aplicacion;
import es.unican.CIBEL.domain.Tipo;
import es.unican.CIBEL.domain.Vulnerabilidad;
import es.unican.CIBEL.repository.AppRepository;

@Service
public class AppService {
	
	@Autowired
	private AppRepository appRepository;
	
	public List<Aplicacion> aplicaciones() {
		return appRepository.findAll();
	}
	
	public Aplicacion getAppById(Long id) {
		return appRepository.findById(id).orElse(null);
	}
	
	public List<Vulnerabilidad> getVulnerabilidadesAplicaciones() {
        List<Aplicacion> aplicaciones = appRepository.findAll();
        return aplicaciones.stream()
                .flatMap(aplicacion -> aplicacion.getVulnerabilidades().stream())
                .distinct()
                .collect(Collectors.toList());
    }

    public List<Tipo> getTiposAplicaciones() {
        List<Aplicacion> aplicaciones = appRepository.findAll();
        return aplicaciones.stream()
                .map(Aplicacion::getTipo)
                .distinct()
                .collect(Collectors.toList());
    }

}
