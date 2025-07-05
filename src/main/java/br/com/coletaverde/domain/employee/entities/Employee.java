package br.com.coletaverde.domain.employee.entities;

import br.com.coletaverde.domain.penalty.entities.Penalty;
import br.com.coletaverde.domain.user.entities.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

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

    @OneToMany(mappedBy = "employee")
    private List<Penalty> penaltiesCreated;

    @OneToMany(mappedBy = "analyst")
    private List<Penalty> penaltiesAnalyzed;
}
