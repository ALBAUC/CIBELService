package es.unican.CIBEL.controller;

import es.unican.CIBEL.domain.Usuario;
import es.unican.CIBEL.service.AuthenticationService;
import es.unican.CIBEL.util.JwtUtil;
import es.unican.CIBEL.util.LoginResponse;
import es.unican.CIBEL.dto.LoginUserDto;
import es.unican.CIBEL.dto.RegisterUserDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<Usuario> register(@RequestBody RegisterUserDto registerUserDto) {
        Usuario registeredUser = authenticationService.register(registerUserDto);

        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) {
        Usuario authenticatedUser = authenticationService.authenticate(loginUserDto);

        String jwtToken = jwtUtil.generateToken(authenticatedUser);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtUtil.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }
}
