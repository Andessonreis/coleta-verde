package br.com.coletaverde.infrastructure.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;

import br.com.coletaverde.domain.user.entities.User;
import br.com.coletaverde.domain.user.repository.UserRepository;
import br.com.coletaverde.infrastructure.exceptions.BusinessException;
import br.com.coletaverde.infrastructure.exceptions.BusinessExceptionMessage;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Service class for JWT token authentication.
 */
@Service
public class TokenAuthenticationService {

    @Value("${api.security.token.secret}")
    private String secretKey;

    @Autowired
    private UserRepository userRepository;

    /**
     * Retrieves the authentication object based on the JWT token present in the
     * request.
     *
     * @param request  the HTTP servlet request
     * @param response the HTTP servlet response
     * @return the Authentication object if the token is valid and user is found;
     *         null otherwise
     */
    public Authentication getAuthentication(HttpServletRequest request, HttpServletResponse response) {
        String token = getTokenFromRequest(request);

        if (token != null) {
            String username = validateToken(token);

            if (username != null) {
                UserDetails userDetails = loadUserByUsername(username);
                if (userDetails != null) {
                    return new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities());
                } else {
                    throw new UsernameNotFoundException("User not found!");
                }
            }
        }

        allowCors(response);
        return null;
    }

    /**
     * Loads the user by email (username) for authentication.
     * Throws UsernameNotFoundException if user not found, para compatibilidade com Spring Security.
     *
     * @param email the user's email
     * @return UserDetails object
     */
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    /**
     * Retrieves the JWT token from the Authorization header of the request.
     *
     * @param request the HTTP servlet request
     * @return the JWT token extracted from the Authorization header, or null if not
     *         present or invalid
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    /**
     * Validates the JWT token using the configured secret key.
     *
     * @param token the JWT token to validate
     * @return the username extracted from the token if valid; null otherwise
     */
    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            return JWT.require(algorithm)
                    .withIssuer("login-auth-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            return null;
        }
    }

    /**
     * Finds a user in the database based on the given email.
     * Esse método é para lógica de negócio, usa BusinessException.
     *
     * @param email the email of the user to find
     * @return the User object if found
     * @throws BusinessException if no user with the given email is found
     */
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new BusinessException(
                BusinessExceptionMessage.USER_NOT_FOUND_BY_EMAIL.format(email)));
    }

    /**
     * Allows Cross-Origin Resource Sharing (CORS) for the HTTP servlet response.
     *
     * @param response the HTTP servlet response to allow CORS for
     */
    private void allowCors(HttpServletResponse response) {
        final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
        final String ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";
        final String ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";
        final String ACCESS_CONTROL_REQUEST_HEADERS = "Access-Control-Request-Headers";

        List<String> headers = new ArrayList<>(List.of(
                ACCESS_CONTROL_ALLOW_ORIGIN,
                ACCESS_CONTROL_ALLOW_HEADERS,
                ACCESS_CONTROL_ALLOW_METHODS,
                ACCESS_CONTROL_REQUEST_HEADERS));

        headers.forEach(h -> {
            if (response.getHeader(h) == null)
                response.setHeader(h, "*");
        });
    }
}
