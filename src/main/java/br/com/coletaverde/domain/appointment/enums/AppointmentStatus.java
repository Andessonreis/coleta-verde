package br.com.coletaverde.domain.appointment.enums;

/**
 * Enum representing the possible statuses of an appointment lifecycle.
 */
public enum AppointmentStatus {
    /**
     * The appointment is scheduled and pending execution.
     */
    SCHEDULED,

    /**
     * The appointment was canceled before execution.
     */
    CANCELED,

    /**
     * The appointment was completed successfully.
     */
    COMPLETED,

    /**
     * The appointment was not completed (e.g., missed or rejected).
     */
    NOT_COMPLETED
}
