package br.com.coletaverde.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class for Web Security.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig implements WebMvcConfigurer {

    @Autowired
    private JWTLoginFilter jwtLoginFilter;

    /**
     * Configures the security filter chain with customizations.
     *
     * @param http the HttpSecurity object to configure
     * @return the SecurityFilterChain configured
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize

                // TODO: Revisar as permissões abaixo. Estão liberadas temporariamente para desenvolvimento/testes.
                //       Avaliar quais endpoints realmente devem ser públicos e aplicar regras de acesso corretas
                //       com base em papéis (roles), autenticação e lógica de negócios.

                        // CORS e endpoints públicos genéricos
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // Pré-voo CORS

                        // Autenticação
                        .requestMatchers(HttpMethod.POST, "/api/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/register").permitAll()

                        // Funcionalidades de funcionários
                        .requestMatchers(HttpMethod.POST, "/api/employees/employee").permitAll()
                        .requestMatchers(HttpMethod.GET,  "/api/employees").permitAll()
                        .requestMatchers(HttpMethod.GET,  "/api/employees/**").permitAll()

                        // Notificações
                        .requestMatchers(HttpMethod.POST, "/api/notifications/notification").permitAll()
                        .requestMatchers(HttpMethod.GET,  "/api/notifications").permitAll()
                        .requestMatchers(HttpMethod.GET,  "/api/notifications/**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/appointments").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/appointments/**").permitAll()

                        .requestMatchers("/supabase/**").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()

                        .anyRequest().authenticated()
                )
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()))
                .addFilterBefore(jwtLoginFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Configures the password encoder used for encoding passwords.
     *
     * @return the PasswordEncoder instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures the authentication manager.
     *
     * @param authenticationConfiguration the AuthenticationConfiguration object
     * @return the AuthenticationManager instance
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
