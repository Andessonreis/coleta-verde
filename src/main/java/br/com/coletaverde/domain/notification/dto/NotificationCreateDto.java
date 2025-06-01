package br.com.coletaverde.domain.notification.dto;

import br.com.coletaverde.domain.notification.entities.Notification;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationCreateDto {

    @NotBlank(message = "Message cannot be blank.")
    private String message;

    @NotBlank(message = "Shipping method cannot be blank.")
    private Notification.ShippingMethod shippingMethod;

    @NotBlank(message = "Status cannot be blank.")
    private Notification.Status status;
}
