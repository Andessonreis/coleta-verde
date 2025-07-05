package br.com.coletaverde.domain.user.dto;

import br.com.coletaverde.domain.user.enums.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginResponseDTO {
    @JsonProperty("email")
    private String email;

    @JsonProperty("token")
    private String token;
    @JsonProperty("role")
    private Role role;

    @JsonProperty("jobTitle")
    private String jobTitle;
}
