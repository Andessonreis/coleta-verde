package br.com.coletaverde.domain.employee.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EmployeeResponseDto {
    private UUID id;
    private String name;
    private String email;
    private String registration;
    private String jobTitle;
    private String role;
}
