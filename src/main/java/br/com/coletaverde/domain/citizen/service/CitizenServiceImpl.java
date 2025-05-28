package br.com.coletaverde.domain.citizen.service;

import br.com.coletaverde.domain.citizen.dto.CitizenSimpleResponseDTO;
import br.com.coletaverde.domain.citizen.entities.Citizen;
import br.com.coletaverde.domain.citizen.repository.CitizenRepository;
import br.com.coletaverde.domain.user.enums.Role;
import br.com.coletaverde.domain.user.enums.UserStatus;
import br.com.coletaverde.infrastructure.exceptions.BusinessException;
import br.com.coletaverde.infrastructure.exceptions.BusinessExceptionMessage;
import br.com.coletaverde.infrastructure.util.ObjectMapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CitizenServiceImpl implements ICitizenService {

    @Autowired
    private CitizenRepository citizenRepository;

    @Autowired
    private ObjectMapperUtil objectMapperUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Saves a citizen in the database.
     *
     * @param citizen The citizen object to save.
     * @return The saved citizen response DTO.
     * @throws BusinessException If a citizen with the same ID already exists.
     */
    @Override
    public CitizenSimpleResponseDTO saveUser(Citizen citizen) {
        citizen.setPassword(passwordEncoder.encode(citizen.getPassword()));

        citizen.setRole(Role.CITIZEN);
        citizen.setStatus(UserStatus.ACTIVE);

        return Optional.of(citizen)
                .filter(c -> c.getId() == null || !citizenRepository.existsById(c.getId()))
                .map(c -> citizenRepository.save(c))
                .map(saved -> objectMapperUtil.map(saved, CitizenSimpleResponseDTO.class))
                .orElseThrow(() -> new BusinessException(
                        BusinessExceptionMessage.ATTRIBUTE_VALUE_ALREADY_EXISTS.format("citizen")));

    }
}