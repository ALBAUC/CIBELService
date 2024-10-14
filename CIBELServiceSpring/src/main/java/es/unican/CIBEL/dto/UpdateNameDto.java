package es.unican.CIBEL.dto;

import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Data transfer object for updating a user's name.")
public class UpdateNameDto {
	
	@NotBlank(message = "New name is mandatory")
	@Schema(description = "The new name for the user, which is mandatory and cannot be blank.")
	private String newName;

	public String getNewName() {
		return newName;
	}

	public void setNewName(String newName) {
		this.newName = newName;
	}
}
