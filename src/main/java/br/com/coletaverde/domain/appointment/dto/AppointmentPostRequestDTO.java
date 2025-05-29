package br.com.coletaverde.domain.appointment.dto;

import br.com.coletaverde.domain.address.dto.AddressDTO;
import br.com.coletaverde.domain.citizen.dto.CitizenIdDTO;
import br.com.coletaverde.domain.waste.dto.WasteIdDTO;
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
    @NotNull
    @Future(message = "Scheduled date must be in the future")
    private LocalDateTime scheduledAt;

    @JsonProperty("optional_photo_url")
    @Size(max = 255)
    private String optionalPhotoUrl;

    @JsonProperty("requester")
    @NotNull
    private CitizenIdDTO requester; 

    @JsonProperty("address")
    @NotNull(message = "Address is mandatory.")
    private AddressDTO address;

    @JsonProperty("wasteItem")
    @NotNull
    private WasteIdDTO waste;
}
