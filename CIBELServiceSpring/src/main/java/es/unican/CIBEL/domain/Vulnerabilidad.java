package es.unican.CIBEL.domain;

import java.util.Objects;

import jakarta.persistence.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Schema(description = "Represents a vulnerability with associated impacts and severity.")
public class Vulnerabilidad {
	
	@Id
	@Schema(description = "Unique identifier for the vulnerability (CVE identifier).")
	private String idCVE;
	
	@Column(length = 5000)
	@Schema(description = "Description of the vulnerability in Spanish.")
	private String descripcion;
	
	@Column(length = 5000)
	@Schema(description = "Description of the vulnerability in English.")
	private String descripcion_en;
	
    @Schema(description = "Impact on confidentiality which can be COMPLETE, HIGH, PARTIAL, LOW or NONE.")
    private String confidentialityImpact;
    
    @Schema(description = "Impact on integrity which can be COMPLETE, HIGH, PARTIAL, LOW or NONE.")
    private String integrityImpact;
    
    @Schema(description = "Impact on availability which can be COMPLETE, HIGH, PARTIAL, LOW or NONE.")
    private String availabilityImpact;
    
    @Schema(description = "Base score indicating the severity of the vulnerability from 0 to 10.")
    private double baseScore;
    
    @Schema(description = "Base severity classification of the vulnerability which can be CRITICAL, HIGH, MEDIUM and LOW.")
    private String baseSeverity;
    
    public Vulnerabilidad() {}

	public Vulnerabilidad(String idCVE, String descripcion, String confidentialityImpact, String integrityImpact,
			String availabilityImpact, double baseScore, String baseSeverity) {
		this.idCVE = idCVE;
		this.descripcion = descripcion;
		this.confidentialityImpact = confidentialityImpact;
		this.integrityImpact = integrityImpact;
		this.availabilityImpact = availabilityImpact;
		this.baseScore = baseScore;
		this.baseSeverity = baseSeverity;
	}

	public String getIdCVE() {
		return idCVE;
	}

	public void setIdCVE(String idCVE) {
		this.idCVE = idCVE;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getConfidentialityImpact() {
		return confidentialityImpact;
	}

	public void setConfidentialityImpact(String confidentialityImpact) {
		this.confidentialityImpact = confidentialityImpact;
	}

	public String getIntegrityImpact() {
		return integrityImpact;
	}

	public void setIntegrityImpact(String integrityImpact) {
		this.integrityImpact = integrityImpact;
	}

	public String getAvailabilityImpact() {
		return availabilityImpact;
	}

	public void setAvailabilityImpact(String availabilityImpact) {
		this.availabilityImpact = availabilityImpact;
	}

	public double getBaseScore() {
		return baseScore;
	}

	public void setBaseScore(double baseScore) {
		this.baseScore = baseScore;
	}

	public String getBaseSeverity() {
		return baseSeverity;
	}

	public void setBaseSeverity(String baseSeverity) {
		this.baseSeverity = baseSeverity;
	}

	public String getDescripcion_en() {
		return descripcion_en;
	}

	public void setDescripcion_en(String descripcion_en) {
		this.descripcion_en = descripcion_en;
	}

	@Override
	public int hashCode() {
		return Objects.hash(availabilityImpact, baseScore, baseSeverity, confidentialityImpact, idCVE, integrityImpact);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vulnerabilidad other = (Vulnerabilidad) obj;
		return Objects.equals(availabilityImpact, other.availabilityImpact)
				&& Double.doubleToLongBits(baseScore) == Double.doubleToLongBits(other.baseScore)
				&& Objects.equals(baseSeverity, other.baseSeverity)
				&& Objects.equals(confidentialityImpact, other.confidentialityImpact)
				&& Objects.equals(idCVE, other.idCVE) && Objects.equals(integrityImpact, other.integrityImpact);
	}
}
