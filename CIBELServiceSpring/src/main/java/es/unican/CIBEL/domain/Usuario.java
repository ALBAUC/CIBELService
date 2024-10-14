package es.unican.CIBEL.domain;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import io.swagger.v3.oas.annotations.media.Schema;

@SuppressWarnings("serial")
@Entity
@Schema(description = "Represents a user in the system.")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier for the user.")
    private Long id;
    
    @Column(nullable = false)
    @Schema(description = "Name of the user.")
    private String name;

    @Column(nullable = false)
    @Schema(description = "Password of the user.")
    private String password;
    
    @Column(unique = true, nullable = false)
    @Schema(description = "Email address of the user.")
    private String email;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "usuario_x_activo",
    			joinColumns = @JoinColumn(name = "fk_usuario"),
    			inverseJoinColumns = @JoinColumn(name = "fk_activo"))
    @Schema(description = "List of assets associated with the user.")
    private List<Activo> activos;

    public Usuario() {}

    public Usuario(String name, String email, String password) {
    	this.name = name;
        this.email = email;
        this.password = password;
        this.activos = new LinkedList<Activo>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

	public List<Activo> getActivos() {
		return activos;
	}

	public void setActivos(List<Activo> activos) {
		this.activos = activos;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String getUsername() {
		return email;
	}
}
