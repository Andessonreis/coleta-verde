package br.com.coletaverde.domain.penalty.service;

import br.com.coletaverde.domain.penalty.dto.PenaltyPostRequestDTO;
import br.com.coletaverde.domain.penalty.dto.PenaltyResponseDTO;

public interface IPenaltyService {
    PenaltyResponseDTO createPenalty(PenaltyPostRequestDTO request, String createdByEmail);
}