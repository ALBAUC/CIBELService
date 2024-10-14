package es.unican.CIBEL.dto;

import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Data transfer object for user login credentials.")
public class LoginUserDto {
	
	@NotBlank(message = "Email is mandatory")
    @Schema(description = "The email address of the user, which is required for login.")
    private String email;

    @NotBlank(message = "Password is mandatory")
    @Schema(description = "The password of the user, which is required for login.")
    private String password;
    
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	} 
}
