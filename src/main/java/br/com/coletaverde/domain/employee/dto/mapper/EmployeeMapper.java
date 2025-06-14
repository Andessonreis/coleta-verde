package br.com.coletaverde.domain.employee.dto.mapper;

import br.com.coletaverde.domain.employee.dto.EmployeeCreateDto;
import br.com.coletaverde.domain.employee.dto.EmployeeResponseDto;
import br.com.coletaverde.domain.employee.entities.Employee;
import br.com.coletaverde.domain.user.enums.Role;
import br.com.coletaverde.domain.user.enums.UserStatus;
import java.util.List;
import java.util.stream.Collectors;

public class EmployeeMapper {

    // Cria um Employee a partir do DTO de criação
    public static Employee toEmployee(EmployeeCreateDto dto) {
        Employee employee = new Employee();
        employee.setEmail(dto.getEmail());
        employee.setPassword(dto.getPassword());
        employee.setName(dto.getUsername());
        employee.setRegistration(dto.getRegistration());
        employee.setJobTitle(dto.getJobTitle());

        //campos padrões
        employee.setRole(Role.EMPLOYEE);
        employee.setStatus(UserStatus.ACTIVE);

        return employee;
    }

    // Converte Employee para ResponseDto
    public static EmployeeResponseDto toDto(Employee employee) {
        EmployeeResponseDto dto = new EmployeeResponseDto();
        dto.setId(employee.getId());
        dto.setName(employee.getUsername());
        dto.setEmail(employee.getEmail());
        dto.setRegistration(employee.getRegistration());
        dto.setJobTitle(employee.getJobTitle());
        dto.setRole(employee.getRole().name());
        dto.setStatus(employee.getStatus().name());
        return dto;
    }

    // Lista de DTOs
    public static List<EmployeeResponseDto> toListDto(List<Employee> employees) {
        return employees.stream().map(EmployeeMapper::toDto).collect(Collectors.toList());
    }
}
