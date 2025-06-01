package br.com.coletaverde.domain.employee.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeCreateDto {

    @NotNull(message = "Email is mandatory.")
    @NotBlank(message = "Email cannot be blank.")
    @Email(message = "Email should be valid.")
    @Size(max = 150, message = "Email must be at most 150 characters.")
    private String email;

    @NotNull(message = "Password is mandatory.")
    @NotBlank(message = "Password cannot be blank.")
    private String password;

    @NotNull(message = "Username is mandatory.")
    @NotBlank(message = "Username cannot be blank.")
    @Size(min = 4, max = 100)
    private String username;

    @NotNull(message = "Registration is mandatory.")
    @NotBlank(message = "Registration cannot be blank.")
    private String registration; // matrícula

    @NotNull(message = "Job title is mandatory.")
    @NotBlank(message = "Job title cannot be blank.")
    private String jobTitle; // cargo
}

