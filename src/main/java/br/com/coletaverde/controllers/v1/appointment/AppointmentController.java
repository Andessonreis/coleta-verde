package br.com.coletaverde.controllers.v1.appointment;

import br.com.coletaverde.controllers.util.ResultErrorUtil;
import br.com.coletaverde.domain.appointment.dto.AppointmentAssignRequestDTO;
import br.com.coletaverde.domain.appointment.dto.AppointmentPostRequestDTO;
import br.com.coletaverde.domain.appointment.dto.AppointmentStatusUpdateDTO;
import br.com.coletaverde.domain.appointment.enums.AppointmentStatus;
import br.com.coletaverde.domain.appointment.service.IAppointmentService;
import br.com.coletaverde.domain.user.enums.Role;
import br.com.coletaverde.infrastructure.exceptions.BusinessException;
import br.com.coletaverde.infrastructure.security.AuthenticatedUserProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    @Autowired
    private IAppointmentService appointmentService;
    @Autowired
    private AuthenticatedUserProvider authenticatedUserProvider;

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> createAppointment(
            @Valid @RequestBody AppointmentPostRequestDTO request,
            BindingResult bindingResult) {

        log.info("Received createAppointment request");

        if (bindingResult.hasErrors()) {
            var errors = ResultErrorUtil.getFieldErrors(bindingResult);
            log.warn("Validation failed: {}", errors);
            return ResponseEntity
                    .badRequest()
                    .body(errors);
        }

        var optionalUser = authenticatedUserProvider.getAuthenticatedUser();

        if (optionalUser.isEmpty()) {
            log.warn("Unauthorized access attempt to createAppointment");
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Usuário não autenticado");
        }

        var user = optionalUser.get();
        log.info("Authenticated user: {}", user.getEmail());

        try {
            var response = appointmentService.createAppointment(request, user.getEmail());
            log.info("Appointment created successfully for user {}", user.getEmail());

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(response);

        } catch (Exception ex) {
            log.error("Unexpected error while creating appointment", ex);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno inesperado. Tente novamente mais tarde.");
        }
    }

    @GetMapping(value = "/availability", produces = "application/json")
    public ResponseEntity<Object> getAvailability() {
        log.info("[GET] /api/appointments/availability - Buscando disponibilidade");

        try {
            var availabilityResponse = appointmentService.getAvailability();
            log.info("Disponibilidade de agendamentos retornada com sucesso");
            return ResponseEntity.ok(availabilityResponse);

        } catch (Exception ex) {
            log.error("Erro ao buscar disponibilidade de agendamentos", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno ao buscar a disponibilidade de agendamentos.");
        }
    }

    /**
     * Atualiza um agendamento existente
     */
    @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> updateAppointment(
            @PathVariable UUID id,
            @Valid @RequestBody AppointmentPostRequestDTO request,
            BindingResult bindingResult) {

        log.info("Received updateAppointment request for id {}", id);

        if (bindingResult.hasErrors()) {
            var errors = ResultErrorUtil.getFieldErrors(bindingResult);
            log.warn("Validation failed: {}", errors);
            return ResponseEntity
                    .badRequest()
                    .body(errors);
        }

        var optionalUser = authenticatedUserProvider.getAuthenticatedUser();

        if (optionalUser.isEmpty()) {
            log.warn("Unauthorized access attempt to updateAppointment");
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Usuário não autenticado");
        }

        var user = optionalUser.get();
        log.info("Authenticated user: {}", user.getEmail());

        try {
            var updatedAppointment = appointmentService.updateAppointment(id, request, user.getEmail());
            log.info("Appointment updated successfully for id {}", id);

            return ResponseEntity.ok(updatedAppointment);

        } catch (BusinessException ex) {
            log.warn("Business error while updating appointment: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());

        } catch (Exception ex) {
            log.error("Unexpected error while updating appointment with id {}", id, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno ao atualizar o agendamento.");
        }
    }

    /**
     * Retorna todos os agendamentos
     */
    @GetMapping(produces = "application/json")
    public ResponseEntity<Object> getAllAppointments() {
        log.info("Received getAllAppointments request");

        try {
            var appointments = appointmentService.getAllAppointments();
            return ResponseEntity.ok(appointments);
        } catch (Exception ex) {
            log.error("Error while fetching all appointments", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno ao buscar os agendamentos.");
        }
    }

    /**
     * Retorna um agendamento pelo ID
     */
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Object> getAppointmentById(@PathVariable UUID id) {
        log.info("Received getAppointmentById request with id {}", id);

        try {
            var appointment = appointmentService.getAppointmentById(id);
            return ResponseEntity.ok(appointment);
        } catch (BusinessException ex) {
            log.warn("Appointment not found: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            log.error("Error while fetching appointment by id {}", id, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno ao buscar o agendamento.");
        }
    }

    @PutMapping(value = "/assign", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> assignAppointmentToEmployee(
            @Valid @RequestBody AppointmentAssignRequestDTO request,
            BindingResult bindingResult
    ) {
        log.info("Iniciando requisição para atribuir agendamento a funcionário");

        if (bindingResult.hasErrors()) {
            var errors = ResultErrorUtil.getFieldErrors(bindingResult);
            log.warn("Validação falhou: {}", errors);
            return ResponseEntity.badRequest().body(errors);
        }

        var optionalUser = authenticatedUserProvider.getAuthenticatedUser();

        if (optionalUser.isEmpty()) {
            log.warn("Tentativa de acesso não autenticado");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não autenticado");
        }

        var user = optionalUser.get();
        log.info("Usuário autenticado: {}", user.getEmail());

        if (!user.getRole().equals(Role.ADMIN)) {
            log.warn("Usuário sem permissão tentou atribuir agendamento: {}", user.getEmail());
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Apenas administradores podem atribuir agendamentos");
        }

        try {
            var assignedAppointment = appointmentService.assignAppointment(
                    request.getAppointmentId(),
                    request.getEmployeeId()
            );

            log.info("Agendamento atribuído com sucesso: {}", assignedAppointment.getId());
            return ResponseEntity.ok(assignedAppointment);

        } catch (BusinessException ex) {
            log.warn("Erro de negócio ao atribuir agendamento: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());

        } catch (Exception ex) {
            log.error("Erro inesperado ao atribuir agendamento", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno ao atribuir o agendamento");
        }
    }

    /**
     * Retorna os agendamentos associados ao funcionário autenticado
     */
    @GetMapping(value = "/employee", produces = "application/json")
    public ResponseEntity<Object> getAppointmentsByAuthenticatedEmployee() {
        log.info("Iniciando requisição para buscar agendamentos do funcionário autenticado");

        var optionalUser = authenticatedUserProvider.getAuthenticatedUser();

        if (optionalUser.isEmpty()) {
            log.warn("Tentativa de acesso não autenticado");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não autenticado");
        }

        var user = optionalUser.get();

        if (!user.getRole().equals(Role.EMPLOYEE)) {
            log.warn("Usuário sem permissão tentou acessar agendamentos de funcionário: {}", user.getEmail());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Apenas funcionários podem acessar esta rota");
        }

        try {
            var appointments = appointmentService.getAppointmentsByEmployeeEmail(user.getEmail());
            log.info("Agendamentos recuperados com sucesso para {}", user.getEmail());
            return ResponseEntity.ok(appointments);

        } catch (BusinessException ex) {
            log.warn("Erro de negócio: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());

        } catch (Exception ex) {
            log.error("Erro inesperado ao buscar agendamentos", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno ao buscar os agendamentos do funcionário");
        }
    }

    @PutMapping(value = "/{id}/status", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> updateAppointmentStatus(
            @PathVariable UUID id,
            @Valid @RequestBody AppointmentStatusUpdateDTO request,
            BindingResult bindingResult
    ) {
        log.info("Recebida solicitação para atualizar status do agendamento com id {}", id);

        if (bindingResult.hasErrors()) {
            var errors = ResultErrorUtil.getFieldErrors(bindingResult);
            log.warn("Validação falhou: {}", errors);
            return ResponseEntity.badRequest().body(errors);
        }

        var optionalUser = authenticatedUserProvider.getAuthenticatedUser();

        if (optionalUser.isEmpty()) {
            log.warn("Tentativa de acesso não autenticado");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não autenticado");
        }

        var user = optionalUser.get();

        boolean isCitizen = user.getRole().equals(Role.CITIZEN);
        boolean isEmployee = user.getRole().equals(Role.EMPLOYEE);

        if (isCitizen) {
            if (!request.getStatus().equals(AppointmentStatus.CANCELED)) {
                log.warn("Cidadão tentou definir status inválido: {}", request.getStatus());
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Cidadãos só podem cancelar seus próprios agendamentos.");
            }

            var appointment = appointmentService.getAppointmentById(id);
            if (!appointment.getRequester().getEmail().equals(user.getEmail())) {
                log.warn("Cidadão tentou cancelar agendamento que não é seu.");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você só pode cancelar seus próprios agendamentos.");
            }
        } else if (!isEmployee) {
            log.warn("Usuário sem permissão tentou alterar status: {}", user.getEmail());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Apenas funcionários ou o próprio cidadão podem alterar o status.");
        }

        try {
            var updated = appointmentService.updateAppointmentStatus(id, request.getStatus(), request.getObservacoes(), user.getEmail());
            log.info("Status do agendamento {} atualizado com sucesso para {}", id, request.getStatus());
            return ResponseEntity.ok(updated);

        } catch (BusinessException ex) {
            log.warn("Erro de negócio: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());

        } catch (Exception ex) {
            log.error("Erro inesperado ao atualizar status do agendamento", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno ao atualizar o status do agendamento");
        }
    }

    @GetMapping(value = "/citizen", produces = "application/json")
    public ResponseEntity<Object> getAppointmentsByAuthenticatedCitizen() {
        log.info("Iniciando requisição para buscar agendamentos do cidadão autenticado");

        var optionalUser = authenticatedUserProvider.getAuthenticatedUser();

        if (optionalUser.isEmpty()) {
            log.warn("Tentativa de acesso não autenticado");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não autenticado");
        }

        var user = optionalUser.get();

        if (!user.getRole().equals(Role.CITIZEN)) {
            log.warn("Usuário sem permissão tentou acessar agendamentos de cidadão: {}", user.getEmail());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Apenas cidadãos podem acessar esta rota");
        }

        try {
            var appointments = appointmentService.getAppointmentsByCitizenEmail(user.getEmail());
            log.info("Agendamentos do cidadão {} recuperados com sucesso", user.getEmail());
            return ResponseEntity.ok(appointments);

        } catch (BusinessException ex) {
            log.warn("Erro de negócio: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());

        } catch (Exception ex) {
            log.error("Erro inesperado ao buscar agendamentos do cidadão", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno ao buscar os agendamentos do cidadão");
        }
    }

}
