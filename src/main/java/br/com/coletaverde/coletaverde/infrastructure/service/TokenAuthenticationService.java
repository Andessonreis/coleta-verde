package br.com.coletaverde.coletaverde.infrastructure.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class TokenAuthenticationService {

    @Value("${api.security.token.secret}")
    private String secretKey;

    private final SupabaseUserService supabaseUserService;

    public TokenAuthenticationService(SupabaseUserService supabaseUserService) {
        this.supabaseUserService = supabaseUserService;
    }

    public Authentication getAuthentication(HttpServletRequest request, HttpServletResponse response) {
        String token = getTokenFromRequest(request);

        if (token != null) {
            String username = validateToken(token);

            if (username != null) {
                var userJson = supabaseUserService.findUserByEmail(username);
                if (userJson != null) {
                    User user = new User(username, "", new ArrayList<>());
                    return new UsernamePasswordAuthenticationToken(
                            user.getUsername(),
                            null,
                            user.getAuthorities()
                    );
                } else {
                    throw new UsernameNotFoundException("User not found in Supabase!");
                }
            }
        }

        allowCors(response);
        return null;
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

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
                ACCESS_CONTROL_REQUEST_HEADERS
        ));

        headers.forEach(h -> {
            if (response.getHeader(h) == null)
                response.setHeader(h, "*");
        });
    }
}
