package es.unican.CIBEL.domain;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

@Entity
public class Activo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String nombre;
	
	@Column(length = 500)
	private String icono;
	
	@OneToOne
	@JoinColumn(name="fk_categoria")
	private Categoria categoria;
	
	@OneToOne
	@JoinColumn(name ="fk_tipo")
	private Tipo tipo;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "activo_x_vulnerabilidad",
				joinColumns = @JoinColumn(name = "fk_activo"),
				inverseJoinColumns = @JoinColumn(name = "fk_vulnerabilidad"))
	private List<Vulnerabilidad> vulnerabilidades;
	
	private int durabilidad;
	private int reparabilidad;
	private int reciclabilidad;
	private int efClimatica;
	private int efRecursos;
	private int ecoPuntuacion;
	
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
	
	public Categoria getCategoria() {
		return categoria;
	}
	
	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
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

	public int getDurabilidad() {
		return durabilidad;
	}

	public void setDurabilidad(int durabilidad) {
		this.durabilidad = durabilidad;
	}

	public int getReparabilidad() {
		return reparabilidad;
	}

	public void setReparabilidad(int reparabilidad) {
		this.reparabilidad = reparabilidad;
	}

	public int getReciclabilidad() {
		return reciclabilidad;
	}

	public void setReciclabilidad(int reciclabilidad) {
		this.reciclabilidad = reciclabilidad;
	}

	public int getEfClimatica() {
		return efClimatica;
	}

	public void setEfClimatica(int efClimatica) {
		this.efClimatica = efClimatica;
	}

	public int getEfRecursos() {
		return efRecursos;
	}

	public void setEfRecursos(int efRecursos) {
		this.efRecursos = efRecursos;
	}

	public int getEcoPuntuacion() {
		return ecoPuntuacion;
	}

	public void setEcoPuntuacion(int ecoPuntuacion) {
		this.ecoPuntuacion = ecoPuntuacion;
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
