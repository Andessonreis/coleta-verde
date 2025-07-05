package br.com.coletaverde.domain.citizen.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CitizenBasicDTO {
    private UUID id;
    private String name;
    private String email;
}