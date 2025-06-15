package br.com.coletaverde.domain.appointment.dto;

import br.com.coletaverde.domain.appointment.enums.AppointmentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AppointmentStatusUpdateDTO {

    @NotNull
    private AppointmentStatus status;

    private String observacoes;
}
