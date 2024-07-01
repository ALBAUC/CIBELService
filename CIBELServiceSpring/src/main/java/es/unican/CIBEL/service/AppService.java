package es.unican.CIBEL.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.unican.CIBEL.domain.Aplicacion;
import es.unican.CIBEL.domain.Tipo;
import es.unican.CIBEL.domain.Usuario;
import es.unican.CIBEL.domain.Vulnerabilidad;
import es.unican.CIBEL.exceptions.AssetNotFoundException;
import es.unican.CIBEL.repository.AppRepository;
import es.unican.CIBEL.repository.UsuarioRepository;

@Service
public class AppService {

	@Autowired
	private AppRepository appRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	public List<Aplicacion> aplicaciones() {
		return appRepository.findAll();
	}

	public Aplicacion getAppById(Long id) {
		return appRepository.findById(id).orElse(null);
	}

	public List<Vulnerabilidad> getVulnerabilidadesAplicaciones() {
		List<Aplicacion> aplicaciones = appRepository.findAll();
		return aplicaciones.stream().flatMap(aplicacion -> aplicacion.getVulnerabilidades().stream()).distinct()
				.collect(Collectors.toList());
	}

	public List<Tipo> getTiposAplicaciones() {
		List<Aplicacion> aplicaciones = appRepository.findAll();
		return aplicaciones.stream().map(Aplicacion::getTipo).distinct().collect(Collectors.toList());
	}

	public Usuario addAppToUser(Usuario usuario, Long appId) {
		if (appId != null) {
			Aplicacion actualApp = getAppById(appId);
			if (actualApp != null) {
				usuario.getActivos().add(actualApp);
				usuarioRepository.save(usuario);
			} else {
				throw new AssetNotFoundException(appId);
			}

		} else {
			throw new IllegalArgumentException("App ID cannot be null");
		}

		return usuario;
	}

	public void removeAppFromUser(Usuario user, Long appId) {
		user.getActivos().removeIf(activo -> activo.getId().equals(appId) && activo instanceof Aplicacion);
		usuarioRepository.save(user);
	}

}
