package br.com.coletaverde.domain.employee.service;

import br.com.coletaverde.domain.employee.entities.Employee;
import br.com.coletaverde.domain.employee.repository.EmployeeRepository;
import br.com.coletaverde.domain.user.enums.Role;
import br.com.coletaverde.domain.user.enums.UserStatus;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    @Transactional
    public Employee save (Employee obj) {
        try {
            obj.setStatus(UserStatus.ACTIVE);
            obj.setRole(Role.EMPLOYEE);
            return employeeRepository.save(obj);
        } catch (Exception e) {
            //TODO : tratar exeção
            throw new RuntimeException(e);
        }
    }

    @Transactional(readOnly = true)
    public Employee findById(UUID id) {
        return employeeRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Employee with id %s not found", id))
        );
    }

    @Transactional(readOnly = true)
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }
}
