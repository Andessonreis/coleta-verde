package br.com.coletaverde.domain.waste.repository;

import br.com.coletaverde.domain.waste.entities.Waste;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WasteRepository extends CrudRepository<Waste, UUID> {
}
