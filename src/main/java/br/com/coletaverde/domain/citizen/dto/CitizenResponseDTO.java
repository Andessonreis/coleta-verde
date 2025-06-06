package br.com.coletaverde.domain.citizen.dto;

import br.com.coletaverde.domain.address.dto.AddressDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CitizenResponseDTO {

    @JsonProperty("username")
    private String username;

    @JsonProperty("email")
    private String email;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("status")
    private String status;

    @JsonProperty("address")
    private AddressDTO address;
}