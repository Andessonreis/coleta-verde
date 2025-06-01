package br.com.coletaverde.domain.notification.service;

import br.com.coletaverde.domain.notification.entities.Notification;
import br.com.coletaverde.domain.notification.repository.NotificationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;

    @Transactional
    public Notification save(Notification obj) {
        try {
            return notificationRepository.save(obj);
        } catch (RuntimeException e) {
            //TODO : tratar exceção
            throw new RuntimeException("Falha ao salvar notificação", e);
        }
    }

    @Transactional(readOnly = true)
    public Notification findById(UUID id) {
        return notificationRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Notification with id %s not found", id))
        );
    }

    @Transactional(readOnly = true)
    public List<Notification> findAll() {
        return notificationRepository.findAll();
    }

}
