package es.unican.CIBEL.domain;

import jakarta.persistence.Entity;

@Entity
public class VulnerabilidadApp extends Vulnerabilidad {
	
    private String versionEndExcluding;
	private String versionEndIncluding;
	
	public VulnerabilidadApp() {}
	
	public VulnerabilidadApp(String idCVE, String descripcion, String confidentialityImpact, String integrityImpact,
			String availabilityImpact, double baseScore, String baseSeverity) {
		super(idCVE, descripcion, confidentialityImpact, integrityImpact, availabilityImpact, baseScore, baseSeverity);
	}
	
	public String getVersionEndExcluding() {
		return versionEndExcluding;
	}

	public void setVersionEndExcluding(String versionEndExcluding) {
		this.versionEndExcluding = versionEndExcluding;
	}

	public String getVersionEndIncluding() {
		return versionEndIncluding;
	}

	public void setVersionEndIncluding(String versionEndIncluding) {
		this.versionEndIncluding = versionEndIncluding;
	}
}
