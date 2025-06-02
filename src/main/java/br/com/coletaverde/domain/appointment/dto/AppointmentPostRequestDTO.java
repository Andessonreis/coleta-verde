package br.com.coletaverde.domain.appointment.dto;

import br.com.coletaverde.domain.waste.dto.WasteDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentPostRequestDTO {

    @JsonProperty("scheduled_at")
    @NotNull(message = "Scheduled date is required")
    @Future(message = "Scheduled date must be in the future")
    private LocalDateTime scheduledAt;

    @JsonProperty("optional_photo_url")
    @Size(max = 255, message = "Photo URL must be at most 255 characters")
    private String optionalPhotoUrl;

    @JsonProperty("waste")
    @NotNull(message = "Waste information is required")
    private WasteDTO waste;
}
