package br.com.coletaverde.config.security;

import br.com.coletaverde.domain.user.entities.User;
import br.com.coletaverde.domain.user.repository.UserRepository;
import br.com.coletaverde.infrastructure.service.TokenAuthenticationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Filter responsible for authenticating users based on JWT tokens in the request header.
 */
@Component
public class JWTLoginFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JWTLoginFilter.class);
    private static final List<String> PUBLIC_ENDPOINTS = List.of("/api/register", "/api/login",
            "/api/employees", "/api/employees/employee",
            "/api/notifications" , "/api/notifications/notification");

    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final TokenAuthenticationService tokenAuthenticationService;
    private final UserRepository userRepository;

    public JWTLoginFilter(TokenAuthenticationService tokenAuthenticationService,
                          UserRepository userRepository) {
        this.tokenAuthenticationService = tokenAuthenticationService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        String path = request.getServletPath();

        if (isPublicEndpoint(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            Optional<String> tokenOpt = extractToken(request);

            if (tokenOpt.isPresent()) {
                String email = tokenAuthenticationService.validateToken(tokenOpt.get());

                if (email != null) {
                    User user = userRepository.findByEmail(email)
                            .orElseThrow(() -> new IllegalStateException("User not found for email: " + email));

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } else {
                    LOGGER.warn("Invalid JWT token provided for request to {}", path);
                }
            }
        } catch (Exception ex) {
            LOGGER.error("JWT authentication failed: {}", ex.getMessage(), ex);
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Checks whether the current request is to a public endpoint that doesn't require authentication.
     */
    private boolean isPublicEndpoint(String path) {
        return PUBLIC_ENDPOINTS.stream().anyMatch(publicPath ->
                publicPath.endsWith("/**") ? path.startsWith(publicPath.replace("/**", ""))
                        : path.equals(publicPath)
        );
    }

    /**
     * Extracts the JWT token from the Authorization header, if present.
     */
    private Optional<String> extractToken(HttpServletRequest request) {
        String header = request.getHeader(AUTH_HEADER);
        if (header != null && header.startsWith(BEARER_PREFIX)) {
            return Optional.of(header.substring(BEARER_PREFIX.length()));
        }
        return Optional.empty();
    }
}
