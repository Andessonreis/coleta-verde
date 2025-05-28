package br.com.coletaverde.domain.citizen.service;

import br.com.coletaverde.domain.citizen.dto.CitizenSimpleResponseDTO;
import br.com.coletaverde.domain.citizen.entities.Citizen;

public interface ICitizenService {
    CitizenSimpleResponseDTO saveUser(Citizen citizen);

}
