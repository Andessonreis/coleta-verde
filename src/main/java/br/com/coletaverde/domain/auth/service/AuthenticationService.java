package br.com.coletaverde.domain.auth.service;

import br.com.coletaverde.domain.employee.entities.Employee;
import br.com.coletaverde.domain.penalty.entities.Penalty;
import br.com.coletaverde.domain.penalty.enums.PenaltyStatus;
import br.com.coletaverde.domain.penalty.repository.PenaltyRepository;
import br.com.coletaverde.domain.user.dto.UserLoginRequestDTO;
import br.com.coletaverde.domain.user.dto.UserLoginResponseDTO;
import br.com.coletaverde.domain.user.entities.User;
import br.com.coletaverde.domain.user.enums.UserStatus;
import br.com.coletaverde.domain.user.repository.UserRepository;
import br.com.coletaverde.infrastructure.exceptions.UserSuspendedException;
import br.com.coletaverde.infrastructure.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable; 
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PenaltyRepository penaltyRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @Transactional
    public UserLoginResponseDTO login(UserLoginRequestDTO request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new BadCredentialsException("Email ou senha inválidos."));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new BadCredentialsException("Email ou senha inválidos.");
        }

        if (user.getStatus() == UserStatus.SUSPENDED) {
            // Sem cast, pois PageRequest IMPLEMENTA Pageable
            Pageable limitToOne = PageRequest.of(0, 1);
            List<Penalty> penalties = penaltyRepository.findLatestApprovedPenalty(user.getId(), PenaltyStatus.APPROVED, limitToOne);
            Optional<Penalty> latestPenaltyOpt = penalties.stream().findFirst();

            if (latestPenaltyOpt.isPresent()) {
                Penalty latestPenalty = latestPenaltyOpt.get();
                if (LocalDateTime.now().isBefore(latestPenalty.getBlockEndDate())) {
                    throw new UserSuspendedException("Acesso negado. Sua conta está suspensa até: " + latestPenalty.getBlockEndDate());
                } else {
                    user.setStatus(UserStatus.ACTIVE);
                }
            }
        }

        String token = tokenService.generateToken(user);
        String cargo = (user instanceof Employee employee) ? employee.getJobTitle() : null;

        return new UserLoginResponseDTO(user.getEmail(), token, user.getRole(), cargo, user.getStatus());
    }
}