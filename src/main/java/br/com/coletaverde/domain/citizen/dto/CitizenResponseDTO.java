package br.com.coletaverde.domain.citizen.dto;

import br.com.coletaverde.domain.address.dto.AddressDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CitizenResponseDTO {
    @JsonProperty("id")
    private UUID id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("email")
    private String email;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("status")
    private String status;

    @JsonProperty("address")
    private AddressDTO address;
}