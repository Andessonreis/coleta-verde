package br.com.coletaverde.domain.appointment.repository;

import br.com.coletaverde.domain.appointment.entities.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {}
