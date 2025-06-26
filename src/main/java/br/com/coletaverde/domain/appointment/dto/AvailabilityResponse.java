package br.com.coletaverde.domain.appointment.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AvailabilityResponse {
    private List<DayAvailability> availableDates;
    private int defaultMaxAppointments;
}