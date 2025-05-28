package br.com.coletaverde.domain.citizen.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CitizenSimpleResponseDTO {

    @JsonProperty("email")
    private String email;
}
