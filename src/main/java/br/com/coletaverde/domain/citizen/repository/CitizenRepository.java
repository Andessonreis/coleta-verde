package br.com.coletaverde.domain.citizen.repository;

import br.com.coletaverde.domain.citizen.entities.Citizen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CitizenRepository extends JpaRepository<Citizen, UUID> {
    @Query("SELECT c FROM citizen c JOIN FETCH c.address WHERE c.email = :email")
    Optional<Citizen> findByEmail(@Param("email") String email);

}
