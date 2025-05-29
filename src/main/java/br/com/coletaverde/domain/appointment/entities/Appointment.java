package br.com.coletaverde.domain.appointment.entities;

import br.com.coletaverde.domain.address.entities.Address;
import br.com.coletaverde.domain.appointment.enums.AppointmentStatus;
import br.com.coletaverde.domain.citizen.entities.Citizen;
import br.com.coletaverde.domain.waste.entities.Waste;
import br.com.coletaverde.infrastructure.model.PersistenceEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "appointments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class Appointment extends PersistenceEntity {

    @Column(name = "scheduled_at", nullable = false)
    private LocalDateTime scheduledAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "canceled_at")
    private LocalDateTime canceledAt;

    @Column(name = "optional_photo_url", length = 255)
    private String optionalPhotoUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus status = AppointmentStatus.SCHEDULED;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "citizen_id")
    private Citizen requester;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "address_id")
    private Address address;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "waste_id")
    private Waste wasteItem;
}
