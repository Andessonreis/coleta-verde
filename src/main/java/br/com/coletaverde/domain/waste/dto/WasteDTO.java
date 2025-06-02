package br.com.coletaverde.domain.waste.dto;

import br.com.coletaverde.domain.waste.enums.WasteType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WasteDTO {

  @NotNull(message = "Waste type is required")
  private WasteType type;

  @NotBlank(message = "Description is required")
  @Size(max = 100, message = "Description must be at most 100 characters")
  private String description;
}

