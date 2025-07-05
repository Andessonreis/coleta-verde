package br.com.coletaverde.domain.penalty.dto.mapper;

import br.com.coletaverde.domain.citizen.dto.CitizenBasicDTO;
import br.com.coletaverde.domain.employee.dto.EmployeeBasicDTO;
import br.com.coletaverde.domain.penalty.dto.PenaltyResponseDTO;
import br.com.coletaverde.domain.penalty.entities.Penalty;
import org.springframework.stereotype.Component;

@Component
public class PenaltyMapper {

    public PenaltyResponseDTO toResponseDTO(Penalty penalty) {
        if (penalty == null) {
            return null;
        }

        PenaltyResponseDTO dto = new PenaltyResponseDTO();

        // Mapeamento dos campos diretos
        dto.setId(penalty.getId());
        dto.setDescription(penalty.getDescription());
        dto.setType(penalty.getType());
        dto.setStatus(penalty.getStatus());
        dto.setEvidencePhotoUrl(penalty.getEvidencePhotoUrl());
        dto.setBlockDays(penalty.getBlockDays());
        dto.setReportedAt(penalty.getReportedAt());
        dto.setAnalysisDate(penalty.getAnalysisDate());
        dto.setAnalysisObservations(penalty.getAnalysisObservations());
        dto.setBlockStartDate(penalty.getBlockStartDate());
        dto.setBlockEndDate(penalty.getBlockEndDate());

        // Mapeamento das associações
        if (penalty.getAppointment() != null) {
            dto.setAppointmentId(penalty.getAppointment().getId());
        }

        if (penalty.getCitizen() != null) {
            var citizen = penalty.getCitizen();
            dto.setCitizenId(citizen.getId());
            dto.setCidadao(new CitizenBasicDTO(citizen.getId(), citizen.getName(), citizen.getEmail()));
        }


        if (penalty.getEmployee() != null) {
            var employee = penalty.getEmployee();
            dto.setEmployeeId(employee.getId());
            dto.setFuncionario(new EmployeeBasicDTO(employee.getId(), employee.getName(), employee.getRegistration()));
        }

        if (penalty.getAnalyst() != null) {
            var analyst = penalty.getAnalyst();
            dto.setAnalista(new EmployeeBasicDTO(analyst.getId(), analyst.getName(), analyst.getRegistration()));
        }

        return dto;
    }
}