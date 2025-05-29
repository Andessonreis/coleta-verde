package br.com.coletaverde.domain.waste.entities;

import br.com.coletaverde.domain.waste.enums.WasteType;
import br.com.coletaverde.infrastructure.model.PersistenceEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "wastes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Waste extends PersistenceEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "wasteType", nullable = false)
    private WasteType type;

    @Column(name = "description", nullable = false)
    private String description;

}

