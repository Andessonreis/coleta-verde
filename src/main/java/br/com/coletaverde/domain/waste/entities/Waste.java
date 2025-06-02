package br.com.coletaverde.domain.waste.entities;

import br.com.coletaverde.domain.waste.enums.WasteType;
import br.com.coletaverde.infrastructure.model.PersistenceEntity;
import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "wastes")
public class Waste extends PersistenceEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "waste_type", nullable = false, length = 20)
    private WasteType type;

    @Column(name = "description", nullable = false, length = 100)
    private String description;
}
