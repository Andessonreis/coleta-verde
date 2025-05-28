package br.com.coletaverde.domain.address.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AddressDTO(
        @NotBlank(message = "Public place is mandatory.")
        @Size(max = 150)
        String publicPlace,

        @NotBlank(message = "Street is mandatory.")
        @Size(max = 150)
        String street,

        @NotBlank(message = "Number is mandatory.")
        @Size(max = 20)
        String number,

        @Size(max = 100)
        String complement,

        @NotBlank(message = "City is mandatory.")
        @Size(max = 100)
        String city,

        @NotBlank(message = "UF is mandatory.")
        @Size(min = 2, max = 2)
        String uf,

        @NotBlank(message = "Zip code is mandatory.")
        @Size(max = 10)
        String zipCode
) {}
