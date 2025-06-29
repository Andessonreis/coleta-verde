package br.com.coletaverde.domain.penalty.service;

import br.com.coletaverde.domain.appointment.entities.Appointment;
import br.com.coletaverde.domain.appointment.repository.AppointmentRepository;
import br.com.coletaverde.domain.penalty.dto.PenaltyPostRequestDTO;
import br.com.coletaverde.domain.penalty.dto.PenaltyResponseDTO;
import br.com.coletaverde.domain.penalty.entities.Penalty;
import br.com.coletaverde.domain.penalty.enums.PenaltyStatus;
import br.com.coletaverde.domain.penalty.repository.PenaltyRepository;
import br.com.coletaverde.infrastructure.util.ObjectMapperUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class PenaltyServiceImpl implements IPenaltyService {

    private final PenaltyRepository penaltyRepository;
    private final AppointmentRepository appointmentRepository;

    @Autowired
    private ObjectMapperUtil objectMapperUtil;

    @Override
    public PenaltyResponseDTO createPenalty(PenaltyPostRequestDTO request, String createdByEmail) {
        log.info("Creating penalty for appointmentId={} by {}", request.getAppointmentId(), createdByEmail);

        Appointment appointment = appointmentRepository.findById(request.getAppointmentId())
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found"));

        Penalty penalty = objectMapperUtil.map(request, Penalty.class);
        penalty.setAppointment(appointment); 
        penalty.setReportedAt(LocalDateTime.now());
        penalty.setStatus(PenaltyStatus.PENDING_ANALYSIS);
        Penalty savedPenalty = penaltyRepository.save(penalty);

        log.info("Penalty saved successfully with id={}", savedPenalty.getId());

        return objectMapperUtil.map(savedPenalty, PenaltyResponseDTO.class);
    }
}
