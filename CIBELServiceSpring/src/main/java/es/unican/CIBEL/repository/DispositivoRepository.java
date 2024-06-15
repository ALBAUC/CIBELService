package es.unican.CIBEL.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import es.unican.CIBEL.domain.Dispositivo;

public interface DispositivoRepository extends JpaRepository<Dispositivo, Long> {

	public Dispositivo findByNombre(String nombre);

}
