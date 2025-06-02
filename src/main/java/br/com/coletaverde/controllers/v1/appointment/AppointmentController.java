package br.com.coletaverde.controllers.v1.appointment;

import br.com.coletaverde.controllers.util.ResultErrorUtil;
import br.com.coletaverde.domain.appointment.dto.AppointmentPostRequestDTO;
import br.com.coletaverde.domain.appointment.service.IAppointmentService;
import br.com.coletaverde.infrastructure.security.AuthenticatedUserProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
}
