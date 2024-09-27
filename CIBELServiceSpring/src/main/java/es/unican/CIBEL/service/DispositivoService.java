package es.unican.CIBEL.service;

import es.unican.CIBEL.domain.Dispositivo;
import es.unican.CIBEL.domain.Tipo;
import es.unican.CIBEL.domain.Usuario;
import es.unican.CIBEL.domain.Vulnerabilidad;
import es.unican.CIBEL.repository.DispositivoRepository;
import es.unican.CIBEL.repository.UsuarioRepository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DispositivoService {

	@Autowired
	private DispositivoRepository dispositivoRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	public List<Dispositivo> dispositivos() {
		return dispositivoRepository.findAll();
	}

	public Dispositivo getDispositivoById(Long id) {
		return dispositivoRepository.findById(id).orElse(null);
	}

	public List<Vulnerabilidad> getVulnerabilidadesDispositivos() {
		List<Dispositivo> dispositivos = dispositivoRepository.findAll();
		return dispositivos.stream().flatMap(dispositivo -> dispositivo.getVulnerabilidades().stream()).distinct()
				.collect(Collectors.toList());
	}

	public List<Tipo> getTiposDispositivos() {
		List<Dispositivo> dispositivos = dispositivoRepository.findAll();
		return dispositivos.stream().map(Dispositivo::getTipo).distinct().collect(Collectors.toList());
	}

	public Usuario addDeviceToUser(Usuario usuario, Long dispositivoId) {
		Usuario result = null;
		Dispositivo actualDevice = getDispositivoById(dispositivoId);
		if (actualDevice != null) {
			usuario.getActivos().add(actualDevice);
			usuarioRepository.save(usuario);
			result = usuario;
		}
		return result;
	}

	public Usuario removeDeviceFromUser(Usuario usuario, Long deviceId) {
	    boolean removed = usuario.getActivos().removeIf(activo -> activo.getId().equals(deviceId) && activo instanceof Dispositivo);
	    if (removed) {
	        usuarioRepository.save(usuario);
	        return usuario;
	    } else {
	        return null;
	    }
	}

	public List<Dispositivo> getDispositivosByTipo(String tipo) {
		return dispositivoRepository.findByTipoNombre(tipo);
	}
}
