package sodresoftwares.government.api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import sodresoftwares.government.api.infra.security.TokenService;
import sodresoftwares.government.api.model.user.AuthenticationDTO;
import sodresoftwares.government.api.model.user.LoginResponseDTO;
import sodresoftwares.government.api.model.user.RegisterDTO;
import sodresoftwares.government.api.model.user.User;
import sodresoftwares.government.api.repositories.UserRepository;
import sodresoftwares.government.api.services.AuthService;


@RestController
@RequestMapping("auth")
public class AuthenticationController {

	private final AuthService authService;

	public AuthenticationController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AuthenticationDTO data) {
		LoginResponseDTO response = authService.login(data);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/register")
	public ResponseEntity<Void> register(@RequestBody @Valid RegisterDTO data) {
		authService.register(data);
		return ResponseEntity.ok().build();
	}
}
