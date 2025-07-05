package br.com.coletaverde.domain.employee.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EmployeeBasicDTO {
    private UUID id;
    private String nome;
    private String registration;
}