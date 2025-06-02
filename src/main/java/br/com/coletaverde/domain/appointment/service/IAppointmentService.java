package br.com.coletaverde.domain.appointment.service;

import java.util.List;
import java.util.UUID;

import br.com.coletaverde.domain.appointment.dto.AppointmentPostRequestDTO;
import br.com.coletaverde.domain.appointment.dto.AppointmentResponseDTO;

public interface IAppointmentService {
    AppointmentResponseDTO createAppointment(AppointmentPostRequestDTO dto, String userEmail);

    List<AppointmentResponseDTO> getAllAppointments();
    
    AppointmentResponseDTO getAppointmentById(UUID id);

}
