package es.unican.CIBEL.dto;

import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Data transfer object for updating a user's password.")
public class UpdatePasswordDto {
	
	@NotBlank(message = "Old password is mandatory")
	@Schema(description = "The old password of the user, which is mandatory and cannot be blank.")
    private String oldPassword;
	
	@NotBlank(message = "New password is mandatory")
	@Schema(description = "The new password for the user, which is mandatory and cannot be blank.")
    private String newPassword;
    
	public String getOldPassword() {
		return oldPassword;
	}
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
}
