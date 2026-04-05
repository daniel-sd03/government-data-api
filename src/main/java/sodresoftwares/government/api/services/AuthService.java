package sodresoftwares.government.api.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import sodresoftwares.government.api.exception.ApiException;
import sodresoftwares.government.api.infra.security.TokenService;
import sodresoftwares.government.api.model.user.AuthenticationDTO;
import sodresoftwares.government.api.model.user.LoginResponseDTO;
import sodresoftwares.government.api.model.user.RegisterDTO;
import sodresoftwares.government.api.model.user.User;
import sodresoftwares.government.api.repositories.UserRepository;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AuthenticationManager authenticationManager, UserRepository userRepository,
                       TokenService tokenService, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponseDTO login(AuthenticationDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((User) auth.getPrincipal());

        return new LoginResponseDTO(token);
    }

    public void register(RegisterDTO data) {
        if (this.userRepository.findByLogin(data.login()) != null) {
            throw new ApiException(400, "user already exists");
        }

        String encryptedPassword = passwordEncoder.encode(data.password());
        User newUser = new User(data.login(), encryptedPassword, data.role());

        this.userRepository.save(newUser);
    }
}