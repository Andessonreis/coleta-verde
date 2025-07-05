package br.com.coletaverde.domain.penalty.service;

import br.com.coletaverde.domain.penalty.dto.PenaltyAnalysisDTO;
import br.com.coletaverde.domain.penalty.dto.PenaltyPostRequestDTO;
import br.com.coletaverde.domain.penalty.dto.PenaltyResponseDTO;

import java.util.List;
import java.util.UUID;

public interface IPenaltyService {
    PenaltyResponseDTO createPenalty(PenaltyPostRequestDTO request, String createdByEmail);
    List<PenaltyResponseDTO> listarPenalidades();
    PenaltyResponseDTO analisarPenalidade(UUID penaltyId, PenaltyAnalysisDTO analysisDTO, String analystEmail);

}