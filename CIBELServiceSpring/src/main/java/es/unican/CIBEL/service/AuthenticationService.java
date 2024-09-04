package es.unican.CIBEL.service;

import es.unican.CIBEL.domain.Usuario;
import es.unican.CIBEL.dto.LoginUserDto;
import es.unican.CIBEL.dto.RegisterUserDto;
import es.unican.CIBEL.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private AuthenticationManager authenticationManager;

    public Usuario register(RegisterUserDto input) {
        if (usuarioRepository.findByEmail(input.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }
        
        String encodedPassword = passwordEncoder.encode(input.getPassword());
        Usuario usuario = new Usuario(input.getName(), input.getEmail(), encodedPassword);
        return usuarioRepository.save(usuario);
    }

    public Usuario authenticate(LoginUserDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        return usuarioRepository.findByEmail(input.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
