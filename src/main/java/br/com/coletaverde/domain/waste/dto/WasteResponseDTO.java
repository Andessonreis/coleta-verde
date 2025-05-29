package br.com.coletaverde.domain.waste.dto;

import br.com.coletaverde.domain.waste.enums.WasteType;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WasteResponseDTO {

    @JsonProperty("type")
    private WasteType type;

    @NotNull
    @JsonProperty("description")
    private String description;

}
