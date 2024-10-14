package es.unican.CIBEL.domain;

import jakarta.persistence.Entity;
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Schema(description = "Represents a vulnerability specific to applications, extending the general vulnerability model.")
public class VulnerabilidadApp extends Vulnerabilidad {

	@Schema(description = "The version range of the application that is not included in the vulnerability (exclusive).")
	private String versionEndExcluding;

	@Schema(description = "The version range of the application that is included in the vulnerability (inclusive).")
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
