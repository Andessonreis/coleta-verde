package br.com.coletaverde.domain.notification.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponseDto {
    private UUID id;
    private String message;
    private String shippingMethod;
    private String status;
}
