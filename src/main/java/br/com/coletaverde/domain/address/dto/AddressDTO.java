package br.com.coletaverde.domain.address.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {

        @NotBlank(message = "Public place is mandatory.")
        @Size(max = 150)
        private String publicPlace;

        @NotBlank(message = "Street is mandatory.")
        @Size(max = 150)
        private String street;

        @NotBlank(message = "Number is mandatory.")
        @Size(max = 20)
        private String number;

        @Size(max = 100)
        private String complement;

        @NotBlank(message = "City is mandatory.")
        @Size(max = 100)
        private String city;

        @NotBlank(message = "UF is mandatory.")
        @Size(min = 2, max = 2)
        private String uf;

        @NotBlank(message = "Zip code is mandatory.")
        @Size(max = 10)
        private String zipCode;
}
