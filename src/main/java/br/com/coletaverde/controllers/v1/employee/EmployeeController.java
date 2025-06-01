package br.com.coletaverde.controllers.v1.employee;

import br.com.coletaverde.domain.employee.dto.EmployeeCreateDto;
import br.com.coletaverde.domain.employee.dto.EmployeeResponseDto;
import br.com.coletaverde.domain.employee.dto.mapper.EmployeeMapper;
import br.com.coletaverde.domain.employee.entities.Employee;
import br.com.coletaverde.domain.employee.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    @PostMapping(path = "/employee", consumes = "application/json", produces = "application/json")
    public ResponseEntity<EmployeeResponseDto> save (@Valid @RequestBody EmployeeCreateDto obj) {
        System.out.println("DTO EMPLOYEE: " + obj.toString());

        Employee employee = employeeService.save(EmployeeMapper.toEmployee(obj));

        return ResponseEntity.status(HttpStatus.CREATED).body(EmployeeMapper.toDto(employee));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponseDto> findById(@PathVariable UUID id) {
        Employee employee = employeeService.findById(id);
        return ResponseEntity.ok().body(EmployeeMapper.toDto(employee));
    }

    @GetMapping
    public ResponseEntity<List<EmployeeResponseDto>> findAll() {
        List<Employee> employees = employeeService.findAll();
        return ResponseEntity.ok(EmployeeMapper.toListDto(employees));
    }
}
