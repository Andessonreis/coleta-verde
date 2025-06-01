package br.com.coletaverde.domain.employee.entities;

import br.com.coletaverde.domain.user.entities.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Employee extends User implements Serializable {

    @NotNull(message = "Registration is mandatory.")
    @NotBlank(message = "Registration cannot be blank.")
    @Column(name = "registration", unique = true, nullable = false)
    private String registration;//matricula

    @NotNull(message = "Job title is mandatory.")
    @NotBlank(message = "Job title cannot be blank.")
    @Column(name = "job_title", nullable = false)
    private String jobTitle; //cargo
}
