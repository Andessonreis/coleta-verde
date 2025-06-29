package br.com.coletaverde.domain.penalty.dto;

import br.com.coletaverde.domain.penalty.enums.PenaltyStatus;
import br.com.coletaverde.domain.penalty.enums.PenaltyType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PenaltyResponseDTO {
    private UUID id;
    private UUID appointmentId;
    private UUID citizenId;
    private UUID employeeId;
    private PenaltyType type;
    private String description;
    private String evidencePhotoUrl;
    private Integer blockDays;
    private LocalDateTime reportedAt;
    private PenaltyStatus status;
}
