package es.unican.CIBEL.domain;

import java.util.Objects;

import jakarta.persistence.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Schema(description = "Represents a category of asset.")
public class Tipo {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Schema(description = "Unique identifier for the asset type.")
	private Long id;
	
	@Schema(description = "Name of the asset type in Spanish.")
	private String nombre;

	@Schema(description = "Name of the asset type in English.")
	private String nombre_en;

	public Tipo() {}

	public Tipo(String nombre) {
		this.nombre = nombre;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNombre_en() {
		return nombre_en;
	}

	public void setNombre_en(String nombre_en) {
		this.nombre_en = nombre_en;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, nombre);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tipo other = (Tipo) obj;
		return Objects.equals(id, other.id) && Objects.equals(nombre, other.nombre);
	}
}
