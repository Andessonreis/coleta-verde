package br.com.coletaverde.domain.appointment.service;

import java.util.List;
import java.util.UUID;

import br.com.coletaverde.domain.appointment.dto.AppointmentPostRequestDTO;
import br.com.coletaverde.domain.appointment.dto.AppointmentResponseDTO;
import br.com.coletaverde.domain.appointment.dto.AvailabilityResponse;
import br.com.coletaverde.domain.appointment.entities.Appointment;
import br.com.coletaverde.domain.appointment.enums.AppointmentStatus;

public interface IAppointmentService {
    AppointmentResponseDTO createAppointment(AppointmentPostRequestDTO dto, String userEmail);
    AppointmentResponseDTO updateAppointment(UUID id, AppointmentPostRequestDTO dto, String userEmail);
    List<AppointmentResponseDTO> getAllAppointments();
    AppointmentResponseDTO getAppointmentById(UUID id);
    AppointmentResponseDTO assignAppointment(UUID appointmentId, UUID employeeId);
    List<Appointment> getAppointmentsByEmployeeEmail(String email);
    AppointmentResponseDTO updateAppointmentStatus(UUID appointmentId, AppointmentStatus newStatus, String observacoes, String userEmail);
    List<AppointmentResponseDTO> getAppointmentsByCitizenEmail(String email);
    AvailabilityResponse getAvailability();
}
