package br.com.coletaverde.coletaverde.infrastructure.service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

import br.com.coletaverde.coletaverde.domain.user.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import jakarta.annotation.PostConstruct;

/**
 * Service class for generating JWT tokens for authenticated users.
 */
@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secretKey;

    @Value("${api.security.token.expiration-hours}")
    private Long expirationHours;

    @Value("${api.security.token.timezone}")
    private String timeZone;

    private Algorithm algorithm;

    @PostConstruct
    public void init() {
        this.algorithm = Algorithm.HMAC256(secretKey);
    }

    /**
     * Generates a JWT token for the authenticated user.
     *
     * @param user the authenticated user
     * @return the generated JWT token
     */
    public String generateToken(User user) {
        try {
            Date issuedAt = new Date();
            Date expirationDate = generateExpirationDate();

            return JWT.create()
                    .withSubject(user.getEmail())
                    .withIssuer("login-auth-api")
                    .withIssuedAt(issuedAt)
                    .withExpiresAt(expirationDate)
                    .sign(algorithm);

        } catch (Exception e) {
            throw new RuntimeException("Error generating JWT token", e);
        }
    }

    /**
     * Generates the expiration date for the JWT token.
     *
     * @return the expiration date
     */
    private Date generateExpirationDate() {
        return Date.from(LocalDateTime.now()
                .plusHours(expirationHours)
                .toInstant(ZoneOffset.of(timeZone)));
    }
}
