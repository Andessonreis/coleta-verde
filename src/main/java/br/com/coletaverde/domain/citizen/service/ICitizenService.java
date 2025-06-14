package br.com.coletaverde.domain.citizen.service;

import br.com.coletaverde.domain.citizen.dto.CitizenResponseDTO;
import br.com.coletaverde.domain.citizen.dto.CitizenSimpleResponseDTO;
import br.com.coletaverde.domain.citizen.entities.Citizen;

import java.util.List;
import java.util.UUID;

public interface ICitizenService {
    CitizenSimpleResponseDTO saveUser(Citizen citizen);

    List<CitizenResponseDTO> getAllCitizen();

    CitizenResponseDTO getCitizenById(UUID id);
    CitizenResponseDTO getCitizenByEmail(String email);

}
