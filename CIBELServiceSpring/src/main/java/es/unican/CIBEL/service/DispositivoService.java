package es.unican.CIBEL.service;

import es.unican.CIBEL.domain.Dispositivo;
import es.unican.CIBEL.domain.Tipo;
import es.unican.CIBEL.domain.Vulnerabilidad;
import es.unican.CIBEL.repository.DispositivoRepository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DispositivoService {

    @Autowired
    private DispositivoRepository dispositivoRepository;

    public List<Dispositivo> dispositivos() {
        return dispositivoRepository.findAll();
    }

    public Dispositivo getDispositivoById(Long id) {
        return dispositivoRepository.findById(id).orElse(null);
    }

    public List<Vulnerabilidad> getVulnerabilidadesDispositivos() {
        List<Dispositivo> dispositivos = dispositivoRepository.findAll();
        return dispositivos.stream()
                .flatMap(dispositivo -> dispositivo.getVulnerabilidades().stream())
                .distinct()
                .collect(Collectors.toList());
    }

    public List<Tipo> getTiposDispositivos() {
        List<Dispositivo> dispositivos = dispositivoRepository.findAll();
        return dispositivos.stream()
                .map(Dispositivo::getTipo)
                .distinct()
                .collect(Collectors.toList());
    }
}
