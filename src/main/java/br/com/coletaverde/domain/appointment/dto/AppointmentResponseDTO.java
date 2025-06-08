package br.com.coletaverde.domain.appointment.dto;

import br.com.coletaverde.domain.appointment.enums.AppointmentStatus;
import br.com.coletaverde.domain.waste.dto.WasteResponseDTO;
import br.com.coletaverde.domain.waste.entities.Waste;
import br.com.coletaverde.domain.waste.enums.WasteType;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponseDTO {

    @JsonProperty("scheduled_at")
    private LocalDateTime scheduledAt;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    @JsonProperty("canceled_at")
    private LocalDateTime canceledAt;

    @JsonProperty("optional_photo_url")
    private String optionalPhotoUrl;

    @JsonProperty("status")
    private AppointmentStatus status;

    @JsonProperty("wasteItem")
    private WasteResponseDTO wasteItem;

}