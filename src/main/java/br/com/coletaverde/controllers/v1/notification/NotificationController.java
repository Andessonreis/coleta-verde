package br.com.coletaverde.controllers.v1.notification;

import br.com.coletaverde.domain.notification.dto.NotificationCreateDto;
import br.com.coletaverde.domain.notification.dto.NotificationResponseDto;
import br.com.coletaverde.domain.notification.dto.mapper.NotificationMapper;
import br.com.coletaverde.domain.notification.entities.Notification;
import br.com.coletaverde.domain.notification.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    @PostMapping(path = "/notification", consumes = "application/json", produces = "application/json")
    public ResponseEntity<NotificationResponseDto> save (@Valid @RequestBody NotificationCreateDto obj) {
        Notification notification = notificationService.save(NotificationMapper.toNotification(obj));
        return ResponseEntity.status(HttpStatus.CREATED).body(NotificationMapper.toDto(notification));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationResponseDto> findById (@PathVariable UUID id) {
        Notification notification = notificationService.findById(id);
        return ResponseEntity.ok().body(NotificationMapper.toDto(notification));
    }

    @GetMapping
    public ResponseEntity<List<NotificationResponseDto>> findAll () {
        List<Notification> notifications = notificationService.findAll();
        return ResponseEntity.ok(NotificationMapper.toListDto(notifications));
    }
}
