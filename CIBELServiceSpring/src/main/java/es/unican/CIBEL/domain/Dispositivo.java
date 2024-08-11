package es.unican.CIBEL.domain;

import java.util.List;

import jakarta.persistence.Entity;

@Entity
public class Dispositivo extends Activo {
	
	private int durabilidad;
	private int reparabilidad;
	private int reciclabilidad;
	private int efClimatica;
	private int efRecursos;
	private int ecoPuntuacion;
	
	public Dispositivo() {
		
	}
	
	public Dispositivo(String nombre, String icono, Tipo tipo) {
		super(nombre, icono, tipo);
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
	
	public int calcularPuntuacionSeguridad() {
        // Considero un límite de 400 como la máxima gravedad posible
        int puntuacionSeguridad = (int) Math.round(100 - (calcularTotalGravedad() / 4));
        return Math.max(0, Math.min(100, puntuacionSeguridad));
    }

    public double calcularTotalGravedad() {
        List<Vulnerabilidad> vulnerabilidades = getVulnerabilidades();
        int totalGravedad = 0;

        for (Vulnerabilidad v : vulnerabilidades) {
            totalGravedad += v.getBaseScore() * 10; // baseScore de 0 a 100
        }

        return totalGravedad;
    }

	@Override
	public String toString() {
		return "Dispositivo [id=" + super.getId() + ", nombre=" + super.getNombre() + ", icono=" + super.getIcono() + "]";
	}

}
