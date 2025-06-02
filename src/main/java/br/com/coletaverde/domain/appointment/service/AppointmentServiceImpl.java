package br.com.coletaverde.domain.appointment.service;

import br.com.coletaverde.domain.address.entities.Address;
import br.com.coletaverde.domain.appointment.dto.AppointmentPostRequestDTO;
import br.com.coletaverde.domain.appointment.dto.AppointmentResponseDTO;
import br.com.coletaverde.domain.appointment.entities.Appointment;
import br.com.coletaverde.domain.appointment.enums.AppointmentStatus;
import br.com.coletaverde.domain.appointment.repository.AppointmentRepository;
import br.com.coletaverde.domain.citizen.entities.Citizen;
import br.com.coletaverde.domain.citizen.repository.CitizenRepository;
import br.com.coletaverde.domain.waste.entities.Waste;
import br.com.coletaverde.domain.waste.service.WasteServiceImpl;
import br.com.coletaverde.infrastructure.exceptions.BusinessException;
import br.com.coletaverde.infrastructure.util.ObjectMapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@RequiredArgsConstructor
@Service
public class AppointmentServiceImpl implements IAppointmentService {
    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private final CitizenRepository citizenRepository;

    @Autowired
    private  ObjectMapperUtil objectMapperUtil;
    @Autowired
    private  WasteServiceImpl wasteService;

    @Override
    public AppointmentResponseDTO createAppointment(AppointmentPostRequestDTO dto, String userEmail) {
        Citizen citizen = citizenRepository.findByEmail(userEmail)
                .orElseThrow(() -> new BusinessException("Cidadão não encontrado para o e-mail informado."));

        Address address = citizen.getAddress();
        if (address == null) {
            throw new BusinessException("Cidadão não possui endereço cadastrado.");
        }

        validateAppointmentDate(dto.getScheduledAt());

        Waste waste = wasteService.createWaste(dto.getWaste());

        Appointment appointment = Appointment.builder()
                .scheduledAt(dto.getScheduledAt())
                .optionalPhotoUrl(dto.getOptionalPhotoUrl())
                .status(AppointmentStatus.SCHEDULED)
                .requester(citizen)
                .address(address)
                .wasteItem(waste)
                .build();

        Appointment savedAppointment = appointmentRepository.save(appointment);

        return objectMapperUtil.map(savedAppointment, AppointmentResponseDTO.class);
    }

    /**
     * Valida a data de agendamento, garantindo não nulo e futuro.
     * @param scheduledAt data/hora agendada
     */
    private void validateAppointmentDate(LocalDateTime scheduledAt) {
        if (scheduledAt == null) {
            throw new BusinessException("Data/hora do agendamento é obrigatória.");
        }
        if (scheduledAt.isBefore(LocalDateTime.now())) {
            throw new BusinessException("Data/hora do agendamento não pode ser no passado.");
        }
    }

}
