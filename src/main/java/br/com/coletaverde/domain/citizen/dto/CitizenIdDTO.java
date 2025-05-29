package br.com.coletaverde.domain.citizen.dto;

import java.util.UUID;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CitizenIdDTO {
  
  @NotNull
  private UUID id;
}

