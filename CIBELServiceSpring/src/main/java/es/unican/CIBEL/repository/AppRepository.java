package es.unican.CIBEL.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.unican.CIBEL.domain.Aplicacion;

public interface AppRepository extends JpaRepository<Aplicacion, Long> {
	public Aplicacion findByNombre(String nombre);
}
