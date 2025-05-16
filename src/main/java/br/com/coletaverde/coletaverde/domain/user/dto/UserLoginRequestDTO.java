package br.com.coletaverde.coletaverde.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserLoginRequestDTO(
        @JsonProperty(value = "email")
        @NotNull(message = "Email is mandatory.")
        @NotBlank(message = "Email cannot be blank.")
        @Email(message = "Email should be valid.")
        @Size(max = 150, message = "Email must be at most 150 characters.")
        String email,

        @JsonProperty(value = "password")
        @NotNull(message = "Password is mandatory.")
        @NotBlank(message = "Password cannot be blank.")
        String password
) {}
