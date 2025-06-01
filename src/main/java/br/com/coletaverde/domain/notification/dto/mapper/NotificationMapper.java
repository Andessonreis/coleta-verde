package br.com.coletaverde.domain.notification.dto.mapper;

import br.com.coletaverde.domain.notification.dto.NotificationCreateDto;
import br.com.coletaverde.domain.notification.dto.NotificationResponseDto;
import br.com.coletaverde.domain.notification.entities.Notification;

import java.util.List;
import java.util.stream.Collectors;

public class NotificationMapper {
    public static Notification toNotification(NotificationCreateDto dto) {
        Notification notification = new Notification();
        notification.setMessage(dto.getMessage());
        notification.setShippingMethod(dto.getShippingMethod());
        notification.setStatus(dto.getStatus());
        return notification;
    }

    public static NotificationResponseDto toDto(Notification entity) {
        NotificationResponseDto dto = new NotificationResponseDto();
        dto.setId(entity.getId());
        dto.setMessage(entity.getMessage());
        dto.setShippingMethod(entity.getShippingMethod().name());
        dto.setStatus(entity.getStatus().name());
        return dto;
    }

    public static List<NotificationResponseDto> toListDto(List<Notification> notifications) {
        return notifications.stream().map(NotificationMapper::toDto).collect(Collectors.toList());
    }
}
