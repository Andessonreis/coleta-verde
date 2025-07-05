package br.com.coletaverde.domain.penalty.repository;

import br.com.coletaverde.domain.penalty.entities.Penalty;
import br.com.coletaverde.domain.penalty.enums.PenaltyStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable; 
import java.util.List;
import java.util.UUID;

public interface PenaltyRepository extends JpaRepository<Penalty, UUID> {
    /**
     * Busca todas as penalidades, trazendo junto (JOIN FETCH) todas as entidades
     * relacionadas em uma única consulta otimizada para evitar o problema N+1.
     */
    @Query("SELECT p FROM Penalty p " +
           "LEFT JOIN FETCH p.citizen " +
           "LEFT JOIN FETCH p.employee " +
           "LEFT JOIN FETCH p.analyst " +
           "LEFT JOIN FETCH p.appointment")
    List<Penalty> findAllWithDetails();

        /**
     * Encontra a penalidade mais recente com base na data de análise,
     * para um cidadão específico e com um status específico, usando uma query JPQL explícita.
     *
     * @param citizenId O ID do cidadão a ser verificado.
     * @param status O status da penalidade a ser buscada (ex: APPROVED).
     * @param pageable Objeto para controle de paginação, usado aqui para limitar o resultado a apenas 1.
     * @return Uma Lista contendo no máximo uma penalidade.
     */
    @Query("SELECT p FROM Penalty p WHERE p.citizen.id = :citizenId AND p.status = :status ORDER BY p.analysisDate DESC")
    List<Penalty> findLatestApprovedPenalty(
            @Param("citizenId") UUID citizenId,
            @Param("status") PenaltyStatus status,
            Pageable pageable
    );
}
