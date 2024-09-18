package es.unican.CIBEL.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import es.unican.CIBEL.domain.Usuario;
import es.unican.CIBEL.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Guardar o actualizar un usuario
    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    // Verificar si la contraseña proporcionada es correcta
    public boolean checkPassword(Usuario usuario, String rawPassword) {
        return passwordEncoder.matches(rawPassword, usuario.getPassword());
    }

    // Encriptar la nueva contraseña antes de guardarla
    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    // Eliminar un usuario
    public void delete(Usuario usuario) {
        usuario.getActivos().clear();
        usuarioRepository.save(usuario);

        usuarioRepository.delete(usuario);
    }
}
