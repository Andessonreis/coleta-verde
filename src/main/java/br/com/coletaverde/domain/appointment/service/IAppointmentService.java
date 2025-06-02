package br.com.coletaverde.domain.appointment.service;

import br.com.coletaverde.domain.appointment.dto.AppointmentPostRequestDTO;
import br.com.coletaverde.domain.appointment.dto.AppointmentResponseDTO;

public interface IAppointmentService {
    AppointmentResponseDTO createAppointment(AppointmentPostRequestDTO dto, String userEmail);
}
