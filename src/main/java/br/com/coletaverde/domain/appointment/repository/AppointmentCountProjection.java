package br.com.coletaverde.domain.appointment.repository;

import java.time.LocalDate;

// Interface para mapear o resultado da nossa query customizada
public interface AppointmentCountProjection {
    LocalDate getAppointmentDate();
    Long getTotal();
}