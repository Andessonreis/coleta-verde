package br.com.coletaverde.domain.penalty.service;

import br.com.coletaverde.domain.appointment.repository.AppointmentRepository;
import br.com.coletaverde.domain.citizen.repository.CitizenRepository;
import br.com.coletaverde.domain.employee.repository.EmployeeRepository;
import br.com.coletaverde.domain.penalty.dto.PenaltyAnalysisDTO;
import br.com.coletaverde.domain.penalty.dto.PenaltyPostRequestDTO;
import br.com.coletaverde.domain.penalty.dto.PenaltyResponseDTO;
import br.com.coletaverde.domain.penalty.dto.mapper.PenaltyMapper;
import br.com.coletaverde.domain.penalty.entities.Penalty;
import br.com.coletaverde.domain.penalty.enums.PenaltyStatus;
import br.com.coletaverde.domain.penalty.repository.PenaltyRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PenaltyServiceImpl implements IPenaltyService {

    private final PenaltyRepository penaltyRepository;
    private final AppointmentRepository appointmentRepository;
    private final CitizenRepository citizenRepository;
    private final EmployeeRepository employeeRepository;
    private final PenaltyMapper penaltyMapper;

    @Override
    @Transactional
    public PenaltyResponseDTO createPenalty(PenaltyPostRequestDTO request, String createdByEmail) {
        var appointment = appointmentRepository.findById(request.getAppointmentId())
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found: " + request.getAppointmentId()));

        var citizen = citizenRepository.findById(request.getCitizenId())
                .orElseThrow(() -> new EntityNotFoundException("Citizen not found: " + request.getCitizenId()));

        var employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new EntityNotFoundException("Employee not found: " + request.getEmployeeId()));

        Penalty penalty = Penalty.builder()
                .appointment(appointment)
                .citizen(citizen)
                .employee(employee)
                .type(request.getType())
                .description(request.getDescription())
                .evidencePhotoUrl(request.getEvidencePhotoUrl())
                .blockDays(request.getBlockDays())
                .status(PenaltyStatus.PENDING_ANALYSIS)
                .reportedAt(LocalDateTime.now())
                .build();

        var savedPenalty = penaltyRepository.save(penalty);

        return penaltyMapper.toResponseDTO(savedPenalty);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PenaltyResponseDTO> listarPenalidades() {
        return penaltyRepository.findAll().stream()
                .map(penaltyMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PenaltyResponseDTO analisarPenalidade(UUID penaltyId, PenaltyAnalysisDTO analysisDTO, String analystEmail) {
        var penalty = penaltyRepository.findById(penaltyId)
                .orElseThrow(() -> new EntityNotFoundException("Penalty not found: " + penaltyId));

        if (penalty.getStatus() != PenaltyStatus.PENDING_ANALYSIS) {
            throw new IllegalStateException("Penalty has already been analyzed. Status: " + penalty.getStatus());
        }

        var analyst = employeeRepository.findByEmail(analystEmail)
                .orElseThrow(() -> new EntityNotFoundException("Analyst not found: " + analystEmail));

        penalty.setAnalyst(analyst);
        penalty.setAnalysisObservations(analysisDTO.getObservations());
        penalty.setAnalysisDate(LocalDateTime.now());

        if (analysisDTO.getApproved()) {
            penalty.setStatus(PenaltyStatus.APPROVED);
            LocalDateTime blockStartDate = LocalDateTime.now();
            penalty.setBlockStartDate(blockStartDate);
            penalty.setBlockEndDate(blockStartDate.plusDays(penalty.getBlockDays()));
        } else {
            penalty.setStatus(PenaltyStatus.REJECTED);
        }

        // Força a escrita no DB para contornar o bug de estado do Hibernate
        penaltyRepository.saveAndFlush(penalty);

        var updatedPenaltyFromDb = penaltyRepository.findById(penaltyId)
                .orElseThrow(() -> new IllegalStateException("Could not re-fetch penalty after analysis: " + penaltyId));

        return penaltyMapper.toResponseDTO(updatedPenaltyFromDb);
    }
}