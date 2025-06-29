package br.com.coletaverde.domain.penalty.repository;

import br.com.coletaverde.domain.penalty.entities.Penalty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PenaltyRepository extends JpaRepository<Penalty, UUID> {
}
