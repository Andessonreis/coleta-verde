package br.com.coletaverde.domain.citizen.dto;

import br.com.coletaverde.domain.address.dto.AddressDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CitizenPostRequestDTO {

        @JsonProperty("email")
        @NotNull(message = "Email is mandatory.")
        @NotBlank(message = "Email cannot be blank.")
        @Email(message = "Email should be valid.")
        @Size(max = 150, message = "Email must be at most 150 characters.")
        private String email;

        @JsonProperty("password")
        @NotNull(message = "Password is mandatory.")
        @NotBlank(message = "Password cannot be blank.")
        private String password;

        @JsonProperty("username")
        @NotNull(message = "Username is mandatory.")
        @NotBlank(message = "Username cannot be blank.")
        @Size(min = 4, max = 100)
        private String username;

        @JsonProperty("phone")
        @NotNull(message = "Phone is mandatory.")
        @NotBlank(message = "Phone cannot be blank.")
        @Size(max = 20)
        private String phone;

        @JsonProperty("address")
        @NotNull(message = "Address is mandatory.")
        private AddressDTO address;
}
