package br.com.coletaverde.domain.appointment.service;

import br.com.coletaverde.domain.address.entities.Address;
import br.com.coletaverde.domain.appointment.dto.AppointmentPostRequestDTO;
import br.com.coletaverde.domain.appointment.dto.AppointmentResponseDTO;
import br.com.coletaverde.domain.appointment.dto.AvailabilityResponse;
import br.com.coletaverde.domain.appointment.dto.DayAvailability;
import br.com.coletaverde.domain.appointment.entities.Appointment;
import br.com.coletaverde.domain.appointment.enums.AppointmentStatus;
import br.com.coletaverde.domain.appointment.repository.AppointmentCountProjection;
import br.com.coletaverde.domain.appointment.repository.AppointmentRepository;
import br.com.coletaverde.domain.citizen.entities.Citizen;
import br.com.coletaverde.domain.citizen.repository.CitizenRepository;
import br.com.coletaverde.domain.employee.entities.Employee;
import br.com.coletaverde.domain.employee.repository.EmployeeRepository;
import br.com.coletaverde.domain.user.repository.UserRepository;
import br.com.coletaverde.domain.waste.entities.Waste;
import br.com.coletaverde.domain.waste.service.WasteServiceImpl;
import br.com.coletaverde.infrastructure.exceptions.BusinessException;
import br.com.coletaverde.infrastructure.util.ObjectMapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class AppointmentServiceImpl implements IAppointmentService {
    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private final CitizenRepository citizenRepository;

    private final UserRepository userRepository;

    @Autowired
    private  ObjectMapperUtil objectMapperUtil;
    @Autowired
    private  WasteServiceImpl wasteService;

    @Value("${app.appointment.availability.days-to-check}")
    private int daysToCheck;

    @Value("${app.appointment.availability.max-per-day}")
    private int defaultMaxAppointments;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

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


    @Override
    public AvailabilityResponse getAvailability() {
        LocalDate startDate = LocalDate.now().plusDays(1);
        Map<LocalDate, Long> appointmentCounts = getAppointmentCounts(startDate);

        List<LocalDate> businessDays = generateBusinessDays(startDate, daysToCheck);
        List<DayAvailability> availableDates = buildAvailabilityList(businessDays, appointmentCounts);

        return AvailabilityResponse.builder()
                .availableDates(availableDates)
                .defaultMaxAppointments(defaultMaxAppointments)
                .build();
    }

    private List<LocalDate> generateBusinessDays(LocalDate startDate, int days) {
        List<LocalDate> businessDays = new ArrayList<>();
        for (int i = 0; businessDays.size() < days; i++) {
            LocalDate date = startDate.plusDays(i);
            if (!(date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY)) {
                businessDays.add(date);
            }
        }
        return businessDays;
    }

    private List<DayAvailability> buildAvailabilityList(List<LocalDate> dates, Map<LocalDate, Long> counts) {
        return dates.stream().map(date -> {
            long currentAppointments = counts.getOrDefault(date, 0L);
            boolean isAvailable = currentAppointments < defaultMaxAppointments;

            return DayAvailability.builder()
                    .date(date.format(DATE_FORMATTER))
                    .available(isAvailable)
                    .currentAppointments(currentAppointments)
                    .maxAppointments(defaultMaxAppointments)
                    .reason(isAvailable ? null : "Limite diário atingido")
                    .build();
        }).collect(Collectors.toList());
    }

    private Map<LocalDate, Long> getAppointmentCounts(LocalDate startDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = startDate.plusDays(daysToCheck).atStartOfDay();

        List<AppointmentStatus> activeStatuses = List.of(
                AppointmentStatus.SCHEDULED
        );

        List<AppointmentCountProjection> results = appointmentRepository.countActiveAppointmentsByDateRange(
                startDateTime, endDateTime, activeStatuses
        );

        return results.stream()
                .collect(Collectors.toMap(
                        AppointmentCountProjection::getAppointmentDate,
                        AppointmentCountProjection::getTotal
                ));
    }

    @Override
    public AppointmentResponseDTO updateAppointment(UUID id, AppointmentPostRequestDTO dto, String userEmail) {
        Appointment existingAppointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Agendamento não encontrado para o ID: " + id));

        // Verifica se o usuário logado é o solicitante
        if (!existingAppointment.getRequester().getEmail().equals(userEmail)) {
            throw new BusinessException("Você não tem permissão para alterar este agendamento.");
        }

        validateAppointmentDate(dto.getScheduledAt());

        Waste updatedWaste = wasteService.createWaste(dto.getWaste());

        existingAppointment.setScheduledAt(dto.getScheduledAt());
        existingAppointment.setOptionalPhotoUrl(dto.getOptionalPhotoUrl());
        existingAppointment.setWasteItem(updatedWaste);

        // Atualiza o status se fornecido
        if (dto.getStatus() != null) {
            existingAppointment.setStatus(dto.getStatus());
        }

        Appointment updatedAppointment = appointmentRepository.save(existingAppointment);

        return objectMapperUtil.map(updatedAppointment, AppointmentResponseDTO.class);
    }

    @Override
    public List<AppointmentResponseDTO> getAllAppointments() {
        List<Appointment> appointments = appointmentRepository.findAllWithDetails();

        return appointments.stream()
                .map(appointment -> objectMapperUtil.map(appointment, AppointmentResponseDTO.class))
                .toList();
    }

    @Override
    public AppointmentResponseDTO getAppointmentById(UUID id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Agendamento não encontrado para o ID: " + id));

        return objectMapperUtil.map(appointment, AppointmentResponseDTO.class);
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


    @Override
    public AppointmentResponseDTO assignAppointment(UUID appointmentId, UUID employeeId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new BusinessException("Agendamento não encontrado para o ID: " + appointmentId));

        if (appointment.getEmployee() != null) {
            throw new BusinessException("Este agendamento já foi atribuído a um funcionário.");
        }

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new BusinessException("Funcionário não encontrado para o ID: " + employeeId));

        appointment.setEmployee(employee);
        appointment.setStatus(AppointmentStatus.SCHEDULED);

        Appointment updated = appointmentRepository.save(appointment);

        return objectMapperUtil.map(updated, AppointmentResponseDTO.class);
    }

    @Override
    public List<Appointment> getAppointmentsByEmployeeEmail(String email) {
        Employee employee = (Employee) userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException("Funcionário não encontrado com email: " + email));

        return appointmentRepository.findAllByEmployeeIdWithDetails(employee.getId());
    }


    @Override
    public AppointmentResponseDTO updateAppointmentStatus(UUID appointmentId, AppointmentStatus newStatus, String observacoes, String userEmail) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new BusinessException("Agendamento não encontrado para o ID: " + appointmentId));

        boolean isEmployee = appointment.getEmployee() != null &&
                appointment.getEmployee().getEmail().equals(userEmail);

        boolean isCitizen = appointment.getRequester() != null &&
                appointment.getRequester().getEmail().equals(userEmail);

        if (!isEmployee && !isCitizen) {
            throw new BusinessException("Você não tem permissão para alterar o status deste agendamento.");
        }

        // Cidadão só pode cancelar
        if (isCitizen && newStatus != AppointmentStatus.CANCELED) {
            throw new BusinessException("Cidadãos só podem cancelar agendamentos.");
        }

        // Impede reedição se já finalizado
        if (appointment.getStatus() == AppointmentStatus.COMPLETED ||
                appointment.getStatus() == AppointmentStatus.NOT_COMPLETED) {
            throw new BusinessException("Este agendamento já foi finalizado.");
        }

        // Atualiza status
        appointment.setStatus(newStatus);

        // Define observações, se fornecidas ( motivo do problema)
        if (observacoes != null && !observacoes.trim().isEmpty()) {
            appointment.setOptionalPhotoUrl(observacoes);
        }

        appointment.setUpdatedAt(LocalDateTime.now());

        Appointment updated = appointmentRepository.save(appointment);

        return objectMapperUtil.map(updated, AppointmentResponseDTO.class);
    }


    @Override
    public List<AppointmentResponseDTO> getAppointmentsByCitizenEmail(String email) {
        Citizen citizen = citizenRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException("Cidadão não encontrado com email: " + email));

        List<Appointment> appointments = appointmentRepository.findAllByCitizenIdWithDetails(citizen.getId());

        return appointments.stream()
                .map(appointment -> objectMapperUtil.map(appointment, AppointmentResponseDTO.class))
                .toList();
    }

}
