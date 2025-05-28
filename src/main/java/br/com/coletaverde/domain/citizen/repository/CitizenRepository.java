package br.com.coletaverde.domain.citizen.repository;

import br.com.coletaverde.domain.citizen.entities.Citizen;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CitizenRepository extends CrudRepository<Citizen, UUID> {

    Optional<Citizen> findByEmail(String email);
}
