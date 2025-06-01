package br.com.coletaverde.domain.notification.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import br.com.coletaverde.infrastructure.model.PersistenceEntity;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Notification extends PersistenceEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank
    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "shipping_date")
    private LocalDateTime shippingDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "shipping_method")
    private ShippingMethod shippingMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    public enum Status {
        ENVIADA,
        NAO_ENVIADA,
        FALHA
    }

    public enum ShippingMethod {
        EMAIL,
        SMS
    }
}
