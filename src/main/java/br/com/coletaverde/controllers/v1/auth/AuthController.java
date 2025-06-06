package br.com.coletaverde.controllers.v1.auth;

import br.com.coletaverde.controllers.util.ResultErrorUtil;
import br.com.coletaverde.domain.citizen.entities.Citizen;
import br.com.coletaverde.domain.citizen.service.ICitizenService;
import br.com.coletaverde.domain.user.dto.UserLoginRequestDTO;
import br.com.coletaverde.domain.user.dto.UserLoginResponseDTO;
import br.com.coletaverde.domain.citizen.dto.CitizenCreateDTO;
import br.com.coletaverde.domain.user.entities.User;
import br.com.coletaverde.domain.user.repository.UserRepository;
import br.com.coletaverde.infrastructure.service.TokenService;
import br.com.coletaverde.infrastructure.util.ObjectMapperUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private ICitizenService citizenService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapperUtil objectMapperUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    @PostMapping(path = "/register", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> registerUser(@Valid @RequestBody CitizenCreateDTO citizenDTO, BindingResult result) {

        return result.hasErrors()
                ? ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResultErrorUtil.getFieldErrors(result))
                : ResponseEntity.status(HttpStatus.CREATED)
                        .body(citizenService.saveUser(objectMapperUtil.map(citizenDTO, Citizen.class)));
    }

    /**
     * Endpoint for user login.
     *
     * @param body the user login request body
     * @return ResponseEntity with user login response or error
     */
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserLoginRequestDTO body) {
        User user = userRepository.findByEmail(body.email())
                                  .orElseThrow(() -> new RuntimeException("User not found"));

        if (passwordEncoder.matches(body.password(), user.getPassword())) {
            String token = tokenService.generateToken(user);
            return ResponseEntity.ok(new UserLoginResponseDTO(user.getEmail(), token));
        }        

        return ResponseEntity.badRequest().build();
    }
    
}

