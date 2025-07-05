package br.com.coletaverde.domain.employee.service;

import br.com.coletaverde.domain.employee.entities.Employee;
import br.com.coletaverde.domain.employee.repository.EmployeeRepository;
import br.com.coletaverde.domain.user.enums.Role;
import br.com.coletaverde.domain.user.enums.UserStatus;
import br.com.coletaverde.domain.user.repository.UserRepository;
import br.com.coletaverde.infrastructure.exceptions.BusinessException;
import br.com.coletaverde.infrastructure.exceptions.BusinessExceptionMessage;
import br.com.coletaverde.infrastructure.util.ObjectMapperUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    private final UserRepository userRepository;

    private final ObjectMapperUtil objectMapperUtil;

    private final PasswordEncoder passwordEncoder;

  @Transactional
    public Employee save(Employee obj) {
        obj.setPassword(passwordEncoder.encode(obj.getPassword()));

        obj.setRole(Role.EMPLOYEE);
        obj.setStatus(UserStatus.ACTIVE);

        return Optional.of(obj)
                .filter(c -> c.getId() == null || !employeeRepository.existsById(c.getId()))
                .map(c -> employeeRepository.save(c))
                .map(saved -> objectMapperUtil.map(saved, Employee.class))
                .orElseThrow(() -> new BusinessException(
                        BusinessExceptionMessage.ATTRIBUTE_VALUE_ALREADY_EXISTS.format("Employee")));

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


    public Employee getEmployeeByEmail(String email) {
        Employee employee = (Employee) userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException("funcionario não encontrado para o email: " + email));
        System.out.println("getCitizeByEmailService: " + employee);
        System.out.println("Nome do cidadão: " + employee.getName());

        return objectMapperUtil.map(employee, Employee.class);
    }
}
