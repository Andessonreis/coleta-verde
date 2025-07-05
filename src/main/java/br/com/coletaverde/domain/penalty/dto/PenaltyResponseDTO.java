package br.com.coletaverde.domain.penalty.dto;

import br.com.coletaverde.domain.citizen.dto.CitizenBasicDTO;
import br.com.coletaverde.domain.employee.dto.EmployeeBasicDTO;
import br.com.coletaverde.domain.penalty.enums.PenaltyStatus;
import br.com.coletaverde.domain.penalty.enums.PenaltyType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO (Data Transfer Object) que representa a estrutura de dados de uma penalidade
 * a ser enviada como resposta JSON para o frontend.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PenaltyResponseDTO {

    /** O ID único da penalidade. */
    private UUID id;

    /** O ID do agendamento de coleta relacionado. */
    @JsonProperty("coletaId")
    private UUID appointmentId;

    /** O ID do cidadão penalizado. */
    @JsonProperty("cidadaoId")
    private UUID citizenId;

    /** O ID do funcionário que criou a penalidade. */
    @JsonProperty("funcionarioId")
    private UUID employeeId;

    /** O tipo da infração. */
    @JsonProperty("tipo")
    private PenaltyType type;

    /** A descrição original do problema reportado. */
    @JsonProperty("descricao")
    private String description;

    /** A URL da foto de evidência. */
    @JsonProperty("fotoEvidencia")
    private String evidencePhotoUrl;

    /** O número de dias de bloqueio a serem aplicados. */
    @JsonProperty("diasBloqueio")
    private Integer blockDays;

    /** A data em que a penalidade foi reportada. */
    @JsonProperty("dataReporte")
    private LocalDateTime reportedAt;

    /** O status atual da penalidade (PENDING_ANALYSIS, APPROVED, REJECTED). */
    private PenaltyStatus status;

    /** Objeto com os dados básicos do cidadão penalizado. */
    private CitizenBasicDTO cidadao;

    /** Objeto com os dados básicos do funcionário que criou a penalidade. */
    private EmployeeBasicDTO funcionario;

    /** Objeto com os dados básicos do analista que avaliou a penalidade (pode ser nulo). */
    @JsonProperty("analista")
    private EmployeeBasicDTO analista;

    /** A data em que a análise foi realizada (pode ser nula). */
    @JsonProperty("dataAnalise")
    private LocalDateTime analysisDate;

    /** As observações feitas pelo analista (pode ser nulo). */
    @JsonProperty("observacoesAnalista")
    private String analysisObservations;

    /** A data de início do período de bloqueio (pode ser nula). */
    @JsonProperty("dataInicioBloqueio")
    private LocalDateTime blockStartDate;

    /** A data de fim do período de bloqueio (pode ser nula). */
    @JsonProperty("dataFimBloqueio")
    private LocalDateTime blockEndDate;
}