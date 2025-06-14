package br.com.coletaverde.controllers.v1.employee;

import br.com.coletaverde.domain.employee.dto.EmployeeCreateDto;
import br.com.coletaverde.domain.employee.dto.EmployeeResponseDto;
import br.com.coletaverde.domain.employee.dto.mapper.EmployeeMapper;
import br.com.coletaverde.domain.employee.entities.Employee;
import br.com.coletaverde.domain.employee.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
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


    @GetMapping(value = "/me", produces = "application/json")
    public ResponseEntity<Object> getCurrentEmployee(Authentication authentication) {
        try {
            Object principal = authentication.getPrincipal();
            String email;

            if (principal instanceof org.springframework.security.core.userdetails.UserDetails) {
                email = ((org.springframework.security.core.userdetails.UserDetails) principal).getUsername();

            } else if (principal instanceof String) {
        
                email = (String) principal;
            } else {
         
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não autenticado corretamente.");
            }

            System.out.println("Email extraído: " + email);

            var employee = employeeService.getEmployeeByEmail(email);
            System.out.println("var: " + employee);
            if (employee == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cidadão não encontrado.");
            }
            return ResponseEntity.ok(employee);

        } catch (Exception ex) {
            log.error("Erro ao buscar cidadão autenticado", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno ao buscar o cidadão.");
        }
    }
}
