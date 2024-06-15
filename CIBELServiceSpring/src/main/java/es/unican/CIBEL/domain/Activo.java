package es.unican.CIBEL.domain;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Activo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String nombre;
	
	@Column(length = 500)
	private String icono;
	
	@OneToOne
	@JoinColumn(name ="fk_tipo")
	private Tipo tipo;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "activo_x_vulnerabilidad",
				joinColumns = @JoinColumn(name = "fk_activo"),
				inverseJoinColumns = @JoinColumn(name = "fk_vulnerabilidad"))
	private List<Vulnerabilidad> vulnerabilidades;
	
	public Activo() {}
	
	public Activo(String nombre, String icono, Tipo tipo) {
		this.nombre = nombre;
		this.icono = icono;
		this.tipo = tipo;
		this.vulnerabilidades = new LinkedList<Vulnerabilidad>();
	}

	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getIcono() {
		return icono;
	}
	
	public void setIcono(String icono) {
		this.icono = icono;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Tipo getTipo() {
		return tipo;
	}

	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}

	public List<Vulnerabilidad> getVulnerabilidades() {
		return vulnerabilidades;
	}

	public void setVulnerabilidades(List<Vulnerabilidad> vulnerabilidades) {
		this.vulnerabilidades = vulnerabilidades;
	}

	@Override
	public int hashCode() {
		return Objects.hash(icono, id, nombre, tipo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Activo other = (Activo) obj;
		return Objects.equals(icono, other.icono)
				&& Objects.equals(id, other.id) && Objects.equals(nombre, other.nombre)
				&& Objects.equals(tipo, other.tipo);
	}

	@Override
	public String toString() {
		return "Activo [id=" + id + ", nombre=" + nombre + ", icono=" + icono + "]";
	}
}
