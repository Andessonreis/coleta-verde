package br.com.coletaverde.domain.penalty.dto;

import br.com.coletaverde.domain.penalty.enums.PenaltyType;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Data
public class PenaltyPostRequestDTO {

    @NotNull(message = "appointmentId is required")
    private UUID appointmentId;

    @NotNull(message = "citizenId is required")
    private UUID citizenId;

    @NotNull(message = "employeeId is required")
    private UUID employeeId;

    @NotNull(message = "type is required")
    private PenaltyType type;

    @NotBlank(message = "description is required")
    private String description;

    @NotBlank(message = "evidencePhotoUrl is required")
    private String evidencePhotoUrl;

    @NotNull(message = "blockDays is required")
    @Min(value = 1, message = "blockDays must be at least 1")
    private Integer blockDays;
}
