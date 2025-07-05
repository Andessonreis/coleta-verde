package br.com.coletaverde.domain.penalty.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PenaltyAnalysisDTO {

    /**
     * A decisão da análise.
     * true = A penalidade foi aprovada e será aplicada.
     * false = A penalidade foi rejeitada.
     */
    @NotNull(message = "A decisão (approved) é obrigatória.")
    private Boolean approved;

    /**
     * A justificativa ou observação do analista sobre a decisão tomada.
     */
    @NotBlank(message = "As observações (observations) são obrigatórias.")
    private String observations;
}