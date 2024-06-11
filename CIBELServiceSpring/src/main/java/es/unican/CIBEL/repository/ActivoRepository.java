package es.unican.CIBEL.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import es.unican.CIBEL.domain.Categoria;
import es.unican.CIBEL.domain.Tipo;
import es.unican.CIBEL.domain.Activo;

public interface ActivoRepository extends JpaRepository<Activo, Long> {
	
	public List<Activo> findByCategoria(Categoria categoria);
	
	public Activo findByNombre(String nombre);

	public List<Activo> findByTipo(Tipo tipo);
}
