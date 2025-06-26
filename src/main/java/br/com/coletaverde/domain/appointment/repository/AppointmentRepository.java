package br.com.coletaverde.domain.appointment.repository;

import br.com.coletaverde.domain.appointment.entities.Appointment;
import br.com.coletaverde.domain.appointment.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

    @Query("SELECT a FROM Appointment a " +
        "JOIN FETCH a.requester r " +
        "JOIN FETCH a.wasteItem w " +
        "LEFT JOIN FETCH a.employee e " +
        "WHERE a.id = :id")
    Optional<Appointment> findByIdWithDetails(@Param("id") UUID id);

    @Query("SELECT a FROM Appointment a " +
            "LEFT JOIN FETCH a.requester " +
            "LEFT JOIN FETCH a.employee " +
            "LEFT JOIN FETCH a.wasteItem " +
            "LEFT JOIN FETCH a.address")
    List<Appointment> findAllWithDetails();

    @Query("SELECT a FROM Appointment a " +
            "LEFT JOIN FETCH a.requester " +
            "LEFT JOIN FETCH a.employee e " +
            "LEFT JOIN FETCH a.wasteItem " +
            "LEFT JOIN FETCH a.address " +
            "WHERE e.id = :employeeId")
    List<Appointment> findAllByEmployeeIdWithDetails(@Param("employeeId") UUID employeeId);

    @Query("SELECT a FROM Appointment a " +
            "JOIN FETCH a.address " +
            "JOIN FETCH a.wasteItem " +
            "LEFT JOIN FETCH a.employee " +
            "WHERE a.requester.id = :citizenId")
    List<Appointment> findAllByCitizenIdWithDetails(@Param("citizenId") UUID citizenId);


    /**
     * Conta os agendamentos ativos, agrupados por dia, dentro de um intervalo de tempo.
     * Usa FUNCTION('DATE', ...) para extrair a data de um campo LocalDateTime.
     * Filtra por status para não contar agendamentos cancelados.
     */
    @Query("SELECT FUNCTION('DATE', a.scheduledAt) as appointmentDate, COUNT(a.id) as total " +
            "FROM Appointment a " +
            "WHERE a.scheduledAt >= :startDateTime AND a.scheduledAt < :endDateTime " +
            "AND a.status IN :activeStatuses " +
            "GROUP BY FUNCTION('DATE', a.scheduledAt)")
    List<AppointmentCountProjection> countActiveAppointmentsByDateRange(
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime,
            @Param("activeStatuses") List<AppointmentStatus> activeStatuses
    );

}