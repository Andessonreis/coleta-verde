package br.com.coletaverde.domain.notification.dto;

import br.com.coletaverde.domain.notification.entities.Notification;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationCreateDto {

    @NotNull(message = "Message cannot be blank.")
    private String message;

    @NotNull(message = "Shipping method cannot be blank.")
    private Notification.ShippingMethod shippingMethod;

    @NotNull(message = "Status cannot be blank.")
    private Notification.Status status;
}
