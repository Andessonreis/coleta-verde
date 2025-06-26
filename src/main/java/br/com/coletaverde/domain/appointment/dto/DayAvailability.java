package br.com.coletaverde.domain.appointment.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DayAvailability {
    private String date; 
    private boolean available;
    private long currentAppointments;
    private int maxAppointments;
    private String reason;
}