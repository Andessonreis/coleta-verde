package br.com.coletaverde.domain.penalty.entities;

import br.com.coletaverde.domain.appointment.entities.Appointment;
import br.com.coletaverde.domain.citizen.entities.Citizen;
import br.com.coletaverde.domain.employee.entities.Employee;
import br.com.coletaverde.domain.penalty.enums.PenaltyStatus;
import br.com.coletaverde.domain.penalty.enums.PenaltyType;
import br.com.coletaverde.infrastructure.model.PersistenceEntity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Representa uma penalidade aplicada a um cidadão, reportada por um funcionário
 * e posteriormente analisada por outro.
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "penalties")
public class Penalty extends PersistenceEntity {

    /**
     * O agendamento de coleta ao qual esta penalidade está associada.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", nullable = false)
    private Appointment appointment;

    /**
     * O cidadão que recebeu a penalidade.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "citizen_id")
    private Citizen citizen;

    /**
     * O funcionário que reportou/criou a penalidade.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    /**
     * O funcionário (analista) que avaliou a penalidade.
     * Permanece nulo até que a análise seja concluída.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "analyst_id")
    private Employee analyst;

    /**
     * O texto com a justificativa do analista sobre a aprovação ou rejeição.
     */
    @Column(columnDefinition = "TEXT")
    private String analysisObservations;

    /**
     * A data e hora em que a análise foi realizada.
     */
    private LocalDateTime analysisDate;

    /**
     * A data e hora de início do bloqueio do cidadão, se a penalidade for aprovada.
     */
    private LocalDateTime blockStartDate;

    /**
     * A data e hora de término do bloqueio do cidadão.
     */
    private LocalDateTime blockEndDate;

    /**
     * O tipo da infração cometida.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PenaltyType type;

    /**
     * A descrição detalhada do problema reportado pelo funcionário.
     */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    /**
     * A URL da foto enviada como evidência da infração.
     */
    @Column(nullable = false)
    private String evidencePhotoUrl;

    /**
     * A quantidade de dias que o cidadão será bloqueado caso a penalidade seja aprovada.
     */
    @Column(nullable = false)
    private Integer blockDays;

    /**
     * A data e hora em que a penalidade foi criada.
     */
    @Column(nullable = false)
    private LocalDateTime reportedAt;

    /**
     * O status atual da penalidade (ex: PENDING_ANALYSIS, APPROVED, REJECTED).
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PenaltyStatus status;
}