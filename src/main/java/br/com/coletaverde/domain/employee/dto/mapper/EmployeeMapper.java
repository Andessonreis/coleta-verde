package br.com.coletaverde.domain.employee.dto.mapper;

import br.com.coletaverde.domain.employee.dto.EmployeeCreateDto;
import br.com.coletaverde.domain.employee.dto.EmployeeResponseDto;
import br.com.coletaverde.domain.employee.entities.Employee;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import java.util.List;
import java.util.stream.Collectors;

public class EmployeeMapper {
    public static Employee toEmployee(EmployeeCreateDto createDto) {
        return new ModelMapper().map(createDto, Employee.class);
    }

    public static EmployeeResponseDto toDto(Employee employee) {
        String role = employee.getRole().name();
        PropertyMap<Employee, EmployeeResponseDto> props = new PropertyMap<Employee, EmployeeResponseDto>() {
            @Override
            protected void configure() {
                map().setRole(role);
            }
        };
        ModelMapper mapper = new ModelMapper();
        mapper.addMappings(props);
        return mapper.map(employee, EmployeeResponseDto.class);
    }

    public static List<EmployeeResponseDto> toListDto(List<Employee> employees) {
        return employees.stream().map(employee -> toDto(employee)).collect(Collectors.toList());
    }
}
