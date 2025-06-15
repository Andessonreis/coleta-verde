package br.com.coletaverde.domain.appointment.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class AppointmentAssignRequestDTO {
    @NotNull(message = "O ID do agendamento é obrigatório")
    private UUID appointmentId;

    @NotNull(message = "O ID do funcionário é obrigatório")
    private UUID employeeId;
}
