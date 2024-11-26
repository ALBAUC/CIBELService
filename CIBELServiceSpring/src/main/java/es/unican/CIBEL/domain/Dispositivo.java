package es.unican.CIBEL.domain;

import jakarta.persistence.Entity;
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Schema(description = "Represents a device, which is a type of asset with various sustainability metrics.")
public class Dispositivo extends Activo {

    @Schema(description = "Durability score of the device.")
    private int durabilidad;

    @Schema(description = "Repairability score of the device.")
    private int reparabilidad;

    @Schema(description = "Recyclability score of the device.")
    private int reciclabilidad;

    @Schema(description = "Climate efficiency score of the device.")
    private int efClimatica;

    @Schema(description = "Resource efficiency score of the device.")
    private int efRecursos;

    @Schema(description = "Eco score of the device.")
    private int ecoPuntuacion;
    
    @Schema(description = "Security score of the device.")
    private int securityScore;
    
    @Schema(description = "Indicates whether the eco score value is estimated (true - 1) or real (false - 0).")
    private boolean ecoPredicted;
    
    @Schema(description = "Indicates whether the security score value is estimated (true - 1) or real (false - 0).")
    private boolean securityPredicted;

	public Dispositivo() {
        // Constructor vacío
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
    
    public int getSecurityScore() {
		return securityScore;
	}

	public void setSecurityScore(int securityScore) {
		this.securityScore = securityScore;
	}

    public boolean isEcoPredicted() {
		return ecoPredicted;
	}

	public void setEcoPredicted(boolean ecoPredicted) {
		this.ecoPredicted = ecoPredicted;
	}

	public boolean isSecurityPredicted() {
		return securityPredicted;
	}

	public void setSecurityPredicted(boolean securityPredicted) {
		this.securityPredicted = securityPredicted;
	}

	@Override
    public String toString() {
        return "Dispositivo [id=" + super.getId() + ", nombre=" + super.getNombre() + ", icono=" + super.getIcono() + "]";
    }
}
